package com.sdc2ch.service.admin.impl;

import static java.util.stream.Collectors.groupingBy;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.sdc2ch.aiv.event.IFirebaseNotificationEvent;
import com.sdc2ch.aiv.event.IFirebaseNotificationEvent.Priority;
import com.sdc2ch.core.security.auth.I2CHAuthorization;
import com.sdc2ch.repo.io.AllocatedGroupIO;
import com.sdc2ch.repo.io.MobileAppInfoIO;
import com.sdc2ch.repo.io.TmsCarIO;
import com.sdc2ch.repo.io.TmsDriverIO;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.pubsub.I2ChEventManager;
import com.sdc2ch.require.pubsub.I2ChEventPublisher;
import com.sdc2ch.require.service.I2ChUserService;
import com.sdc2ch.service.admin.IAllocationConfirmGroupService;
import com.sdc2ch.service.admin.IAllocationConfirmService;
import com.sdc2ch.service.common.IComboBoxService;
import com.sdc2ch.service.common.model.ComboBoxVo;
import com.sdc2ch.service.event.IAllocateCanceledEvent;
import com.sdc2ch.service.event.IAllocateChangeEvent;
import com.sdc2ch.service.event.model.FrontExitVo;
import com.sdc2ch.tms.service.ITmsPlanService;
import com.sdc2ch.tms.service.ITmsSmsService;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;
import com.sdc2ch.web.admin.repo.dao.T_CaralcCnfirmGroupRepository2;
import com.sdc2ch.web.admin.repo.dao.T_ERPQueryBuilderRepository;
import com.sdc2ch.web.admin.repo.domain.alloc.QT_CARALC_CNFIRM_GROUP2;
import com.sdc2ch.web.admin.repo.domain.alloc.T_CARALC_CNFIRM2;
import com.sdc2ch.web.admin.repo.domain.alloc.T_CARALC_CNFIRM_GROUP2;
import com.sdc2ch.web.admin.repo.domain.v.V_CARALC_MSTR;
import com.sdc2ch.web.service.IAllocatedGroupService;
import com.sdc2ch.web.service.IMobileAppService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
class AllocationConfirmGroupServiceImpl implements IAllocationConfirmGroupService, IAllocatedGroupService {

	@Autowired I2CHAuthorization auth;
	@Autowired IAllocationConfirmService confirmSvc;  
	@Autowired IMobileAppService appSvc;          
	@Autowired I2ChUserService userSvc;           
	@Autowired ITmsPlanService tmsPlanSvc;
	@Autowired IComboBoxService comboSvc;
	@Autowired ITmsSmsService smsSvc;
	

	@Autowired T_CaralcCnfirmGroupRepository2 repo2;
	private I2ChEventPublisher<IFirebaseNotificationEvent> eventPush;
	private ExecutorService service = Executors.newFixedThreadPool(4);

	
	@Autowired AdmQueryBuilder builder;

	@Value(value = "${CONFIRM_TBL}")
	private String CONFIRM_TBL;

	
	private I2ChEventPublisher<IAllocateCanceledEvent> eventCanceled;
	private I2ChEventPublisher<IAllocateChangeEvent> eventChanged;

	@Autowired
	public void init(I2ChEventManager manager) {
		this.eventPush = manager.regist(IFirebaseNotificationEvent.class);
		this.eventCanceled = manager.regist(IAllocateCanceledEvent.class);
		this.eventChanged = manager.regist(IAllocateChangeEvent.class);
	}

