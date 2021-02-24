package com.sdc2ch.prcss.eb.impl;

import static com.sdc2ch.core.lambda.tuple.Tuple.tuple;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.Projections;
import com.sdc2ch.core.lambda.seq.Seq;
import com.sdc2ch.prcss.eb.IEmptyBoxService;
import com.sdc2ch.prcss.eb.component.PRMInterfaceComponent;
import com.sdc2ch.prcss.eb.event.IEmptyboxEvent;
import com.sdc2ch.prcss.eb.io.EmptyboxIO;
import com.sdc2ch.prcss.eb.io.EmptyboxIO.Cause;
import com.sdc2ch.prcss.eb.repo.T_EmptyBoxRepository;
import com.sdc2ch.prcss.eb.repo.domain.QT_EMPTYBOX_INFO;
import com.sdc2ch.prcss.eb.repo.domain.T_EMPTYBOX_INFO;
import com.sdc2ch.prcss.eb.vo.DriverSquareboxErpVo;
import com.sdc2ch.prcss.eb.vo.EmptyboxErpVo;
import com.sdc2ch.prcss.eb.vo.EmptyboxVo;
import com.sdc2ch.prcss.eb.vo.SummaryEmptyboxErpVo;
import com.sdc2ch.prcss.eb.vo.SummaryEmptyboxVo;
import com.sdc2ch.repo.builder.IAdmQueryBuilder;
import com.sdc2ch.require.pubsub.I2ChEventManager;
import com.sdc2ch.require.pubsub.I2ChEventPublisher;
import com.sdc2ch.require.service.I2ChUserService;
import com.sdc2ch.tms.enums.FactoryType;
import com.sdc2ch.tms.enums.ShippingType;
import com.sdc2ch.tms.io.TmsOrderStopIO;
import com.sdc2ch.tms.io.TmsPlanIO;
import com.sdc2ch.tms.service.ITmsPlanService;
import com.sdc2ch.tms.service.ITmsShippingService;
import com.sdc2ch.tms.service.ITmsStopService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmptyBoxServiceImpl implements IEmptyBoxService {

	@Autowired IAdmQueryBuilder builder;
	@Autowired ITmsPlanService tmsPlanSvc; 
	@Autowired ITmsShippingService shipSvc; 
	@Autowired ITmsStopService stopSvc;

	@Autowired T_EmptyBoxRepository eboxRepo; 

	@Autowired I2ChUserService userSvc;
	
	@Autowired PRMInterfaceComponent prm;

	I2ChEventPublisher<IEmptyboxEvent> emtPub;
	private ExecutorService async = Executors.newSingleThreadExecutor();

	@Autowired
	private void setEmptyboxEvent(I2ChEventManager manager) {
		emtPub = manager.regist(IEmptyboxEvent.class);
	}

	public List<EmptyboxVo> findTmsPlanByRoute(String dlvyDe, String ... routeNos) {
		return tmsPlanSvc.findTmPlansByIds(dlvyDe, routeNos).stream()
				.filter(p -> (ShippingType.DELEVERY == shipSvc.findShippingType(p.getRouteNo())
				? !contains(p.getStopCd())
				: false))
		.map(p -> convert(p))
		.collect(Collectors.toList());
	}

	@Override
	public Optional<EmptyboxIO> findEmptyBoxById(Long id) throws ServiceException {
		return Optional.ofNullable(eboxRepo.findById(id).orElse(null));
	}

	public List<EmptyboxVo> findEmptyBoxByRoute(String dlvyDe, String ... routeNos) {
		return findEmptyBoxByRoute(dlvyDe, false, routeNos);
	}
	
	public List<EmptyboxVo> findEmptyBoxByRouteByErp(String dlvyDe, String ... routeNos) {
		return findEmptyBoxByRoute(dlvyDe, true, routeNos);
	}

	@SuppressWarnings("unchecked")
	public List<EmptyboxVo> findEmptyBoxByRoute(String dlvyDe, boolean erp, String ... routeNos) {
		
		QT_EMPTYBOX_INFO empty = QT_EMPTYBOX_INFO.t_EMPTYBOX_INFO;
		List<EmptyboxVo> vos = leftjoin(findTmsPlanByRoute(dlvyDe, routeNos), builder.create().selectFrom(empty)
				.where(empty.dlvyDe.eq(dlvyDe).and(empty.routeNo.in(routeNos))).fetch());

		if (erp) {	
			List<EmptyboxErpVo> vosErp = new ArrayList<EmptyboxErpVo>(vos.size());

			for ( EmptyboxVo vo : vos ) {

				EmptyboxErpVo voErp = new EmptyboxErpVo();
				BeanUtils.copyProperties(vo, voErp);

				String stopCd = voErp.getStopCd();
				String routeNo = voErp.getRouteNo();

				Object[] params = { dlvyDe, routeNo, stopCd};
				String procName = "[dbo].[SP_2CH_EB_PRM_SEL]";

				
				List<Object[]> rows = new ArrayList<>();

				
				rows = builder.storedProcedureResultCall(procName, params);
				if ( rows.size() > 0 ) {
					Object[] row = rows.get(0);
					
					int sqBoxCnt = (row[4] != null) ? Integer.parseInt(row[4]+"") : 0 ;			
					int trBoxCnt = (row[5] != null) ? Integer.parseInt(row[5]+"") : 0 ;			
					int ydBoxCnt = (row[6] != null) ? Integer.parseInt(row[6]+"") : 0 ;			
					int paBoxCnt = (row[7] != null) ? Integer.parseInt(row[7]+"") : 0 ;			

					int sqErpBoxCnt = (row[8] != null) ? Integer.parseInt(row[8]+"") : 0 ;		
					int trErpBoxCnt = (row[9] != null) ? Integer.parseInt(row[9]+"") : 0 ;		
					int ydErpBoxCnt = (row[10] != null) ? Integer.parseInt(row[10]+"") : 0 ;	
					int paErpBoxCnt = (row[11] != null) ? Integer.parseInt(row[11]+"") : 0 ;	

					voErp.setSquareBoxQty(sqBoxCnt);			
					voErp.setTriangleBoxQty(trBoxCnt);			
					voErp.setYodelryBoxQty(ydBoxCnt);			
					voErp.setPalletQty(paBoxCnt);				

					voErp.setSquareBoxErpQty(sqErpBoxCnt);		
					voErp.setTriangleBoxErpQty(trErpBoxCnt);	
					voErp.setYodelryBoxErpQty(ydErpBoxCnt);		
					voErp.setPalletErpQty(paErpBoxCnt);			
				}

				vosErp.add(voErp);

			}
			vos = (List<EmptyboxVo>)(Object)vosErp;

		} else {	
		}
		if(vos != null) {
			try {
				vos = vos.stream().sorted(Comparator.nullsLast(Comparator.comparing(EmptyboxVo::getArriveTime))).collect(Collectors.toList());
			}catch (Exception e) {
				log.error("findEmptyBoxByRoute() {}", e);
			}
		}
		return vos;
	}


	public List<EmptyboxVo> findEmptyBoxByRouteOnly(String dlvyDe, String ... routeNos) {
		return findEmptyBoxByRouteOnly(dlvyDe, false, routeNos);
	}

	@SuppressWarnings("unchecked")
	public List<EmptyboxVo> findEmptyBoxByRouteOnly(String dlvyDe, boolean erp, String ... routeNos) {
		
		QT_EMPTYBOX_INFO empty = QT_EMPTYBOX_INFO.t_EMPTYBOX_INFO;
		List<EmptyboxVo> vos = leftjoin(findTmsPlanByRoute(dlvyDe, routeNos), builder.create().selectFrom(empty)
				.where(empty.dlvyDe.eq(dlvyDe).and(empty.routeNo.in(routeNos))).fetch());

		if (erp) {	
			String stringByrouteNos = "";
			for ( String routeNo : routeNos ) {
				stringByrouteNos += ",''" + routeNo + "''";
			}
			
			if ( stringByrouteNos.length() > 0 ) {
				stringByrouteNos = stringByrouteNos.substring(1);	
			} else {
				log.info("findEmptyBoxByRouteOnly=> dlvyDe={}, erp={}, routeNos.length={}", dlvyDe, erp, routeNos.length);
			}

			Object[] params = { dlvyDe, stringByrouteNos};
			String procName = "[dbo].[SP_2CH_EB_PRM_SEL_ROUTENOS]";

			
			List<Object[]> rows = new ArrayList<>();

			
			rows = builder.storedProcedureResultCall(procName, params);


			List<EmptyboxErpVo> vosErp = new ArrayList<EmptyboxErpVo>(vos.size());

			for ( EmptyboxVo vo : vos ) {

				EmptyboxErpVo voErp = new EmptyboxErpVo();
				BeanUtils.copyProperties(vo, voErp);

				String routeNo = voErp.getRouteNo();
				String stopCd = voErp.getStopCd();


				for ( Object[] row : rows ) {

					String erpDlvyDe = (row[0] != null) ? (row[0]+"") : "" ;	
					String erpRouteNo = (row[1] != null) ? (row[1]+"") : "" ;	
					String erpStopCd = (row[2] != null) ? (row[2]+"") : "" ;	

					if ( routeNo.equals(erpRouteNo) && stopCd.equals(erpStopCd) ) {
						int sqBoxCnt = (row[7] != null) ? Integer.parseInt(row[7]+"") : 0 ;			
						int trBoxCnt = (row[8] != null) ? Integer.parseInt(row[8]+"") : 0 ;			
						int ydBoxCnt = (row[9] != null) ? Integer.parseInt(row[9]+"") : 0 ;			
						int paBoxCnt = (row[10] != null) ? Integer.parseInt(row[10]+"") : 0 ;			

						int sqErpBoxCnt = (row[11] != null) ? Integer.parseInt(row[11]+"") : 0 ;		
						int trErpBoxCnt = (row[12] != null) ? Integer.parseInt(row[12]+"") : 0 ;		
						int ydErpBoxCnt = (row[13] != null) ? Integer.parseInt(row[13]+"") : 0 ;	
						int paErpBoxCnt = (row[14] != null) ? Integer.parseInt(row[14]+"") : 0 ;	

						voErp.setSquareBoxQty(sqBoxCnt);			
						voErp.setTriangleBoxQty(trBoxCnt);			
						voErp.setYodelryBoxQty(ydBoxCnt);			
						voErp.setPalletQty(paBoxCnt);				

						voErp.setSquareBoxErpQty(sqErpBoxCnt);		
						voErp.setTriangleBoxErpQty(trErpBoxCnt);	
						voErp.setYodelryBoxErpQty(ydErpBoxCnt);		
						voErp.setPalletErpQty(paErpBoxCnt);			

						
						if ( row[11] != null && row[12] != null && row[13] != null && row[14] != null ) {
							voErp.setErpConfirmYn(true);
						} else {
							voErp.setErpConfirmYn(false);
						}

					}

				}
				vosErp.add(voErp);


			}
			vos = (List<EmptyboxVo>)(Object)vosErp;

		} else {	
		}
		if(vos != null) {
			try {
				vos = vos.stream().sorted(Comparator.nullsLast(Comparator.comparing(EmptyboxVo::getArriveTime))).collect(Collectors.toList());
			}catch (Exception e) {
				log.error("findEmptyBoxByRoute() {}", e);
			}
		}
		return vos;
	}


	@Override
	public boolean isCompletedBoxProcess(String dlvyDe, String ... routeNos) {
		List<EmptyboxVo> voss = findEmptyBoxByRoute(dlvyDe, routeNos);
		return voss.stream().allMatch(eb -> eb.isReadOnly() || eb.isConfirm());
	}
	
	@Override
	public boolean isCompletedBoxProcessByErp(String dlvyDe, String ... routeNos) {
		List<EmptyboxVo> voss = findEmptyBoxByRouteByErp(dlvyDe, routeNos);
		return voss.stream().allMatch(eb -> eb.isReadOnly() || eb.isConfirm());
	}
	
	@Override
	public boolean isConfirm(EmptyboxIO eb) {
		
		try {




			List<Object[]> rows = new ArrayList<>();
			rows = builder.storedProcedureResultCall("[dbo].[SP_2CH_EB_PRM_REG_CHK]"
					, eb.getDlvyDe()		
					, eb.getRouteNo()		
					, eb.getStopCd());	

			if ( rows.size() > 0 ) {		
				String row = (String)(Object)(rows.get(0));
				int cnt = Integer.parseInt(row);
				if ( cnt > 0 ) {			
					return true;
				}
			}
		} catch (Exception e) {
			log.error("{}", e);
			return true;	
		}

		return false;

	}

	@Override
	public EmptyboxIO save(EmptyboxIO eb) {
		
		T_EMPTYBOX_INFO db = null;
		TmsOrderStopIO stop = null;
		try {
			
			Long orgId = eb.getId();
			
			db = eboxRepo.findByDlvyDeAndRouteNoAndStopCd(eb.getDlvyDe(), eb.getRouteNo(), eb.getStopCd());
			if(db == null) {
				db = new T_EMPTYBOX_INFO();
			}
			
			Long dbId = db.getId();
			BeanUtils.copyProperties(eb, db);
			db.setId(dbId);
			eboxRepo.save(db);
			stop = stopSvc.findStopInfoByStopCd(eb.getStopCd());
			
			prm.offer(db);
			
			if ( orgId == null ) {
				async.submit(new Task(db));
			}
			
			return eb;
		}finally {
			if(stop != null) {
				emtPub.fireEvent(EmptyBoxEventImpl.builder()
						.dlvyLcId(eb.getStopCd())
						.dlvyLcNm(stop.getStopNm())
						.user(userSvc.findByUsername(eb.getRegUser()).get())
						.dlvyDe(eb.getDlvyDe())
						.cause(eb.getCause() == null ? null : eb.getCause().name())
						.timeStamp(System.currentTimeMillis())
						.squareBoxQty(db.getSquareBoxQty())
						.triangleBoxQty(db.getTriangleBoxQty())
						.yodelryBoxQty(db.getYodelryBoxQty())
						.routeNo(db.getRouteNo())
						.palletQty(db.getPalletQty() == null ? 0 : db.getPalletQty())
						.build());
			}
		}
	}

	

	private List<EmptyboxVo> leftjoin(List<EmptyboxVo> emptybox, List<T_EMPTYBOX_INFO> emptys){
		return Seq.seq(emptybox)
		.flatMap(v1 -> Seq.seq(emptys)
				.filter(v2 -> filter(v1, v2))
				.onEmpty(null)
				.map(v2 -> tuple(v1, v2))
				.map(t -> convert(t.v1, t.v2))
				)
		.sorted(Comparator.comparing(EmptyboxVo::getArriveTime))
		.collect(Collectors.toList());
	}

	private boolean filter(EmptyboxVo v1, T_EMPTYBOX_INFO v2) {
		return v1.hashCode() == v2.hashCode();
	}

	private EmptyboxVo convert(EmptyboxVo v1, T_EMPTYBOX_INFO v2) {
		EmptyboxVo dto = v1;
		if(v2 != null) {
			dto.setId(v2.getId());
			dto.setDriverCd(v2.getRegUser());
			dto.setConfirm(v2.isConfirm());
			dto.setSquareBoxQty(v2.getSquareBoxQty());
			dto.setTriangleBoxQty(v2.getTriangleBoxQty());
			dto.setYodelryBoxQty(v2.getYodelryBoxQty());
			dto.setPalletQty(v2.getPalletQty());
			dto.setCause(v2.getCause() == null ? null : v2.getCause());
			dto.setModifyCause(v2.getModifyCause() == null ? null : v2.getModifyCause());
			dto.setRegUser(v2.getRegUser());
			dto.setCreateDt(v2.getCreateDt());
			

		}
		return dto;
	}

	private boolean contains(String cd) {
		return Stream.of(FactoryType.values()).anyMatch(t -> t.getCode().equals(cd));
	}

	private EmptyboxVo convert(TmsPlanIO plan) {
		EmptyboxVo vo = new EmptyboxVo();
		vo.setDriverCd(plan.getDriverCd());
		vo.setBundledDlvyLc(plan.getBundledDlvyLc());
		vo.setRouteNo(plan.getRouteNo());
		vo.setStopCd(plan.getStopCd());
		vo.setDlvyLoNm(plan.getDlvyLoNm());
		vo.setDlvyDe(plan.getDlvyDe());
		String hash = plan.hashCode() + "";
		if(hash.equals(plan.getBundledDlvyLc())) {
			vo.setReadOnly(false);
		}else {
			vo.setReadOnly(!contains(plan.getFctryCd()) || !plan.getStopCd().endsWith("000"));
		}
		vo.setArriveTime(convertDateAndTimeString(plan));
		vo.setStopCd(plan.getStopCd());
		return vo;
	}

	public LocalDateTime convertDateAndTimeString(TmsPlanIO plan) {
		if(plan == null || StringUtils.isEmpty(plan.getScheDlvyStDe()) || StringUtils.isEmpty(plan.getScheDlvyStTime()))
			return LocalDateTime.now();
		return LocalDateTime.parse(plan.getScheDlvyStDe() + plan.getScheDlvyStTime(), DateTimeFormatter.ofPattern("yyyyMMddHH:mm"));
	}

	@Override
	public SummaryEmptyboxVo sumEmptyboxByDlvyDeAndRouteNo(String dlvyDe, String routeNo) {
		return sumEmptyboxByDlvyDeAndRouteNo(dlvyDe, routeNo, false);
	}

	
	
	@Override
	public SummaryEmptyboxVo sumEmptyboxByDlvyDeAndRouteNo(String dlvyDe, String routeNo, boolean erp) {
		SummaryEmptyboxErpVo svo = null;
		List<Cause> causes = new ArrayList<>();

		EmptyboxVo vo = null;
		if (erp) {
			vo = findEmptyBoxByRoute(dlvyDe, true, routeNo).stream().map(eb -> {
				causes.add(eb.getCause());
				return eb;
			}).reduce((v1, v2) -> summary((EmptyboxErpVo)v1, (EmptyboxErpVo)v2)).orElse(null);

		} else {
			vo = findEmptyBoxByRoute(dlvyDe, routeNo).stream().map(eb -> {
				causes.add(eb.getCause());
				return eb;
			}).reduce((v1, v2) -> summary(v1, v2)).orElse(null);
		}



		if(vo != null) {
			svo = new SummaryEmptyboxErpVo();
			BeanUtils.copyProperties(vo, svo);
			svo.setCauses(causes);
		}
		return (SummaryEmptyboxErpVo)svo;

	}

	@Override
	public List<EmptyboxVo> listEmptyboxByDlvyDeAndRouteNo(String dlvyDe, String routeNo) {
		List<EmptyboxVo> vo = findEmptyBoxByRoute(dlvyDe, routeNo);

		if(vo != null) {
			try {
				vo = vo.stream().sorted(Comparator.nullsLast(Comparator.comparing(EmptyboxVo::getArriveTime))).collect(Collectors.toList());
			}catch (Exception e) {
				log.error("listEmptyboxByDlvyDeAndRouteNo() {}", e);
			}
		}
		return vo;
	}
	@Override
	public List<EmptyboxVo> listEmptyboxByDlvyDeAndRouteNo(String dlvyDe, String routeNo, boolean erp) {
		List<EmptyboxVo> vo = findEmptyBoxByRoute(dlvyDe, erp, routeNo);
		if(vo != null) {
			try {
				vo = vo.stream().sorted(Comparator.nullsLast(Comparator.comparing(EmptyboxVo::getArriveTime))).collect(Collectors.toList());
			}catch (Exception e) {
				log.error("listEmptyboxByDlvyDeAndRouteNo() {}", e);
			}
		}
		return vo;
	}


	public List<EmptyboxVo> listOnlyEmptyboxByDlvyDeAndRouteNo(String dlvyDe, String routeNo) {
		
		QT_EMPTYBOX_INFO empty = QT_EMPTYBOX_INFO.t_EMPTYBOX_INFO;

		List<EmptyboxVo> ebList = builder.create().select(
					Projections.fields(EmptyboxVo.class,
						empty.routeNo, empty.dlvyDe, empty.stopCd,
						empty.squareBoxQty, empty.triangleBoxQty, empty.yodelryBoxQty, empty.palletQty,
						empty.cause, empty.modifyCause, empty.confirm
					)
				).from(empty)
					.where(empty.dlvyDe.eq(dlvyDe).and(empty.routeNo.eq(routeNo)))
					.fetch();

		return ebList;
	}

	public List<DriverSquareboxErpVo> listEmptyBoxDriverMonthly(String dlvyDe, String vrn) {
		List<DriverSquareboxErpVo> vos = new ArrayList<DriverSquareboxErpVo>();

		Object[] params = { dlvyDe, vrn};
		String procName = "[dbo].[SP_2CH_EB_PRM_SEL_DRIVER]";

		
		List<Object[]> rows = new ArrayList<>();

		
		rows = builder.storedProcedureResultCall(procName, params);

		for ( Object[] row : rows) {
			DriverSquareboxErpVo vo = new DriverSquareboxErpVo();
			vo.setDlvyDe((String)row[0]);
			vo.setVrn((String)row[1]);
			vo.setDriverSquareboxCnt1((String)row[2]);
			vo.setDriverSquareboxCnt2((String)row[3]);
			vos.add(vo);
		}

		return vos;
	}

	public List<EmptyboxErpVo> listEmptyBoxPalletMonthly(String dlvyDe, List<EmptyboxErpVo> ebVoList) {
		String where = " 1<>1";
		for ( EmptyboxErpVo ebVo : ebVoList ) {
			where += " or (회수일자 = ''" + ebVo.getDlvyDe() + "'' and 노선번호 = ''" + ebVo.getRouteNo() + "'' and 고객센터번호 = ''" + ebVo.getStopCd() + "'') ";
		}

		System.out.println(dlvyDe);
		System.out.println(where);

		Object[] params = { dlvyDe, where };
		String procName = "[dbo].[SP_2CH_EB_PRM_SEL_PALLET]";


		List<Object[]> rows = new ArrayList<>();







		rows = builder.storedProcedureResultCall(procName, params);

		List<EmptyboxErpVo> vos = new ArrayList<EmptyboxErpVo>();
		for ( Object[] row : rows) {
			EmptyboxErpVo vo = new EmptyboxErpVo();
			vo.setDlvyDe((String)row[0]);
			vo.setPalletQty((int)row[1]);
			vo.setPalletErpQty((int)row[2]);
			vos.add(vo);
		}
		return vos;




	}

	private EmptyboxVo summary(EmptyboxVo v1, EmptyboxVo v2) {
		EmptyboxVo vo = new EmptyboxVo();
		vo.setDlvyDe(v1.getDlvyDe());
		vo.setRegUser(v1.getRegUser());
		vo.setRouteNo(v1.getRouteNo());
		vo.setSquareBoxQty(v1.getSquareBoxQty() + v2.getSquareBoxQty());
		vo.setTriangleBoxQty(v1.getTriangleBoxQty() + v2.getTriangleBoxQty());
		vo.setYodelryBoxQty(v1.getYodelryBoxQty() + v2.getYodelryBoxQty());
		vo.setPalletQty(v1.getPalletQty() + v2.getPalletQty());
		return vo;
	}


	private EmptyboxErpVo summary(EmptyboxErpVo v1, EmptyboxErpVo v2) {
		EmptyboxErpVo vo = new EmptyboxErpVo();

		vo.setDlvyDe(v1.getDlvyDe());
		vo.setRegUser(v1.getRegUser());
		vo.setRouteNo(v1.getRouteNo());

		vo.setSquareBoxQty(v1.getSquareBoxQty() + v2.getSquareBoxQty());
		vo.setTriangleBoxQty(v1.getTriangleBoxQty() + v2.getTriangleBoxQty());
		vo.setYodelryBoxQty(v1.getYodelryBoxQty() + v2.getYodelryBoxQty());
		vo.setPalletQty(v1.getPalletQty() + v2.getPalletQty());

		vo.setSquareBoxErpQty(v1.getSquareBoxErpQty() + v2.getSquareBoxErpQty());
		vo.setTriangleBoxErpQty(v1.getTriangleBoxErpQty() + v2.getTriangleBoxErpQty());
		vo.setYodelryBoxErpQty(v1.getYodelryBoxErpQty() + v2.getYodelryBoxErpQty());
		vo.setPalletErpQty(v1.getPalletErpQty() + v2.getPalletErpQty());

		return vo;
	}


	private class Task implements Runnable {

		T_EMPTYBOX_INFO eb;
		
		Task(T_EMPTYBOX_INFO eb){
			this.eb = eb;
		}
		@Override
		public void run() {
			
			
			try {
				
				List<TmsPlanIO> tmsios = tmsPlanSvc.findTmPlansByIds(eb.getDlvyDe(), eb.getRouteNo());
				
				
				if(tmsios != null && !tmsios.isEmpty()) {
					tmsios.stream()
					.filter(p -> !p.getStopCd().equals(eb.getStopCd()))
					.filter(p -> p.getBundledDlvyLc().equals(eb.getStopCd())).map(p -> {
						
						T_EMPTYBOX_INFO _empt = new T_EMPTYBOX_INFO();
						try {
							BeanUtils.copyProperties(eb, _empt);
							_empt.setSquareBoxQty(0);
							_empt.setTriangleBoxQty(0);
							_empt.setYodelryBoxQty(0);
							_empt.setPalletQty(0);
							_empt.setPicture(null);
							_empt.setId(null);
							_empt.setStopCd(p.getStopCd());
							return eboxRepo.save(_empt);
						}catch (Exception e) {
							
							return _empt;
						}
					}).collect(Collectors.toList());
				}
				
			}catch (Exception e) {
				
			}finally {
				
			}
			
		}
		
	}

}
