package com.sdc2ch.service.mobile.impl;

import static com.sdc2ch.core.lambda.tuple.Tuple.tuple;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.Projections;
import com.sdc2ch.aiv.event.IFirebaseNotificationEvent;
import com.sdc2ch.aiv.event.IFirebaseNotificationEvent.Priority;
import com.sdc2ch.ars.enums.CallType;
import com.sdc2ch.ars.enums.SenderType;
import com.sdc2ch.ars.event.IArsEvent;
import com.sdc2ch.core.domain.Location;
import com.sdc2ch.core.lambda.seq.Seq;
import com.sdc2ch.core.security.auth.error.ErrorCode;
import com.sdc2ch.prcss.ds.IShippingStateService2;
import com.sdc2ch.prcss.ds.event.ShippingStateEvent;
import com.sdc2ch.prcss.ds.event.IShippingContextEvent.EventAction;
import com.sdc2ch.prcss.ds.io.ShippingPlanIO;
import com.sdc2ch.prcss.ds.vo.ShipStateVo2;
import com.sdc2ch.prcss.eb.IEmptyBoxService;
import com.sdc2ch.prcss.eb.io.EmptyboxIO;
import com.sdc2ch.prcss.eb.vo.EmptyboxVo;
import com.sdc2ch.repo.builder.IAdmQueryBuilder;
import com.sdc2ch.repo.io.MobileAppInfoIO;
import com.sdc2ch.repo.io.TmsCarIO;
import com.sdc2ch.repo.io.TmsDriverIO;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.pubsub.I2ChEventManager;
import com.sdc2ch.require.pubsub.I2ChEventPublisher;
import com.sdc2ch.require.service.I2ChUserService;
import com.sdc2ch.service.admin.IAllocationConfirmGroupService;
import com.sdc2ch.service.admin.IAllocationConfirmService;
import com.sdc2ch.service.common.exception.ServiceException;
import com.sdc2ch.service.event.IDriverEnterFactoryEvent;
import com.sdc2ch.service.event.IDriverExitFactoryEvent;
import com.sdc2ch.service.event.IDriverFinishJobEvent;
import com.sdc2ch.service.event.IDriverStartJobEvent;
import com.sdc2ch.service.event.IMobileEvent.MobileEventType;
import com.sdc2ch.service.event.model.AppPushEvent;
import com.sdc2ch.service.event.model.ArsEventImpl;
import com.sdc2ch.service.event.model.EnterFactoryEvent;
import com.sdc2ch.service.event.model.ExitFactoryEvent;
import com.sdc2ch.service.event.model.FinishJobEvent;
import com.sdc2ch.service.event.model.StartJobEvent;
import com.sdc2ch.service.mobile.IMobileUserService;
import com.sdc2ch.service.mobile.model.ChkTblRegistScheduleVo;
import com.sdc2ch.service.mobile.model.ChkTblRegistScheduleVo.State;
import com.sdc2ch.service.mobile.model.MobileCaralcInfVo;
import com.sdc2ch.service.mobile.model.MobileCaralcInfVo.MobileCaralcInfVoBuilder;
import com.sdc2ch.service.mobile.model.MobileDeliveryInfoVo;
import com.sdc2ch.service.mobile.model.MobileHaveOffInfoHstVo;
import com.sdc2ch.service.mobile.model.MobileHaveOffInfoVo;
import com.sdc2ch.service.mobile.model.MobileHaveOffPossibleVo;
import com.sdc2ch.service.mobile.model.MobileRouteInfoVo;
import com.sdc2ch.service.util.ServiceUtils;
import com.sdc2ch.tms.enums.TransportType;
import com.sdc2ch.tms.io.TmsDayOffIO;
import com.sdc2ch.tms.io.TmsOrderStopIO;
import com.sdc2ch.tms.io.TmsPlanIO;
import com.sdc2ch.tms.service.ITmsDayOffService;
import com.sdc2ch.tms.service.ITmsPlanService;
import com.sdc2ch.tms.service.ITmsShippingService;
import com.sdc2ch.tms.service.ITmsStopService;
import com.sdc2ch.web.admin.repo.AdmQueryBuilder;
import com.sdc2ch.web.admin.repo.dao.T_EventWebioHistRepository;
import com.sdc2ch.web.admin.repo.dao.T_ReturningGoodsRepository;
import com.sdc2ch.web.admin.repo.dao.T_SnitatChckTableRepository;
import com.sdc2ch.web.admin.repo.domain.alloc.QT_SNITAT_CHCK_TABLE;
import com.sdc2ch.web.admin.repo.domain.alloc.T_CARALC_CNFIRM_GROUP2;
import com.sdc2ch.web.admin.repo.domain.alloc.T_RTNGUD_UNDTAKE;
import com.sdc2ch.web.admin.repo.domain.alloc.T_SNITAT_CHCK_TABLE;
import com.sdc2ch.web.admin.repo.domain.event.T_EVENT_WEB_IO_HIST;
import com.sdc2ch.web.admin.repo.domain.v.QV_CARALC_MSTR;
import com.sdc2ch.web.admin.repo.domain.v.QV_CARALC_PLAN;
import com.sdc2ch.web.admin.repo.domain.v.QV_DLVY_LC;
import com.sdc2ch.web.admin.repo.domain.v.QV_STATS_DRIVER_MONTHLY;
import com.sdc2ch.web.admin.repo.domain.v.V_CARALC_DTLS;
import com.sdc2ch.web.admin.repo.domain.v.V_CARALC_MSTR;
import com.sdc2ch.web.admin.repo.domain.v.V_CARALC_PLAN;
import com.sdc2ch.web.admin.repo.domain.v.V_DLVY_LC;
import com.sdc2ch.web.admin.repo.domain.v.V_STATS_DRIVER_MONTHLY;
import com.sdc2ch.web.service.IMobileAppService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MobileUserServiceImpl implements IMobileUserService {

	@Autowired AdmQueryBuilder builder;

	

	@Autowired(required = false) IShippingStateService2 shipSvc;
	@Autowired ITmsPlanService tmsPlanSvc; 
	@Autowired ITmsShippingService tmsShippingSvc; 
	@Autowired ITmsStopService tmsStopSvc; 

	@Autowired IEmptyBoxService emptySvc;
	@Autowired ITmsShippingService tmsShipSvc;
	@Autowired I2ChUserService userSvc;
	@Autowired ITmsDayOffService tmsDayOffSvc; 

	@Autowired IAllocationConfirmGroupService alcGroupSvc; 
	@Autowired IAllocationConfirmService alcSvc; 

	@Autowired T_ReturningGoodsRepository rGoodsRepo; 
	@Autowired T_SnitatChckTableRepository sanitationRepo; 
	@Autowired T_EventWebioHistRepository evtWebIoRepo; 


	@Autowired IMobileAppService appSvc;          
	
	private I2ChEventPublisher<IDriverStartJobEvent> eventStartJob;
	private I2ChEventPublisher<IDriverFinishJobEvent> eventEndJob; 
	private I2ChEventPublisher<IDriverExitFactoryEvent> eventExitFactory; 
	private I2ChEventPublisher<IDriverEnterFactoryEvent> eventEnterFactory; 

	
	private I2ChEventPublisher<IFirebaseNotificationEvent> eventPush;


	private I2ChEventPublisher<IArsEvent> eventArs; 

	@Autowired
	public void init(I2ChEventManager manager) {
		this.eventStartJob = manager.regist(IDriverStartJobEvent.class);
		this.eventEndJob = manager.regist(IDriverFinishJobEvent.class);
		this.eventArs = manager.regist(IArsEvent.class);
		this.eventExitFactory = manager.regist(IDriverExitFactoryEvent.class);
		this.eventEnterFactory = manager.regist(IDriverEnterFactoryEvent.class);
		
		this.eventPush = manager.regist(IFirebaseNotificationEvent.class);
	}

	@Override
	public MobileCaralcInfVo findAllocatedInfoByGroupId(Long id) throws ServiceException {

		List<ShippingPlanIO> ios = shipSvc.findShipPlanByAllocatedGroupId(id);

		MobileCaralcInfVoBuilder builder = MobileCaralcInfVo.builder();

		List<MobileRouteInfoVo> rotations = new ArrayList<>();

		AtomicInteger inc = new AtomicInteger();
		ios.stream().collect(Collectors.groupingBy(s -> tmsPlanSvc.convertRouteNo(s.getRouteNo()))).forEach((k, v) -> {

			List<MobileDeliveryInfoVo> infos = new ArrayList<>();


			ShippingPlanIO last = v.stream().reduce((first, second) -> second).orElse(null);
			ShippingPlanIO first = v.stream().findFirst().orElse(null);

			String confRtateRate = last.getConfRtateRate();
			String confTollCost = last.getConfTollCost();
			String confDistance = last.getConfDistance();
			String confCarOil = last.getCarOil();

			if(TransportType.FFFF != last.getTransportTy()) {
				List<ShippingPlanIO> trans = v.stream().filter(ship -> ship.getDlvyLcSeq() == 0).collect(Collectors.toList());
				confRtateRate = trans.stream().map(ShippingPlanIO::getConfRtateRate).reduce((f, s) -> nullsafeAdd(f, s)).orElse("0");
				confTollCost  = trans.stream().map(ShippingPlanIO::getConfTollCost).reduce((f, s)  -> nullsafeAdd(f, s)).orElse("0");
				confDistance  = trans.stream().map(ShippingPlanIO::getConfDistance).reduce((f, s)  -> nullsafeAdd(f, s)).orElse("0");
				confCarOil  = trans.stream().map(ShippingPlanIO::getCarOil).reduce((f, s)  -> nullsafeAdd(f, s)).orElse("0");
			}

			builder.dlvyDe(last.getDlvyDe());
			builder.id(id);
			builder.rotations(rotations);
			builder.resultCd(1);
			rotations.add(MobileRouteInfoVo.builder()
					.carTon(new BigDecimal(last.getCarWegit()))
					.dockSerialNo(StringUtils.isEmpty(last.getDockNo()) ? null : Integer.valueOf(last.getDockNo()))
					.loadingEndTime(StringUtils.isEmpty(first.getLdngEd()) ? first.getPlannedDTime() : first.getLdngEd())
					.loadingStartTime(StringUtils.isEmpty(first.getLdngSt()) ? first.getPlannedATime() : first.getLdngSt())
					.rotationNo(inc.incrementAndGet())

					.routeNo(first.getRouteNo())
					.confRtateRate(confRtateRate)
					.confTollCost(confTollCost)
					.confDistance(confDistance)
					.carOil(confCarOil)
					.time(last.getScheDlvyStDt())
					.dlvyTy(TransportType.FFFF == last.getTransportTy())
					.deliveryInfos(infos).build());

			v.forEach(v1 -> infos.add(MobileDeliveryInfoVo.builder()
					.dlvyTime(v1.getPlannedATime())
					.id(v1.getTmsPlanRowId()).name(v1.getDlvyLcNm())
					.location(new Location(100, new BigDecimal(v1.getLat() + ""), new BigDecimal(v1.getLng() + "")))
					.addr(v1.getAddr())
					.routeNo(v1.getRouteNo())
					.fctryCd(v1.getFctryTy().getCode())
					.build()));

		});
		MobileCaralcInfVo vo = builder.build();
		if(vo != null && vo.getRotations() != null) {
			try {
				vo.getRotations().sort(Comparator.nullsLast(Comparator.comparing(MobileRouteInfoVo::getTime)));
			}catch (Exception e) {
				log.info("findAllocatedInfoByGroupId() -> {}", e);
			}
		}
		return vo;
	}

	@Override
	public MobileCaralcInfVo findAllocatedInfoByGroupIdV2(Long id) throws ServiceException {
		MobileCaralcInfVoBuilder builder = MobileCaralcInfVo.builder();

		T_CARALC_CNFIRM_GROUP2  group =  alcGroupSvc.findById(id).orElse(null);
		if(group == null) {
			return builder.build();
		}
		List<MobileRouteInfoVo> rotations = new ArrayList<>();
		AtomicInteger inc = new AtomicInteger();

		builder.dlvyDe(group.getDlvyDe());
		builder.id(id);
		builder.rotations(rotations);
		builder.resultCd(1);
		List<V_CARALC_MSTR> mstr = alcSvc.findCaralcMstrByDlvyDeAndVrn(group.getDlvyDe(), group.getVrn());

		
		mstr.stream().collect(Collectors.groupingBy(s -> tmsPlanSvc.convertRouteNo(s.getRouteNo()))).forEach((k, m) -> {

			V_CARALC_MSTR last = m.stream().reduce((first, second) -> second).orElse(null);
			V_CARALC_MSTR first = m.stream().findFirst().orElse(null);

			String confRtateRate = last.getConfRtateRate();
			String confTollCost = last.getConfTollcost();
			String confDistance = last.getConfDistance();
			String confCarOil = last.getCarOil();

			if(TransportType.FFFF != tmsShippingSvc.findTransportType(last.getRouteNo())) {
				confRtateRate = m.stream().map(V_CARALC_MSTR::getConfRtateRate).reduce((f, s) -> nullsafeAdd(f, s)).orElse("0");
				confTollCost  = m.stream().map(V_CARALC_MSTR::getConfTollcost).reduce((f, s)  -> nullsafeAdd(f, s)).orElse("0");
				confDistance  = m.stream().map(V_CARALC_MSTR::getConfDistance).reduce((f, s)  -> nullsafeAdd(f, s)).orElse("0");
				confCarOil  = m.stream().map(V_CARALC_MSTR::getCarOil).reduce((f, s)  -> nullsafeAdd(f, s)).orElse("0");
			}
			List<MobileDeliveryInfoVo> infos = new ArrayList<>();

			m.stream().forEach(mst -> {
				List<V_CARALC_DTLS> dtls = 	alcSvc.findCaralcDtlsByDeliveryDateAndRouteNo(mst.getDlvyDe(), mst.getRouteNo());
				dtls.stream().forEach(d -> infos.add(this.cvtMobileDeliveryInfoVo(d, mst)));
			});

			if(infos.isEmpty()) {
				m.stream().forEach(mst -> {
					TmsOrderStopIO fctryInfo = tmsStopSvc.findStopInfoByStopCd(mst.getFctryCd());
					infos.add(0, this.cvtMobileDeliveryInfoVo(fctryInfo, mst)); 
					tmsStopSvc.findOrderStopInfoByDlvyDeAndRouteNo(mst.getDlvyDe(), mst.getRouteNo()).forEach(d -> infos.add(this.cvtMobileDeliveryInfoVo(d, mst)));
					infos.add(infos.size(), this.cvtMobileDeliveryInfoVo(fctryInfo, mst)); 
				});
			}

			rotations.add(MobileRouteInfoVo.builder()
					.carTon(new BigDecimal(last.getCarWeight()))
					.dockSerialNo(StringUtils.isEmpty(last.getDockNo()) ? null : Integer.valueOf(last.getDockNo()))
					.loadingEndTime(StringUtils.isEmpty(first.getLdngEd()) ? first.getScheEndTime() :  first.getLdngEd())
					.loadingStartTime(StringUtils.isEmpty(first.getLdngSt())? first.getScheStartTime() : first.getLdngSt())
					.rotationNo(inc.incrementAndGet())
					.routeNo(first.getRouteNo())
					.confRtateRate(confRtateRate)
					.confTollCost(confTollCost)
					.confDistance(confDistance)
					.carOil(confCarOil)
					.time(this.convertDateAndTimeString(last.getScheStartDate(), last.getScheStartTime()))
					.dlvyTy(TransportType.FFFF == tmsShippingSvc.findTransportType(last.getRouteNo())) 
					.deliveryInfos(infos).build());
		});

		MobileCaralcInfVo vo = builder.build();
		if(vo != null && vo.getRotations() != null) {
			try {
				vo.getRotations().sort(Comparator.nullsLast(Comparator.comparing(MobileRouteInfoVo::getTime)));
			}catch (Exception e) {
				log.info("findAllocatedInfoByGroupIdV2() -> {}", e);
			}
		}
		return vo;
	}

	public MobileCaralcInfVo findAllocatedInfoByGroupIdV4(Long id) throws ServiceException {


		try {

			MobileCaralcInfVoBuilder builder = MobileCaralcInfVo.builder();
			T_CARALC_CNFIRM_GROUP2  group =  alcGroupSvc.findById(id).orElse(null);
			if(group == null) {
				return builder.build();
			}


			List<EmptyboxVo> emptyboxList = currentEmptyBoxByAllicatedGroupId(id);

			List<TmsPlanIO> plans = tmsPlanSvc.findTmPlansByUserAndDeleveryDate(group.getDriverCd(), group.getDlvyDe());



			List<ShippingStateEvent> commutes = shipSvc.findCommuteTimeStateByAllocatedGId(id);


			List<EventAction> commuteActs = commutes.stream().map(s -> s.getEventAct()).collect(Collectors.toList());

			boolean start = commuteActs.contains(EventAction.USR_ST) || commuteActs.contains(EventAction.NFC_TAG_OFFIC);
			boolean finish = commuteActs.contains(EventAction.USR_FIN);


			List<MobileRouteInfoVo> rotations = new ArrayList<>();
			builder.dlvyDe(group.getDlvyDe());
			builder.id(id);
			builder.rotations(rotations);
			builder.resultCd(1);
			builder.confStartJob(start);
			builder.confEndJob(finish);

			List<ShippingStateEvent> inouts = shipSvc.findInoutStateByAllocatedGId(id);



			AtomicInteger inc = new AtomicInteger();
			
			plans.stream().filter(o -> o != null).collect(Collectors.groupingBy(s -> tmsPlanSvc.convertRouteNo(s.getRouteNo()))).forEach((k, v) -> {

				TmsPlanIO last = v.stream().reduce((first, second) -> second).orElse(null);
				TmsPlanIO first = v.stream().findFirst().orElse(null);

				List<EventAction> _actions = inouts.stream().filter(i -> i.getRouteNo().equals(last.getRouteNo())).map(s -> s.getEventAct()).collect(Collectors.toList());

				String confRtateRate = last.getConfRtateRate();
				String confTollCost = last.getConfTollCost();
				String confDistance = last.getConfDistance();
				String confCarOil = last.getCarOil();

				if(TransportType.FFFF != tmsShippingSvc.findTransportType(last.getRouteNo())) {	
					List<TmsPlanIO> trans = v.stream().filter(
							p -> p.getStopSeq() == 0)
							.collect(Collectors.toList());
					confRtateRate = trans.stream().map(TmsPlanIO::getConfRtateRate).reduce((f, s) -> nullsafeAdd(f, s)).orElse("0");
					confTollCost  = trans.stream().map(TmsPlanIO::getConfTollCost).reduce((f, s)  -> nullsafeAdd(f, s)).orElse("0");
					confDistance  = trans.stream().map(TmsPlanIO::getConfDistance).reduce((f, s)  -> nullsafeAdd(f, s)).orElse("0");
					confCarOil  = trans.stream().map(TmsPlanIO::getCarOil).reduce((f, s)  -> nullsafeAdd(f, s)).orElse("0");

				}
				List<MobileDeliveryInfoVo> infos = new ArrayList<>();
				
				rotations.add(MobileRouteInfoVo.builder()
						.carTon(new BigDecimal(last.getCarWegit()))
						.dockSerialNo(StringUtils.isEmpty(last.getDockNo()) ? null : Integer.valueOf(last.getDockNo()))
						.loadingEndTime(StringUtils.isEmpty(first.getLdngEd()) ? first.getScheDlvyEdTime() : first.getLdngEd())
						.loadingStartTime(StringUtils.isEmpty(first.getLdngSt()) ? first.getScheDlvyStTime(): first.getLdngSt())
						.rotationNo(inc.incrementAndGet())

						.routeNo(first.getRouteNo())
						.confRtateRate(confRtateRate)
						.confTollCost(confTollCost)
						.confDistance(confDistance)
						.carOil(confCarOil)
						.time(this.convertDateAndTimeString(last.getScheDlvyStDe(), last.getScheDlvyStTime()))
						.dlvyTy(TransportType.FFFF == tmsShippingSvc.findTransportType(last.getRouteNo()))
						.confFctryArrive(_actions.contains(EventAction.USR_ENTER))
						.confFctryDepart(_actions.contains(EventAction.USR_EXITED))
						.deliveryInfos(infos).build());

				
				v.forEach(v1 -> {

					List<EventAction> egoInouts = null;
					if(TransportType.FFFF != tmsShippingSvc.findTransportType(last.getRouteNo())) {
						egoInouts = inouts.stream().filter(i -> i.getRouteNo().equals(v1.getRouteNo())).map(s -> s.getEventAct()).collect(Collectors.toList());
					}

					infos.add(MobileDeliveryInfoVo.builder()
							.dlvyTime(tmsShippingSvc.getPlanedArriveTime(v1))
							.id(v1.getId())
							.name(v1.getDlvyLoNm())
							.location(new Location(100, new BigDecimal(nullSafeDouble(v1.getLat())), new BigDecimal(nullSafeDouble(v1.getLng()))))
							.addr(v1.getAddr())
							.routeNo(v1.getRouteNo())
							.fctryCd(v1.getFctryCd())
							.confFctryArrive(egoInouts == null ? false : egoInouts.contains(EventAction.USR_ENTER))
							.confFctryDepart(egoInouts == null ? false : egoInouts.contains(EventAction.USR_EXITED))
							.emptyboxInfo(
									emptyboxList.stream()
									.filter(o-> v1.getRouteNo().equals(o.getRouteNo())&& v1.getStopCd().equals(o.getStopCd()))
									.findFirst()
									.orElse(null)
									)
							.build());

				});
			});

			
			MobileCaralcInfVo vo = builder.build();
			if(vo != null && vo.getRotations() != null) {
				try {
					vo.getRotations().sort(Comparator.nullsLast(Comparator.comparing(MobileRouteInfoVo::getTime)));
				}catch (Exception e) {
					log.info("findAllocatedInfoByGroupIdV4() -> {}", e);
				}
			}
			return vo;
		}catch (Exception e) {
			log.error("findAllocatedInfoByGroupIdV4() -> {}", e);
			return findAllocatedInfoByGroupIdV3(id);
		}
	}


	@Override
	public MobileCaralcInfVo findAllocatedInfoByGroupIdV3(Long id) throws ServiceException {
		MobileCaralcInfVoBuilder builder = MobileCaralcInfVo.builder();
		T_CARALC_CNFIRM_GROUP2  group =  alcGroupSvc.findById(id).orElse(null);
		if(group == null) {
			return builder.build();
		}


		List<EmptyboxVo> emptyboxList = currentEmptyBoxByAllicatedGroupId(id);

		List<V_CARALC_PLAN> plans = new ArrayList<>();


		List<V_CARALC_PLAN> _planList = alcSvc.findCaralcPlanByDlvyDeAndVrn(group.getDlvyDe(), group.getVrn());
		plans.addAll(_planList);
		Set<String> planRouteNoList = _planList.stream().filter(o -> o != null).map(V_CARALC_PLAN::getRouteNo).collect(Collectors.toSet());




		List<V_CARALC_MSTR> mstrs = alcSvc.findCaralcMstrByDlvyDeAndVrn(group.getDlvyDe(), group.getVrn());
		Set<String> mstrRouteNoList = mstrs.stream().filter(o -> o != null).map(V_CARALC_MSTR::getRouteNo).collect(Collectors.toSet());


		mstrRouteNoList.removeAll(planRouteNoList);

		
		mstrRouteNoList.stream().forEach(routeNo -> {
			V_CARALC_MSTR mstr = mstrs.stream().filter(o -> o.getRouteNo() == routeNo).findFirst().orElse(null);
			if (mstr != null) {
				plans.addAll(makeCaralcPlanList(mstr));
			}
		});

		List<MobileRouteInfoVo> rotations = new ArrayList<>();
		builder.dlvyDe(group.getDlvyDe());
		builder.id(id);
		builder.rotations(rotations);
		builder.resultCd(1);




		AtomicInteger inc = new AtomicInteger();
		
		plans.stream().filter(o -> o != null).collect(Collectors.groupingBy(s -> tmsPlanSvc.convertRouteNo(s.getRouteNo()))).forEach((k, v) -> {

			V_CARALC_PLAN last = v.stream().reduce((first, second) -> second).orElse(null);
			V_CARALC_PLAN first = v.stream().findFirst().orElse(null);

			String confRtateRate = last.getConfRtateRate();
			String confTollCost = last.getConfTollcost();
			String confDistance = last.getConfDistance();
			String confCarOil = last.getCarOil();

			if(TransportType.FFFF != tmsShippingSvc.findTransportType(last.getRouteNo())) {	
				List<V_CARALC_PLAN> trans = v.stream().filter(
						plan ->	"0".equals(plan.getDlvySeq()))
						.collect(Collectors.toList());
				confRtateRate = trans.stream().map(V_CARALC_PLAN::getConfRtateRate).reduce((f, s) -> nullsafeAdd(f, s)).orElse("0");
				confTollCost  = trans.stream().map(V_CARALC_PLAN::getConfTollcost).reduce((f, s)  -> nullsafeAdd(f, s)).orElse("0");
				confDistance  = trans.stream().map(V_CARALC_PLAN::getConfDistance).reduce((f, s)  -> nullsafeAdd(f, s)).orElse("0");
				confCarOil  = trans.stream().map(V_CARALC_PLAN::getCarOil).reduce((f, s)  -> nullsafeAdd(f, s)).orElse("0");
			}
			List<MobileDeliveryInfoVo> infos = new ArrayList<>();
			
			rotations.add(MobileRouteInfoVo.builder()
					.carTon(new BigDecimal(last.getCarWeight()))
					.dockSerialNo(StringUtils.isEmpty(last.getDockNo()) ? null : Integer.valueOf(last.getDockNo()))
					.loadingEndTime(StringUtils.isEmpty(first.getLdngEd()) ? first.getScheEndTime() : first.getLdngEd())
					.loadingStartTime(StringUtils.isEmpty(first.getLdngSt()) ? first.getScheStartTime(): first.getLdngSt())
					.rotationNo(inc.incrementAndGet())

					.routeNo(first.getRouteNo())
					.confRtateRate(confRtateRate)
					.confTollCost(confTollCost)
					.confDistance(confDistance)
					.carOil(confCarOil)
					.time(this.convertDateAndTimeString(last.getScheStartDate(), last.getScheStartTime()))
					.dlvyTy(TransportType.FFFF == tmsShippingSvc.findTransportType(last.getRouteNo()))
					.deliveryInfos(infos).build());

			
			v.forEach(v1 -> infos.add(MobileDeliveryInfoVo.builder()
					.dlvyTime(getPlanedArriveTime(v1))
					.id(v1.getId())
					.name(v1.getDlvyLcNm())
					.location(new Location(100, new BigDecimal(nullSafeDouble(v1.getLatitude())), new BigDecimal(nullSafeDouble(v1.getLongitude()))))
					.addr(v1.getAddrBasic())
					.routeNo(v1.getRouteNo())
					.fctryCd(v1.getFctryCd())
					.emptyboxInfo(
							emptyboxList.stream()
							.filter(o-> v1.getRouteNo().equals(o.getRouteNo())&& v1.getStopCd().equals(o.getStopCd()))
							.findFirst()
							.orElse(null)
							)
					.build()));
		});

		
		MobileCaralcInfVo vo = builder.build();
		if(vo != null && vo.getRotations() != null) {
			try {
				vo.getRotations().sort(Comparator.nullsLast(Comparator.comparing(MobileRouteInfoVo::getTime)));
			}catch (Exception e) {
				log.info("findAllocatedInfoByGroupIdV3() -> {}", e);
			}
		}
		return vo;
	}

	private List<V_CARALC_PLAN> makeCaralcPlanList(V_CARALC_MSTR mst){
		List<V_CARALC_PLAN> list = new ArrayList<V_CARALC_PLAN>();
		AtomicInteger inc = new AtomicInteger();
		TmsOrderStopIO fctryInfo = tmsStopSvc.findStopInfoByStopCd(mst.getFctryCd());
		list.add(0, this.makeCarAlcPlan(fctryInfo, mst, inc.getAndIncrement())); 
		tmsStopSvc.findOrderStopInfoByDlvyDeAndRouteNo(mst.getDlvyDe(), mst.getRouteNo()).forEach(d -> list.add(this.makeCarAlcPlan(d, mst, inc.getAndIncrement())));
		list.add(list.size(), this.makeCarAlcPlan(fctryInfo, mst, inc.getAndIncrement())); 
		return list;
	}

	private V_CARALC_PLAN makeCarAlcPlan(TmsOrderStopIO d, V_CARALC_MSTR m, Integer dlvySeq) {
		V_CARALC_PLAN plan = new V_CARALC_PLAN();
		BeanUtils.copyProperties(m, plan);
		plan.setStopCd(d.getShipTo());
		plan.setStopNm(d.getStopNm());
		plan.setDlvyLcNm(d.getStopNm());
		plan.setDlvySeq(String.valueOf(dlvySeq));
		return plan;
	}



	private String getPlanedArriveTime(V_CARALC_DTLS d, V_CARALC_MSTR m) {
		return tmsShippingSvc.getPlanedArriveTime(d.getDeliveryDate(), d.getStopCd(), d.getStopType(), d.getRouteNo(), d.getStopSeq(), m.getLdngSt(), d.getDepartEtime(), d.getArriveEtime());
	}

	private String getPlanedArriveTime(V_CARALC_PLAN plan) {
		return tmsShippingSvc.getPlanedArriveTime(plan.getDlvyDe(), plan.getStopCd(), plan.getStopType(), plan.getRouteNo(), Integer.valueOf(StringUtils.isEmpty(plan.getStopSeq())? "0" :plan.getStopSeq()), plan.getLdngSt(), plan.getDepartEtime(), plan.getArriveEtime());
	}

	private MobileDeliveryInfoVo cvtMobileDeliveryInfoVo(TmsOrderStopIO d, V_CARALC_MSTR m) {
		return MobileDeliveryInfoVo.builder()
				.dlvyTime(null)
				.id(null)
				.name(d.getStopNm())
				.location(new Location(100, new BigDecimal(nullSafeDouble(d.getLat())), new BigDecimal(nullSafeDouble(d.getLng()))))
				.addr(d.getAddr())
				.routeNo(m.getRouteNo())
				.fctryCd(m.getFctryCd())
				.build();
	}

	private MobileDeliveryInfoVo cvtMobileDeliveryInfoVo(V_CARALC_DTLS d, V_CARALC_MSTR m) {
		return MobileDeliveryInfoVo.builder()
				.dlvyTime(getPlanedArriveTime(d, m))
				.id(d.getId())
				.name(d.getDlvyLcNm())
				.location(new Location(100, new BigDecimal(nullSafeDouble(d.getLat())), new BigDecimal(nullSafeDouble(d.getLng()))))
				.addr(d.getAddrBasic())
				.routeNo(d.getRouteNo())
				.fctryCd(d.getCenterCd())
				.build();
	}

	private double nullSafeDouble(Double d) {
		return d != null ? d:0;
	}

	private double nullSafeDouble(String s) {
		return !StringUtils.isEmpty(s) ? Double.valueOf(s):0;
	}

	private LocalDateTime convertDateAndTimeString(String de, String time) {
		if(StringUtils.isEmpty(de) || StringUtils.isEmpty(time))
			return null;
		return LocalDateTime.parse(de + time, DateTimeFormatter.ofPattern("yyyyMMddHH:mm"));
	}




	private String nullsafeAdd(String f, String s) {
		BigDecimal first = StringUtils.isEmpty(f) ? BigDecimal.ZERO : new BigDecimal(f);
		BigDecimal last  = StringUtils.isEmpty(s) ? BigDecimal.ZERO : new BigDecimal(s);
		return first.add(last).toString();
	}

	@Override
	public MobileCaralcInfVo findAllocatedInfoByUser(IUser user) throws ServiceException {
		return findAllocatedInfoByUserAndDlvyDe(user, null);
	}

	@Override
	public MobileCaralcInfVo findAllocatedInfoByUserV2(IUser user) throws ServiceException {
		return findAllocatedInfoByUserAndDlvyDeV2(user, null);
	}
	@Override
	public MobileCaralcInfVo findAllocatedInfoByUserV3(IUser user) throws ServiceException {
		return findAllocatedInfoByUserAndDlvyDeV3(user, null);
	}
	@Override
	public MobileCaralcInfVo findAllocatedInfoByUserV4(IUser user) throws ServiceException {
		return findAllocatedInfoByUserAndDlvyDeV4(user, null);
	}

	@Override
	public MobileCaralcInfVo findAllocatedInfoByUserAndDlvyDe(IUser user, String dlvyDe) throws ServiceException {
		String _dlvyDe = dlvyDe == null ? tmsShipSvc.getTmsDeliveryRuleDate() : dlvyDe;
		T_CARALC_CNFIRM_GROUP2 group = alcGroupSvc.findDlvyByUser(user, _dlvyDe).orElse(null);
		return group == null ? null : findAllocatedInfoByGroupId(group.getId());
	}




	@Override
	public MobileCaralcInfVo findAllocatedInfoByUserAndDlvyDeV2(IUser user, String dlvyDe) throws ServiceException {
		String _dlvyDe = dlvyDe == null ? tmsShipSvc.getTmsDeliveryRuleDate() : dlvyDe;
		T_CARALC_CNFIRM_GROUP2 group = alcGroupSvc.findDlvyByUser(user, _dlvyDe).orElse(null);
		return group == null ? null : findAllocatedInfoByGroupIdV2(group.getId());
	}

	@Override
	public MobileCaralcInfVo findAllocatedInfoByUserAndDlvyDeV3(IUser user, String dlvyDe) throws ServiceException {
		String _dlvyDe = dlvyDe == null ? tmsShipSvc.getTmsDeliveryRuleDate() : dlvyDe;
		T_CARALC_CNFIRM_GROUP2 group = alcGroupSvc.findDlvyByUser(user, _dlvyDe).orElse(null);
		
		return group == null || group.getTrnsmisDt() == null? null : findAllocatedInfoByGroupIdV3(group.getId());
	}


	@Override
	public List<EmptyboxVo> listEmptyBox(String routeNo, String dlvyDe) {

		if(StringUtils.isEmpty(routeNo)) {
			return Collections.emptyList();
		}
		return emptySvc.findEmptyBoxByRoute(dlvyDe, routeNo);
	}

	@Override
	public boolean isConfirm(EmptyboxIO eb) {
		return emptySvc.isConfirm(eb);
	}

	@Override
	public EmptyboxIO saveEmptyBox(EmptyboxIO eb) {
		return emptySvc.save(eb);
	}

	@Override
	public EmptyboxIO findEmptyBoxById(Long id) throws ServiceException {
		return emptySvc.findEmptyBoxById(id).orElseThrow(() -> idNotfound(id));
	}

	@Override
	public Optional<T_RTNGUD_UNDTAKE> findReturningGoodsById(Long id){
		return rGoodsRepo.findById(id);
	}

	@Override
	public T_RTNGUD_UNDTAKE saveReturningGoods(T_RTNGUD_UNDTAKE returnGoods) {
		return rGoodsRepo.save(returnGoods);
	}


	@Override
	public boolean checkEmptyBox(IUser user) throws ServiceException {
		Optional<T_CARALC_CNFIRM_GROUP2> groupOtp = alcGroupSvc.findLastConfirmGroupByUser(user);
		return isCompletedBoxProcess(groupOtp);
	}

	private boolean isCompletedBoxProcess(Optional<T_CARALC_CNFIRM_GROUP2> groupOtp) throws ServiceException {
		T_CARALC_CNFIRM_GROUP2 group = groupOtp
				.orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND, "배송이 없습니다."));
		return emptySvc.isCompletedBoxProcess(group.getDlvyDe(),
				group.getRoutes().stream().map(r -> r.getRouteNo()).toArray(String[]::new));
	}

	private boolean isCompletedBoxProcessByErp(Optional<T_CARALC_CNFIRM_GROUP2> groupOtp) throws ServiceException {
		T_CARALC_CNFIRM_GROUP2 group = groupOtp
				.orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND, "배송이 없습니다."));
		return emptySvc.isCompletedBoxProcessByErp(group.getDlvyDe(),
				group.getRoutes().stream().map(r -> r.getRouteNo()).toArray(String[]::new));
	}

	@Override
	public boolean checkEmptyBoxByAllocatedGroupId(Long id) throws ServiceException {
		
		return isCompletedBoxProcess(alcGroupSvc.findById(id));

	}

	@Override
	public Optional<T_CARALC_CNFIRM_GROUP2> findAllocatedGroupById(Long id) {
		return alcGroupSvc.findById(id);
	}

	@Override
	public Optional<T_CARALC_CNFIRM_GROUP2> findLastAllocatedGroupByUser(IUser user) {
		return alcGroupSvc.findLastByUser(user);
	}

	@Override
	public void saveAllocatedGroup(T_CARALC_CNFIRM_GROUP2 group) {
		alcGroupSvc.save(group);
	}

	@Override
	public void startJob(IUser user, Long allocatedGroupId) {
		eventStartJob.fireEvent(StartJobEvent.of(user, new Date(), allocatedGroupId, MobileEventType.START_JOB));
	}

	@Override
	public ShipStateVo2 currentShippingState(IUser user) {
		return shipSvc.getDeliveryState2(user);
	}

	@Override
	public boolean finishJob(IUser user, Long allocatedGroupId) {


		log.info("finishJob => userNm={}, allocatedGroupId={}, ", user.getUsername(), allocatedGroupId);
		eventEndJob.fireEvent(FinishJobEvent.of(user, new Date(), allocatedGroupId, MobileEventType.FINISH_JOB));
		return true;
	}

	@Override
	public List<EmptyboxVo> currentEmptyBoxByAllicatedGroupId(Long id) {
		T_CARALC_CNFIRM_GROUP2 group = alcGroupSvc.findById(id).orElse(null);
		
		return group == null ? new ArrayList<>()
				: emptySvc.findEmptyBoxByRouteOnly(group.getDlvyDe(), true,	
						group.getRoutes().stream().map(r -> r.getRouteNo()).toArray(String[]::new));
	}
	@Override
	public int squareBoxQty(String dlvyDe, String vrn) {
		QV_CARALC_MSTR mstr = QV_CARALC_MSTR.v_CARALC_MSTR;
		List<V_CARALC_MSTR> mstrs = builder.create().selectFrom(mstr).where(mstr.dlvyDe.eq(dlvyDe).and(mstr.vrn.eq(vrn))).fetch();
		if(mstrs != null) {
			return mstrs.stream().map(m -> m.getShipQty()).reduce((a, b) -> a + b).orElse(0);
		}
		return 0;
	}
	@Override
	public int squareBoxQty(String dlvyDe, String vrn, String routeNo) {

		if(StringUtils.isEmpty(routeNo)) {
			return squareBoxQty(dlvyDe, vrn);
		}
		QV_CARALC_MSTR mstr = QV_CARALC_MSTR.v_CARALC_MSTR;
		List<V_CARALC_MSTR> mstrs = builder.create().selectFrom(mstr).where(mstr.dlvyDe.eq(dlvyDe).and(mstr.vrn.eq(vrn)).and(mstr.routeNo.eq(routeNo))).fetch();
		if(mstrs != null) {
			return mstrs.stream().map(m -> m.getShipQty()).reduce((a, b) -> a + b).orElse(0);
		}
		return 0;
	}

	private ServiceException idNotfound(Object id) {
		return new ServiceException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND, "no Search Elements : " + id+" not found");
	}




















	
	@Override
	@Transactional
	public T_SNITAT_CHCK_TABLE saveSnitation(T_SNITAT_CHCK_TABLE snitationInfo) {
		sanitationRepo.save(snitationInfo);
		return snitationInfo;
	}


	
	@Override
	public V_STATS_DRIVER_MONTHLY findDriveMonthly(String dlvyDe, String vrn) {
		log.debug("dlvyDe= {}, vrn= {}", dlvyDe, vrn);
		QV_STATS_DRIVER_MONTHLY view = QV_STATS_DRIVER_MONTHLY.v_STATS_DRIVER_MONTHLY;

		V_STATS_DRIVER_MONTHLY result = builder.create()
				.select( Projections.fields(V_STATS_DRIVER_MONTHLY.class,

						view.settleMonth, view.vrn, view.dstnc, view.confOffday, view.workday,
						view.rtateRate, view.basicCost, view.oilCost, view.sboxCost, view.tboxCost,
						view.yboxCost, view.tranboxCost, view.otherCost, view.updownBoxCost, view.totalCost





				) )
				.from(view)
				.where(
					view.settleMonth.eq(dlvyDe)
					.and(view.vrn.eq(vrn))
				)
				.fetchOne();
		return result;

	}


	
	@Autowired IAdmQueryBuilder builderProc;
	public void saveStoredProcedureCall(Object[] params, String procName) {
		builderProc.storedProcedureCall(procName, params);
	}

	
	public List<Object[]> selectStoredProcedureCall(Object[] params, String procName) {
		return builderProc.storedProcedureResultCall(procName, params);
	}

	
	public List<Object[]> saveStoredProcedureCallRet(Object[] params, String procName) {
		return builderProc.storedProcedureCallRet(procName, params);
	}


	public List<ChkTblRegistScheduleVo> findMyCheckList(IUser user, LocalDate toDay) {

		String vrn = ((TmsDriverIO) user.getUserDetails()).getCar().getVrn();
		LocalDate endOfMonth = toDay.with(TemporalAdjusters.lastDayOfMonth());
		LocalDate startOfMonth = toDay.with(TemporalAdjusters.firstDayOfMonth());

		QT_SNITAT_CHCK_TABLE tbl = QT_SNITAT_CHCK_TABLE.t_SNITAT_CHCK_TABLE;
		List<T_SNITAT_CHCK_TABLE> tbls = builder.create().selectFrom(tbl)
				.where(tbl.vrn.eq(vrn).and(tbl.regDe.between(dateFmt(startOfMonth), dateFmt(endOfMonth)))).fetch();

		List<ChkTblRegistScheduleVo> vos = IntStream.range(0, endOfMonth.getDayOfMonth()).mapToObj(i -> {
			LocalDate tomorrow = startOfMonth.plusDays(i);
			return ChkTblRegistScheduleVo.builder().dayOfWeek(tomorrow.getDayOfWeek()).regDe(tomorrow)
					.state(State.NOTYET).build();
		}).collect(Collectors.toList());

		return leftjoin(vos, tbls);

	}

	private List<ChkTblRegistScheduleVo> leftjoin(List<ChkTblRegistScheduleVo> vos, List<T_SNITAT_CHCK_TABLE> tbls){
		return Seq.seq(vos)
		.flatMap(v1 -> Seq.seq(tbls)
				.filter(v2 -> filter(v1, v2))
				.onEmpty(null)
				.map(v2 -> tuple(v1, v2))
				.map(t -> convert(t.v1, t.v2))
				).collect(Collectors.toList());
	}

	private boolean filter(ChkTblRegistScheduleVo v1, T_SNITAT_CHCK_TABLE v2) {
		LocalDate date = LocalDate.parse(v2.getRegDe(), DateTimeFormatter.ofPattern("yyyyMMdd"));
		return v1.getRegDe().isEqual(date);
	}

	private ChkTblRegistScheduleVo convert(ChkTblRegistScheduleVo v1, T_SNITAT_CHCK_TABLE v2) {
		if(v2 == null)
			return v1;
		return v1.addState(State.COMPLETE);
	}

	private String dateFmt(LocalDate date) {
		return date.toString().replaceAll("-", "");
	}

	
	public void arsCall(CallType type, Long id, String dlvyDe, String dlvyLcCd, String fctryCd, IUser user, String routeNo) {
		arsCall(SenderType.CUSTOMER, type, id, dlvyDe, dlvyLcCd, fctryCd, user, routeNo);
	}

	@Override
	public void arsCall(SenderType senderType, CallType type, Long id, String dlvyDe, String dlvyLcCd, String fctryCd,
			IUser user, String routeNo) {
		log.info("## ARS ## arsCall(), senderType={}, type={}, dlvyDe={}, dlvyLcCd={}, fctryCd={}, routeNo={}, user={}, id={} ", senderType, type, dlvyDe, dlvyLcCd, fctryCd, routeNo, user.getUsername(), id);
		switch(senderType) {
		case CUSTOMER:
			customerArsCall(senderType, type, dlvyDe, dlvyLcCd, fctryCd, routeNo, user);
			break;
		case DRIVER:
			driverArsCall(senderType, type, dlvyDe, dlvyLcCd, fctryCd, routeNo, user, id);
			break;
		case FFFF:
			break;
		default:
			break;

		}
	}

	private void driverArsCall(SenderType senderType, CallType type, String dlvyDe, String dlvyLcCd, String fctryCd,
			String routeNo, IUser user, Long id) {
		String driverMobile = null;
		String driverNm = null;
		String dlvyLcNm = null;
		V_CARALC_PLAN plan= null;
		QV_CARALC_PLAN qplan =QV_CARALC_PLAN.v_CARALC_PLAN;

		Set<String> mdns = new HashSet<>();
		plan = builder.create().selectFrom(qplan).where(qplan.id.eq(id)).fetchOne();
		
		log.info("## ARS ## driverArsCall(); dlvyDe={}, routeNo={}, plansIsNull={}, dlvyLcCd={}", dlvyDe, routeNo, (plan==null), dlvyLcCd );

		if(plan != null) {
			String mdn1 = plan.getCsMobileNo();
			String mdn2 = plan.getCMobileNo();
			dlvyLcNm = plan.getDlvyLcNm();

			if(!StringUtils.isEmpty(mdn1)) {
				mdns.add(mdn1);
			}
			if(!StringUtils.isEmpty(mdn2)) {
				mdns.add(mdn2);
			}
			driverMobile = plan.getMobileNo();
		}
		if(plan != null) {
			fctryCd = plan.getFctryCd();
			dlvyLcNm = plan.getDlvyLcNm();
		}
		ArsEventImpl event = new ArsEventImpl();
		event.setUser(user);
		event.setCallType(type);
		event.setCommandName("ROUTE");
		event.setSenderType(senderType);
		event.setRouteNo(routeNo);
		event.setFctryCd(fctryCd);
		event.setDriverMobile(driverMobile);
		event.setCallers(mdns);
		event.setDriverNm(driverNm);
		event.setDlvyLcNm(dlvyLcNm);
		event.setDlvyLcMobile(mdns.stream().findFirst().orElse(null));
		eventArs.fireEvent(event);

	}

	private void customerArsCall(SenderType senderType, CallType type, String dlvyDe, String dlvyLcCd, String fctryCd,
			String routeNo, IUser user) {

		String driverMobile = null;
		String driverNm = null;
		String dlvyLcNm = null;
		TmsPlanIO plan= null;


		Set<String> mdns = new HashSet<>();

		if(!StringUtils.isEmpty(dlvyLcCd)) {
			dlvyLcCd = dlvyLcCd.length() == 5 ? dlvyLcCd + "000" : dlvyLcCd;
		}

		QV_DLVY_LC QdlvyLc = QV_DLVY_LC.v_DLVY_LC;
		V_DLVY_LC dlvyLc = builder.create().selectFrom(QdlvyLc).where(QdlvyLc.dlvyLcCd.eq(dlvyLcCd)).fetchOne();

		if(dlvyLc != null) {
			fctryCd = dlvyLc.getFctryCd();
			dlvyLcNm = dlvyLc.getDlvyLcNm();
			String mdn1 = dlvyLc.getCsMobileNo();
			String mdn2 = dlvyLc.getCMobileNo();
			if(!StringUtils.isEmpty(mdn1)) {
				mdns.add(mdn1);
			}
			if(!StringUtils.isEmpty(mdn2)) {
				mdns.add(mdn2);
			}
		}

		switch (type) {
		case DRIVER:

			if(StringUtils.isEmpty(routeNo)) {
				routeNo = "9999999999";
			}

			
			List<TmsPlanIO> plans = tmsPlanSvc.findTmPlansByIds(dlvyDe, routeNo);

			log.info("## ARS ## customerArsCall(); dlvyDe={}, routeNo={}, plansIsNull={}, dlvyLcCd={}", dlvyDe, routeNo, (plans==null), dlvyLcCd );
			final String _dlvyLcCd = dlvyLcCd;

			
			plan = plans.stream().filter(p->p.getStopCd().equals(_dlvyLcCd)).findFirst().orElse(null);

			
			if(plan == null) {
				String _dlvyLcCd5 = dlvyLcCd.length() < 5 ? dlvyLcCd : dlvyLcCd.substring(0, 5);
				plan = plans.stream().filter(p->p.getStopCd().equals(_dlvyLcCd5)).findFirst().orElse(null);
			}

			
			
			if(plan == null) {
				String _dlvyLcCd5 = dlvyLcCd.length() < 5 ? dlvyLcCd : dlvyLcCd.substring(0, 5);
				plan = plans.stream().filter(p->{
					String pStopCd5 = (p.getStopCd().length() < 5 ? p.getStopCd() : p.getStopCd().substring(0, 5));
						return pStopCd5.equals(_dlvyLcCd5);
				}).findFirst().orElse(null);
			}

			if(plan != null) {
				driverMobile = plan.getMobileNo();
				driverNm = plan.getDriverNm();
			}

			log.info("## ARS ## customerArsCall() planIsNull={}, driverMobile={}, driverNm={}", (plan == null), driverMobile, driverNm);
			break;
		case FACTORY:
			break;
		default:
			break;
		}

		ArsEventImpl event = new ArsEventImpl();
		event.setUser(user);
		event.setCallType(type);
		event.setCommandName("ROUTE");
		event.setSenderType(senderType);
		event.setRouteNo(routeNo);
		event.setFctryCd(fctryCd);
		event.setDriverMobile(driverMobile);
		event.setCallers(mdns);
		event.setDriverNm(driverNm);
		event.setDlvyLcNm(dlvyLcNm);
		event.setDlvyDe(dlvyDe);
		eventArs.fireEvent(event);
	}

	@Override
	public void arsCall(CallType type, Long id, String dlvyDe, String dlvyLcCd, String fctryCd, IUser user) {
		arsCall(type, id, dlvyDe, dlvyLcCd, fctryCd, user, null);
	}

	@Override
	public Optional<T_CARALC_CNFIRM_GROUP2> findLastConfirmGroupByUser(IUser user) {
		return alcGroupSvc.findLastConfirmGroupByUser(user);
	}








	@Override
	public List<MobileHaveOffInfoVo> upsertDayoff(Integer id, String fctryCd, String startDate, String endDate,
			String carCd, String type, String reason, String unit, String bigo, String routeNo, String username) {
		LocalDate now = LocalDate.now();
		LocalDate stDt = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
		LocalDate nextMonth = LocalDate.now().plusMonths(1l);


		
		if(now.isEqual(stDt)) {
			
			return Stream.of(new MobileHaveOffInfoVo()).map(m -> {
				m.setRetCd(-1);
				m.setRetMessage( "금일 휴무 신청은 접수되지 않습니다. 급한 용무가 있으시면 관리자에게 문의하시길 바랍니다." );
				return m;
			}).collect(Collectors.toList());
		} else if(stDt.isBefore(now)) {
			
			return Stream.of(new MobileHaveOffInfoVo()).map(m -> {
				m.setRetCd(-1);
				m.setRetMessage( "금일 이전 날짜의 휴무신청은 접수되지 않습니다." );
				return m;
			}).collect(Collectors.toList());

		
		} else if(stDt.isAfter(nextMonth)) {
			return Stream.of(new MobileHaveOffInfoVo()).map(m -> {
				m.setRetCd(-1);
				m.setRetMessage( "한달 이내의 날짜만 신청이 가능합니다." );
				return m;
			}).collect(Collectors.toList());

		} else if(stDt.isAfter(now)) {
			if(now.plusDays(1).isEqual(stDt) && LocalDateTime.now().getHour() >= 12) {
				return Stream.of(new MobileHaveOffInfoVo()).map(m -> {
					m.setRetCd(-1);
					m.setRetMessage( "익일 휴무 신청은 금일 오후 12시 이전까지만 가능 합니다. 급한 용무가 있으시면 관리자에게 문의하시길 바랍니다." );
					return m;
				}).collect(Collectors.toList());
			}
		}


		
		if("유급".equals(type) && !tmsDayOffSvc.validOverPaidDayoff(fctryCd, carCd, startDate, startDate, Double.valueOf(unit))) {
			return Stream.of(new MobileHaveOffInfoVo()).map(m -> {
				m.setRetCd(-1);
				m.setRetMessage("사용 가능한 유급휴가를 초과하였습니다.");
				return m;
			}).collect(Collectors.toList());
		}

		
		if(id == 0) {
			if(!this.validDayoffSch(fctryCd, carCd, startDate)) {
				return Stream.of(new MobileHaveOffInfoVo()).map(m -> {
					m.setRetCd(-1);
					m.setRetMessage( "이미 등록된 휴가가 있습니다." );
					return m;
				}).collect(Collectors.toList());
			}
		}else {
			
			TmsDayOffIO dayoff = tmsDayOffSvc.selectOnebyId(String.valueOf(id));
			log.info("{}, {}, {}", dayoff.getId(), dayoff.getCarCd(), dayoff.getDayoffDate());
			LocalDate oldStDt = LocalDate.parse(dayoff.getDayoffDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));
			if(oldStDt.isBefore(now)) {
				return Stream.of(new MobileHaveOffInfoVo()).map(m -> {
					m.setRetCd(-1);
					m.setRetMessage( "금일 이전 휴가신청 내역 변경할 수 없습니다." );
					return m;
				}).collect(Collectors.toList());
			}


		}

		
		if (!tmsDayOffSvc.validLimitSchV2(fctryCd, carCd, startDate, unit)) {
			return Stream.of(new MobileHaveOffInfoVo()).map(m -> {
				m.setRetCd(-1);

				m.setRetMessage("신청일의 휴무신청 허용 가능일을 초과하였습니다.");
				return m;
			}).collect(Collectors.toList());
		}

		Object[] params = {

				id, fctryCd, startDate, endDate,
				carCd, type, reason, unit, bigo,  routeNo, username
		};
		List<Object[]> rows = new ArrayList<>();
		List<MobileHaveOffInfoVo> result = new ArrayList<>(rows.size());
		String procName = "[dbo].[SP_2CH_DAYOFF_REG]";
		
		System.out.println("************************");
		for (Object p : params) {
			System.out.println("[" + p + "]");
		}
		System.out.println("************************");
		rows = this.saveStoredProcedureCallRet(params, procName);

		if (rows.size()>0) {
			for (Object[] row : rows) {
				MobileHaveOffInfoVo info = new MobileHaveOffInfoVo();
				info.setRetCd((Integer) row[0]);
				info.setRetMessage((String) row[1]);
				result.add(info);
			}
		}
		return result;
	}

	@Override
	public List<MobileHaveOffPossibleVo> selectListHaveOffPossible(String fctryCd, String searchMonth, String carCd) {

		int limitCnt = tmsDayOffSvc.getDayoffLimitCnt(fctryCd, carCd);
		List<MobileHaveOffPossibleVo> voResult = new ArrayList<>();
		Optional.ofNullable(tmsDayOffSvc.getDayoffCountV2(fctryCd, carCd, searchMonth)).ifPresent(lst -> {
		voResult.addAll(lst.stream().map(io -> convertMobileHaveOffPossibleVo(io, carCd, limitCnt)).collect(Collectors.toList()));
		});

		return voResult;

	}

	@Override
	public List<MobileHaveOffInfoHstVo> findDayOffHstInfo(String fctryCd, final String carCd, String searchMonth){

		List<MobileHaveOffInfoHstVo> voResult = new ArrayList<>();
		Optional.ofNullable(tmsDayOffSvc.getListDayOffHstInfo(fctryCd, carCd, searchMonth)).ifPresent(lst -> {
		voResult.addAll(lst.stream().map(io -> convertMobileHaveOffInfoHstVo(io, carCd)).collect(Collectors.toList()));
		});

		return voResult;
	}

	@Override
	public boolean isExeDayOff(IUser user, String targetDate) {
		TmsDriverIO driver  = (TmsDriverIO) user.getUserDetails();
		TmsCarIO car = driver.getCar();
		return tmsDayOffSvc.getCntExeDayOff(driver.getFctryCd(), car.getVrn(), targetDate) > 0;
	}
	@Override
	public boolean isExeDayOff(IUser user, String targetDate, double unit) {
		TmsDriverIO driver  = (TmsDriverIO) user.getUserDetails();
		TmsCarIO car = driver.getCar();
		return tmsDayOffSvc.getCntExeDayOff(driver.getFctryCd(), car.getVrn(), targetDate, unit) > 0;
	}

	private MobileHaveOffPossibleVo convertMobileHaveOffPossibleVo(TmsDayOffIO io, String carCd, int limitCnt) {
		MobileHaveOffPossibleVo vo = new MobileHaveOffPossibleVo();
		BeanUtils.copyProperties(io, vo);
		vo.setCarCd(carCd);
		vo.setDay(ServiceUtils.getdayOfWeek_KO(vo.getTargetDate(),"yyyyMMdd"));
		vo.setSchCountMax(limitCnt);
		return vo;
	}

	private MobileHaveOffInfoHstVo convertMobileHaveOffInfoHstVo(TmsDayOffIO io, String carCd){
		MobileHaveOffInfoHstVo vo = new MobileHaveOffInfoHstVo();
		BeanUtils.copyProperties(io, vo);
		vo.setCarCd(carCd);
		vo.setTargetDate(ServiceUtils.isNotEmpty(vo.getExeDayoffDate())?vo.getExeDayoffDate():vo.getSchStartDate());
		return vo;
	}

	@Override
	public String exitFactoryByWeb(IUser user, Long allocatedGroupId, String routeNo, String dlvyDe, String fctryCd) {
		TmsDriverIO driver = (TmsDriverIO) user.getUserDetails();
		TmsCarIO car = driver.getCar();
		T_EVENT_WEB_IO_HIST entity = new T_EVENT_WEB_IO_HIST();
		entity.setAllocatedId(allocatedGroupId);
		entity.setDlvyDe(dlvyDe);
		entity.setEventDt(new Date());
		entity.setEvnetType(MobileEventType.EXIT_FACTORY.toString());
		entity.setRegId(user.getUsername());
		entity.setRouteNo(routeNo);
		entity.setVrn(car.getVrn());

		String _routeNo = routeNo == null ? "" : routeNo;
		if(_routeNo.contains("_")) {
			List<TmsPlanIO> plans = tmsPlanSvc.findTmPlansByIds(dlvyDe, routeNo);

			if(plans != null && !plans.isEmpty()) {
				TmsPlanIO plan = plans.get(0);
				fctryCd = plan.getStopCd();
			}
		}
		entity.setFctryCd(fctryCd);
		evtWebIoRepo.save(entity);

		
		String msg = new String();
		if(tmsPlanSvc.updateExitFactory(user.getUsername(), fctryCd, car.getVrn(), dlvyDe, routeNo, new Date())) { 
			msg = String.format("[서울우유2CH] %s일의 %s노선의 공장출발 처리가 완료되었습니다.", dlvyDe, routeNo);
			eventExitFactory.fireEvent(ExitFactoryEvent.of(user, entity.getEventDt(), allocatedGroupId, routeNo, dlvyDe, MobileEventType.EXIT_FACTORY, fctryCd));
		}else { 
			msg = String.format("[서울우유2CH] %s일의 %s노선의 상차처리가 완료되지 않았습니다.\r\n물류팀에 확인부탁드립니다.", dlvyDe, routeNo);
		}
		MobileAppInfoIO appInfo = appSvc.findAppInfoByUser(user).orElse(null);
		if(appInfo != null) {
			eventPush.fireEvent(
					AppPushEvent.builder()
					.appKey(appInfo.getAppTkn())
					.contents(msg)

					.datas(-1L)
					.priority(Priority.high)
					.mobileNo(user.getMobileNo())
					.user(user)
					.build());
		}

		return msg;
	}

	@Override
	public String enterFactoryByWeb(IUser user, Long allocatedGroupId, String routeNo, String dlvyDe, String fctryCd) {
		TmsDriverIO driver = (TmsDriverIO) user.getUserDetails();
		TmsCarIO car = driver.getCar();
		T_EVENT_WEB_IO_HIST entity = new T_EVENT_WEB_IO_HIST();
		entity.setAllocatedId(allocatedGroupId);
		entity.setDlvyDe(dlvyDe);
		entity.setEventDt(new Date());
		entity.setEvnetType(MobileEventType.ENTER_FACTORY.toString());
		entity.setRegId(user.getUsername());
		entity.setRouteNo(routeNo);
		entity.setVrn(car.getVrn());

		String _routeNo = routeNo == null ? "" : routeNo;
		if(_routeNo.contains("_")) {
			List<TmsPlanIO> plans = tmsPlanSvc.findTmPlansByIds(dlvyDe, routeNo);

			if(plans != null && !plans.isEmpty()) {
				TmsPlanIO plan = plans.get(plans.size() - 1);
				fctryCd = plan.getStopCd();
			}
		}
		entity.setFctryCd(fctryCd);
		evtWebIoRepo.save(entity);
		eventEnterFactory.fireEvent(EnterFactoryEvent.of(user, entity.getEventDt(), allocatedGroupId,routeNo, dlvyDe, MobileEventType.ENTER_FACTORY, fctryCd));
		String msg = String.format("[서울우유2CH] %s일의 %s노선의 공장 도착 처리가 완료되었습니다.", dlvyDe, routeNo);
		MobileAppInfoIO appInfo = appSvc.findAppInfoByUser(user).orElse(null);
		if(appInfo != null) {
			eventPush.fireEvent(
					com.sdc2ch.service.event.model.AppPushEvent.builder()
					.appKey(appInfo.getAppTkn())
					.contents(msg)

					.datas(-1L)
					.priority(Priority.high)
					.mobileNo(user.getMobileNo())
					.user(user)
					.build());
		}

		return msg;
	}

	public boolean validOverPaidDayoff(String fctryCd, String carCd, String startDate, String endDate,
			int dayoffValue) {

		return tmsDayOffSvc.validOverPaidDayoff(fctryCd, carCd, startDate, endDate, dayoffValue);
	}

	public boolean validDayoffSch(String fctryCd, String carCd, String targetDate) {
		return tmsDayOffSvc.validDayoffSch(fctryCd, carCd, targetDate);
	}

	@Override
	public MobileCaralcInfVo findAllocatedInfoByUserAndDlvyDeV4(IUser user, String dlvyDe) throws ServiceException {
		String _dlvyDe = dlvyDe == null ? tmsShipSvc.getTmsDeliveryRuleDate() : dlvyDe;
		T_CARALC_CNFIRM_GROUP2 group = alcGroupSvc.findDlvyByUser(user, _dlvyDe).orElse(null);
		
		return group == null || group.getTrnsmisDt() == null? null : findAllocatedInfoByGroupIdV4(group.getId());
	}

}