	@Override
	public List<T_CARALC_CNFIRM_GROUP2> search2(String dlvyDe, String fctryCd, String carAlcType, String routeNo,
			String vrn, String dcsnYn) {

		List<V_CARALC_MSTR> mstrs = confirmSvc.search2(dlvyDe, fctryCd, carAlcType, routeNo, vrn, dcsnYn);
		Map<String, List<V_CARALC_MSTR>> mapped = mstrs.stream().collect(groupingBy(V_CARALC_MSTR::getVrn));
		List<T_CARALC_CNFIRM_GROUP2> groups = Lists.newArrayList(repo2.findAll(predicate2(dlvyDe, fctryCd, carAlcType, dcsnYn, vrn)));

		List<T_CARALC_CNFIRM_GROUP2> movedList = new ArrayList<>();

		
		groups.removeIf(g -> {

			if(mapped.get(g.getVrn()) == null) {
				movedList.add(g);
			}
			return mapped.get(g.getVrn()) == null;
		});

		
		groups.forEach(g -> {
			List<V_CARALC_MSTR> _mstrs = mapped.get(g.getVrn());
			String hint = _mstrs.stream().map(V_CARALC_MSTR::getRouteNo).collect(Collectors.joining(","));
			g.setChange(isChange(g.getRouteHint(), hint));
			g.setRouteHint(hint);
			g.addAll(convert(g, _mstrs));
		});


		
		List<T_CARALC_CNFIRM_GROUP2> newGroups = mapped.keySet().stream().filter(k -> !contains(groups, k)).map(k -> {
			T_CARALC_CNFIRM_GROUP2 _group = new T_CARALC_CNFIRM_GROUP2();
			V_CARALC_MSTR _mstr = mapped.get(k).stream().findFirst().orElse(null);
			_group.addAll(convert(null, mapped.get(k)));
			if(_mstr != null) {
				_group.setDlvyDe(dlvyDe);
				_group.setDriverCd(_mstr.getDriverCd());
				_group.setMobileNo(_mstr.getMobileNo());
				_group.setCaralcTy(_mstr.getCaraclTy());
				_group.setFctryCd(_mstr.getFctryCd());
				_group.setAdjustTime(_mstr.getScheStartTime());
				_group.setLdngExpcTime(_mstr.getScheStartTime());
				_group.setVrn(_mstr.getVrn());
			}
			return _group;
		}).collect(Collectors.toList());

		groups.addAll(newGroups);

		if(!StringUtils.isEmpty(dcsnYn)) {
			groups.removeIf(g -> ("Y".equals(dcsnYn) ? g.getTrnsmisDt() == null : g.getTrnsmisDt() != null));
		}
		return leftJoin(groups, listFrontExit(dlvyDe, fctryCd));
	}

	private List<T_CARALC_CNFIRM_GROUP2> leftJoin(List<T_CARALC_CNFIRM_GROUP2> groups, List<FrontExitVo> list) {
		Map<String, List<FrontExitVo>> mapped = list.stream().collect(Collectors.groupingBy(v -> group(v)));
		for(T_CARALC_CNFIRM_GROUP2 g : groups) {

			if(mapped.get(g.getDlvyDe() + g.getVrn()) != null) {
				boolean frontExit = mapped.get(g.getDlvyDe() + g.getVrn()).stream()
						.filter(v -> v.getFrontExitTime() != null)
						.allMatch(v -> v.getFrontExitTime().isBefore(LocalDateTime.now()));
				g.setFrontExit(frontExit);
			}
		}

		int length = groups.stream().map(g -> g.getRoutes().size()).reduce((a, b) -> a + b).orElse(0);
		log.info("row count => {}", length);

		return groups;
	}


	private String group(FrontExitVo v) {
		return v.getDlvyDe() + v.getVrn();
	}

	private boolean isChange(String routeHint, String hint) {
		if(StringUtils.isEmpty(routeHint))
			return true;
		if(StringUtils.isEmpty(hint))
			return true;
		return routeHint.hashCode() != hint.hashCode();
	}

	private boolean contains(List<T_CARALC_CNFIRM_GROUP2> groups, String vrn) {
		return groups.stream().anyMatch(g -> g.getVrn().equals(vrn));
	}

	private List<T_CARALC_CNFIRM2> convert(T_CARALC_CNFIRM_GROUP2 g, List<V_CARALC_MSTR> mstrs){

		List<T_CARALC_CNFIRM2> confirms = mstrs.stream().map(m -> {
			T_CARALC_CNFIRM2 confirm = new T_CARALC_CNFIRM2();
			confirm.setAdjustTime(m.getScheStartTime());
			confirm.setCaralcTy(m.getCaraclTy());
			confirm.setDlvyDe(m.getDlvyDe());
			confirm.setDriverCd(m.getDriverCd());
			confirm.setFctryCd(m.getFctryCd());
			confirm.setLdngExpcTime(m.getScheStartTime());
			confirm.setMobileNo(m.getMobileNo());
			confirm.setRouteNo(m.getRouteNo());
			confirm.setVrn(m.getVrn());

			if(g != null) {
				confirm.setTrnsmisDt(g.getTrnsmisDt());
				confirm.setTrnsmisUser(g.getTrnsmisUser());
				confirm.setCnfirmDt(g.getCnfirmDt());
				confirm.setCnfirmUser(g.getCnfirmUser());
			}

			return confirm;
		}).collect(Collectors.toList());
		return confirms;
	}

	private Predicate predicate2(String dlvyDe, String fctryCd, String carAlcType, String dcsnYn, String vrn) {

		BooleanBuilder where = new BooleanBuilder();
		QT_CARALC_CNFIRM_GROUP2 mstr = QT_CARALC_CNFIRM_GROUP2.t_CARALC_CNFIRM_GROUP2;
		where.and(mstr.fctryCd.eq(fctryCd)).and(mstr.dlvyDe.eq(dlvyDe));

		if(!StringUtils.isEmpty(carAlcType)) {
			ComboBoxVo vo = comboSvc.findCarAlcTypeCombo().stream().filter(c -> c.getKey().equals(carAlcType)).findFirst().orElse(null);
			if(vo != null) {
				where.and(mstr.caralcTy.eq(vo.getValue()));
			}
		}
		if(!StringUtils.isEmpty(vrn)) {
			where.and(mstr.vrn.like("%" +vrn+"%"));
		}
		return where;
	}

	private boolean isChangeTime(String src, String tag) {
		return !nullSafeTimeString(src).equals(nullSafeTimeString(tag));
	}

	private String nullSafeTimeString(String time) {
		if(StringUtils.isEmpty(time))
			time = "00:00";
		return time;
	}

	private void fireEvent(final IUser user, T_CARALC_CNFIRM_GROUP2 confirm) {
		MobileAppInfoIO appInfo = appSvc.findAppInfoByUser(user).orElse(null);
		if(appInfo != null ) {
			try {
				String msg = confirm.getId() == null ? "확정" : "변경";
				TmsDriverIO driver  = (TmsDriverIO) user.getUserDetails();
				TmsCarIO car = driver.getCar();

				if(confirm.getId() == null) {
					T_CARALC_CNFIRM_GROUP2 newGroup = (T_CARALC_CNFIRM_GROUP2) findByDlvyDeAndVrn(confirm.getDlvyDe(), confirm.getVrn());
					if(newGroup != null) {
						confirm = newGroup;
					}
				}
				eventPush.fireEvent(com.sdc2ch.service.event.model.AppPushEvent.builder()
				.appKey(appInfo.getAppTkn())
				.contents(String.format("[서울우유2CH] %s 차량의 %s일 배차가 %s되었습니다.", car.getVrn(), confirm.getDlvyDe(), msg))
				.datas(confirm.getId())
				.priority(Priority.high)
				.mobileNo(user.getMobileNo())
				.user(user)
				.build());
			}catch (Exception e) {
				log.error("{}", e);
			}

		}else {
			
			TmsDriverIO driver  = (TmsDriverIO) user.getUserDetails();
			TmsCarIO car = driver.getCar();
			String msg = confirm.getId() == null ? "확정" : "변경";
			smsSvc.sendSms(user.getUsername(), String.format("[서울우유2CH] %s 차량의 %s일 배차가 %s되었습니다.", car.getVrn(), confirm.getDlvyDe(), msg), user.getMobileNo());
		}

		if(confirm.getTrnsmisDt() == null) {
			Long id = confirm.getId();
			eventCanceled.fireEvent(new IAllocateCanceledEvent() {
				@Override
				public IUser user() {
					return user;
				}
				@Override
				public Long getAllocatedGroupId() {
					return id;
				}
			});
		}
		
		
		if(confirm.getId() != null){
			Long id = confirm.getId();
			eventChanged.fireEvent(new IAllocateChangeEvent() {
				@Override
				public IUser user() {
					return user;
				}
				@Override
				public Long getAllocatedGroupId() {
					return id;
				}
			});
		}
	}

	@Override
	public void syncDb() {

	}

	@Override
	public Optional<T_CARALC_CNFIRM_GROUP2> findById(Long id) {
		Optional<T_CARALC_CNFIRM_GROUP2> group = repo2.findById(id);
		group.ifPresent(g -> {
			g.setRoutes(convert(g, confirmSvc.search2(g.getDlvyDe(), g.getFctryCd(), null, null, g.getVrn(), null)));
		});
		return group;
	}

	@Override
	public Optional<T_CARALC_CNFIRM_GROUP2> findLastByUser(IUser user) {
		QT_CARALC_CNFIRM_GROUP2 QGROUP = QT_CARALC_CNFIRM_GROUP2.t_CARALC_CNFIRM_GROUP2;
		BooleanBuilder where = new BooleanBuilder();
		where.and(QGROUP.driverCd.eq(user.getUsername()));
		return Optional.of(builder.create().selectFrom(QGROUP).where(where).orderBy(QGROUP.dlvyDe.desc()).limit(1)

				.fetchOne());

	}

	@Override
	public Optional<T_CARALC_CNFIRM_GROUP2> findDlvyByUser(IUser user, String dlvyDe) {
		QT_CARALC_CNFIRM_GROUP2 QGROUP = QT_CARALC_CNFIRM_GROUP2.t_CARALC_CNFIRM_GROUP2;
		
		
		return builder.create()
				.selectFrom(QGROUP)
				.where(predicate(user.getUsername(), dlvyDe))
				.orderBy(QGROUP.id.desc()).limit(1)
				.fetch().stream().findFirst();

	}

	public Predicate predicate(String id, String dlvyDe) {
		QT_CARALC_CNFIRM_GROUP2 QGROUP = QT_CARALC_CNFIRM_GROUP2.t_CARALC_CNFIRM_GROUP2;
		BooleanBuilder where = new BooleanBuilder();
		where.and(QGROUP.driverCd.eq(id));
		if(!StringUtils.isEmpty(dlvyDe)) {
			where.and(QGROUP.dlvyDe.eq(dlvyDe));
		}
		return where;
	}

	@Override
	public T_CARALC_CNFIRM_GROUP2 save(T_CARALC_CNFIRM_GROUP2 group) {
		return repo2.save(group);
	}

	@Override
	public Optional<T_CARALC_CNFIRM_GROUP2> findLastConfirmGroupByUser(IUser user) {
		QT_CARALC_CNFIRM_GROUP2 QGROUP = QT_CARALC_CNFIRM_GROUP2.t_CARALC_CNFIRM_GROUP2;
		return builder.create()
				.selectFrom(QGROUP)
				.where(QGROUP.driverCd.eq(user.getUsername()))
				.orderBy(QGROUP.dlvyDe.desc(), QGROUP.id.desc()).limit(2).fetch()
				.stream()
				.filter(g -> g.getRoutes()
						.stream()
						.allMatch(r -> r.getTrnsmisDt() != null)).findFirst();
	}

	@Override
	public Optional<AllocatedGroupIO> findAllocatedGroupById(Long allocatedGroupId) {
		return Optional.ofNullable(findById(allocatedGroupId).orElse(null));
	}

	@Override
	public Optional<AllocatedGroupIO> findLastAllocatedGroupByUser(IUser user) {
		return Optional.ofNullable(findLastConfirmGroupByUser(user).orElse(null));
	}

	public AllocatedGroupIO findByDlvyDeAndVrn(String dlvyDe, String vrn) {
		QT_CARALC_CNFIRM_GROUP2 Qgroup = QT_CARALC_CNFIRM_GROUP2.t_CARALC_CNFIRM_GROUP2;
		return builder.create().selectFrom(Qgroup).where(Qgroup.dlvyDe.eq(dlvyDe).and(Qgroup.vrn.eq(vrn))).fetchOne();
	}

	private class Task implements Runnable {
		List<T_CARALC_CNFIRM_GROUP2> tasks;
		private Task(List<T_CARALC_CNFIRM_GROUP2> tasks) {
			this.tasks = tasks;
		}
		@Override
		public void run() {
			tasks.forEach(cf -> {
				IUser user = userSvc.findByUsername(cf.getDriverCd()).orElse(null);
				fireEvent(user, cf);
			});
		}
	}

	@Override
	public List<T_CARALC_CNFIRM_GROUP2> allocateAll2(List<T_CARALC_CNFIRM_GROUP2> groups) {

		executeUpdate(groups);
		executeInsert(groups);
		
		service.execute(new Task(groups));

		return groups;
	}

	private void executeUpdate(List<T_CARALC_CNFIRM_GROUP2> groups) {
		groups = groups.stream().filter(g -> g.getId() != null).collect(Collectors.toList());
		List<Long> ids = groups.stream().map(g -> g.getId()).collect(Collectors.toList());
		if(ids != null && !ids.isEmpty()) {
			builder.createNativeQuery(
					"UPDATE " +CONFIRM_TBL + " "
					+ "SET TRNSMIS_USER_ID = ?1, "
					+ "TRNSMIS_DT = ?2  "
					+ "WHERE ROW_ID IN (?3)", auth.userContext().getUsername(),  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), ids);

			List<T_CARALC_CNFIRM_GROUP2> updateTimes = groups.stream()
					.filter(cf -> isChangeTime(cf.getAdjustTime(), cf.getLdngExpcTime()))
					.collect(Collectors.toList());
			builder.batchUpdate(updateTimes);
		}

	}
	
	private void executeInsert(List<T_CARALC_CNFIRM_GROUP2> groups) {

		groups = groups.stream().filter(g -> g.getId() == null).collect(Collectors.toList());

		if(groups != null && !groups.isEmpty()) {
			String query = IAllocationConfirmGroupService.insertBatchQuery;
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String params2 = groups.stream().filter(g -> g.getId() == null).map(group -> {
				StringBuilder sb = new StringBuilder();
				sb.append("(");
				sb.append(nullConvert(fmt.format(new Date()))).append(",");
				sb.append(nullConvert(fmt.format(new Date()))).append(",");
				sb.append(nullConvert(group.getAdjustTime())).append(",");
				sb.append(nullConvert(group.getCaralcTy())).append(",");
				sb.append(nullConvert(group.getCnfirmDt())).append(",");
				sb.append(nullConvert(group.getCnfirmUser())).append(",");
				sb.append(nullConvert(group.getDlvyDe())).append(",");
				sb.append(nullConvert(group.getDriverCd())).append(",");
				sb.append(nullConvert(group.getFctryCd())).append(",");
				sb.append(nullConvert(group.getLdngExpcTime())).append(",");
				sb.append(nullConvert(group.getMobileNo())).append(",");
				sb.append(nullConvert(group.getRouteHint())).append(",");
				sb.append(nullConvert(fmt.format(group.getTrnsmisDt()))).append(",");
				sb.append(nullConvert(group.getTrnsmisUser())).append(",");
				sb.append(nullConvert(group.getVrn())).append(")");
				return sb.toString();
			}).collect(Collectors.joining(","));

			query = query + " " + params2;

			log.info("caralcCnfirmInsertQuery={}", query);

			builder.createNativeQuery(query);
		}

	}

	private String nullConvert(Object o) {
		return o == null? null : ("'" + o + "'");
	}

	@Override
	public List<T_CARALC_CNFIRM_GROUP2> cancelAll2(List<T_CARALC_CNFIRM_GROUP2> groups) {
		builder.batchUpdate(groups.stream().filter(g -> g.getId() != null).collect(Collectors.toList()));
		return groups;
	}

	private List<FrontExitVo> listFrontExit(String dlvyDe, String fctryCd) {

		LocalDate _dlvyDe = LocalDate.parse(dlvyDe, DateTimeFormatter.ofPattern("yyyyMMdd"));
		String query = String.format(T_ERPQueryBuilderRepository.FRONT_EXIT_QUERY, _dlvyDe.toString(), fctryCd);
		List<?> results = builder.createSelectNativeQuery(query);
		List<FrontExitVo> vos = null;
		if(results != null) {
			vos = results.stream().filter(r -> r != null).map(r -> {
				Object[] o = (Object[]) r;
				FrontExitVo vo = new FrontExitVo();
				vo.setDlvyDe(dlvyDe);
				vo.setRouteNo(o[0] +"");
				vo.setFctryCd(o[1] + "");
				vo.setVrn(o[2] +"");
				vo.setDriverCd(o[3]+"");

				if(o[4] != null) {
					Timestamp ts = (Timestamp) o[4];
					vo.setFrontExitTime(ts.toLocalDateTime());
				}
				return vo;
			}).collect(Collectors.toList());
		}
		return vos;
	}

	@Override
	public List<AllocatedGroupIO> findAllocatedGroupByDlvyDe(String DlvyDe) {
		return Optional.ofNullable(findByDlvyDe(DlvyDe)).orElse(Collections.EMPTY_LIST);
	}

	private List<T_CARALC_CNFIRM_GROUP2> findByDlvyDe(String DlvyDe) {
		QT_CARALC_CNFIRM_GROUP2 QGROUP = QT_CARALC_CNFIRM_GROUP2.t_CARALC_CNFIRM_GROUP2;
		return builder.create()
				.selectFrom(QGROUP)
				.where(QGROUP.dlvyDe.eq(DlvyDe))
				.orderBy(QGROUP.id.desc()).fetch();

	}

}
