package com.sdc2ch.tms.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.tms.config.TmsQueryBuilder;
import com.sdc2ch.tms.dao.TmsPlanRepository;
import com.sdc2ch.tms.domain.view.QTmsPlan;
import com.sdc2ch.tms.domain.view.TmsPlan;
import com.sdc2ch.tms.enums.FactoryType;
import com.sdc2ch.tms.io.TmsPlanIO;
import com.sdc2ch.tms.service.ITmsPlanService;
import com.sdc2ch.tms.service.ITmsShippingService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class TmsPlanServiceImpl implements ITmsPlanService {

	
	@Autowired TmsQueryBuilder builder;
	@Autowired ITmsShippingService shipSvc;
	@Autowired TmsPlanRepository planReop;


	private Map<String, _Point> factryLatLngMapped = new HashMap<>();
	@Autowired
	private void init() {



		_Point point = new _Point();
		point.setLat("37.8205560353999");
		point.setLng("127.059763030966");
		factryLatLngMapped.put("1D1", point);

		point = new _Point();
		point.setLat("37.296220411");
		point.setLng("127.111362237");
		factryLatLngMapped.put("2D1", point);

		point = new _Point();
		point.setLat("37.322633");
		point.setLng("126.757383");
		factryLatLngMapped.put("3D1", point);

		point = new _Point();
		point.setLat("35.6706711519532");
		point.setLng("127.926573029565");
		factryLatLngMapped.put("4D1", point);

		point = new _Point();
		point.setLat("37.8405418354126");
		point.setLng("127.037200530997");
		factryLatLngMapped.put("5D1", point);
	}

	@Getter
	@Setter
	class _Point {
		private String lat;
		private String lng;
	}






































	@Override
	public TmsPlanIO findTmPlanById(Long planRowid) {
		QTmsPlan plan = QTmsPlan.tmsPlan;
		return builder.create().selectFrom(plan).where(plan.id.eq(planRowid)).fetchOne();
	}

	@Override
	public List<TmsPlanIO> findTmPlansByIds(String de, String... routes) {
		QTmsPlan plan = QTmsPlan.tmsPlan;

		List<TmsPlan> plans = builder.create().selectFrom(plan).where(plan.dlvyDe.eq(de).and(plan.routeNo.in(routes)))
				.orderBy(plan.routeNo.asc(), plan.stopSeq.asc()).fetch();

		
		if(plans != null)
			plans.removeIf(p -> p == null);

		return check(plans, de, null, routes);













	}

	private List<TmsPlanIO> getTmsPlanMst(String dlvyDe, String ... routes) {

		String joins = null;
		if(routes != null && routes.length > 0) {
			joins = Arrays.asList(routes).stream().collect(Collectors.joining("','", "'", "'"));
		}
		String query = String.format(ITmsPlanService.TMS_M_PLAN_MST_QUERY, dlvyDe, joins);
		List<?> results = builder.createSelectNativeQuery(query);
		return convert(results);
	}
	private List<TmsPlanIO> getTmsPlanMst(String dlvyDe, String driverCd) {
		String query = String.format(ITmsPlanService.TMS_M_PLAN_MST_QUERY_BY_DLVYDEANDDRIVERCD, dlvyDe, driverCd);
		List<?> results = builder.createSelectNativeQuery(query);
		return convert(results);
	}
	private List<Map<String, String>> getTmsOrder(String dlvyDe, String... routeNos) {

		String joins = null;
		if(routeNos != null && routeNos.length > 0) {
			joins = Arrays.asList(routeNos).stream().collect(Collectors.joining("','", "'", "'"));
		}
		String query = String.format(ITmsPlanService.TMS_ORDER_QUERY, dlvyDe, joins);
		List<?> results = builder.createSelectNativeQuery(query);
		return convertOrder(results);
	}

	private List<TmsPlanIO> convert(List<?> results) {
		List<TmsPlanIO> plans = null;
		if(results != null) {
			plans = new ArrayList<>();
			for(Object o : results) {
				Object[] colums = (Object[]) o;
				TmsPlan plan = new TmsPlan();
				int i = 0;
				plan.setId(nullSafeLong(colums[i++]));
				plan.setFctryCd(nullSafeString(colums[i++]));
				plan.setDlvyDe(nullSafeString(colums[i++]));
				plan.setRouteNo(nullSafeString(colums[i++]));
				plan.setBatchNo(nullSafeString(colums[i++]));
				plan.setVrn(nullSafeString(colums[i++]));
				plan.setDriverCd(nullSafeString(colums[i++]));
				plan.setDriverNm(nullSafeString(colums[i++]));
				i++;
				i++;


				plan.setCarWegit(nullSafeString(colums[i++]));
				plan.setCaraclTy(nullSafeString(colums[i++]));	
				plan.setShipSeq(nullSafeInteger(colums[i++]));
				plan.setScheDlvyStDe(nullSafeString(colums[i++]));
				plan.setScheDlvyStTime(nullSafeString(colums[i++]));
				plan.setScheDlvyEdDe(nullSafeString(colums[i++]));
				plan.setScheDlvyEdTime(nullSafeString(colums[i++]));
				plan.setConfDistance(nullSafeString(colums[i++]));
				plan.setConfRtateRate(nullSafeString(colums[i++]));
				plan.setConfTollCost(nullSafeString(colums[i++]));
				plan.setTimeZoneNm(nullSafeString(colums[i++]));
				plan.setLdngEdDe(nullSafeString(colums[i++]));
				plan.setLdngSt(nullSafeString(colums[i++]));
				plan.setLdngEd(nullSafeString(colums[i++]));
				plan.setCarOil(nullSafeString(colums[i++]));
				plan.setMobileNo(nullSafeString(colums[i++]));	
				plan.setCaraclTy(nullSafeString(colums[i++]));	

				plan.setLdngStDe(plan.getScheDlvyStDe());
				plan.setStopCd(plan.getStopCd());

				plans.add(plan);
			}
		}

		return plans;
	}

	private List<Map<String, String>> convertOrder(List<?> results) {

		List<Map<String, String>> orders = null;
		if(results != null) {
			orders = new ArrayList<>();
			for(Object o : results) {
				Object[] colums = (Object[]) o;
				int i = 0;
				Map<String, String> order = new HashMap<>();

				order.put("dlvyDe", nullSafeString(colums[i++]));
				order.put("stopCd", nullSafeString(colums[i++]));
				order.put("routeNo", nullSafeString(colums[i++]));
				order.put("lat", nullSafeInteger(colums[i++]));
				order.put("lng", nullSafeInteger(colums[i++]));
				order.put("newReqTime", nullSafeString(colums[i++]));
				order.put("newReqTimeSat", nullSafeString(colums[i++]));
				order.put("newReqTimeSun", nullSafeString(colums[i++]));
				order.put("representStopCd", nullSafeString(colums[i++]));
				order.put("stopNm", nullSafeString(colums[i++]));
				order.put("Addr_Basic", nullSafeString(colums[i++]));

				orders.add(order);
			}
		}

		return orders;
	}

	private String nullSafeInteger(Object object) {
		return object == null ? "0" : object + "";
	}

	private Long nullSafeLong(Object object) {
		return object == null ? 0 : new BigDecimal(object + "").longValue();
	}
	private String nullSafeString(Object object) {
		return object == null ? null : object + "";
	}

	public LocalDateTime convertDateAndTimeString(TmsPlanIO plan) {
		if(plan == null || StringUtils.isEmpty(plan.getScheDlvyStDe()) || StringUtils.isEmpty(plan.getScheDlvyStTime()))
			return LocalDateTime.now();
		return LocalDateTime.parse(plan.getScheDlvyStDe() + plan.getScheDlvyStTime(), DateTimeFormatter.ofPattern("yyyyMMddHH:mm"));
	}

	@Override
	public String dayOfweekTimeByArriveTime(TmsPlanIO plan) {
		Assert.notNull(plan, "V_CARALC_PLAN can not be null");
		return shipSvc.getPlanedArriveTime(plan);
	}

	@Override
	public String convertRouteNo(String routeNo) {
		Assert.notNull(routeNo, "routeNo can not be null");
		return routeNo.split("_")[0];
	}

	@Override
	public List<TmsPlanIO> findAllByDeliveryDate(String dlvyDe) {
		return casting(planReop.findAllByDlvyDe(dlvyDe));
	}

	@Override
	@Transactional(readOnly = true)
	public List<TmsPlanIO> findTmPlansByUserAndDeleveryDate(IUser user, String dlvyDe) {
		
		return findTmPlansByUserAndDeleveryDate(user.getUsername(), dlvyDe);
	}

	@Override

	public List<TmsPlanIO> findTmPlansByUserAndDeleveryDate(String driverCd, String dlvyDe) {
		QTmsPlan plan = QTmsPlan.tmsPlan;
		List<TmsPlan> plans = builder.create()
				.select(plan)
				.from(plan)
				.where(plan.dlvyDe.eq(dlvyDe).and(plan.driverCd.eq(driverCd)))
				.orderBy(plan.routeNo.asc(), plan.stopSeq.asc())
				.fetch();

		

		List<TmsPlan> nullPlans = plans.stream().filter(p -> p == null).collect(Collectors.toList());
		if ( nullPlans != null && nullPlans.size() > 0 ) {
			log.info("TMS PLAN EXIST NULL ROW -> dlvyDe={}, driverCd={}", plans.size(), dlvyDe, driverCd);
			List<TmsPlanIO> tmsMsts = getTmsPlanMst(dlvyDe, driverCd);
			plans = (List<TmsPlan>)(Object) tmsMsts;
		}

		return check(plans, dlvyDe, driverCd);
	}

	@SuppressWarnings("unchecked")
	private List<TmsPlanIO> check(List<TmsPlan> plans, String dlvyDe, String username, String ... routeNo) {
		if(plans == null || plans.isEmpty()) {
			List<TmsPlanIO> tmsMsts = findPlanMsts(dlvyDe, username, routeNo);

			if(tmsMsts != null && !tmsMsts.isEmpty()) {
				return makeNewPlan(dlvyDe, tmsMsts.stream().map(p -> p.getRouteNo()).collect(Collectors.toCollection(HashSet::new)), tmsMsts);
			}

		}else {
			log.info("TMS PLAN CHECK -> plans.size()={}, dlvyDe={}, username={}, routeNo={}", plans.size(), dlvyDe, username, routeNo);
			Set<String> pRoutes = plans.stream().map(p -> p.getRouteNo()).collect(Collectors.toCollection(HashSet::new));

			if(pRoutes.stream().allMatch(r -> r.contains("_"))) {
				
				return casting (plans);
			}else if(pRoutes.stream().allMatch(r -> !r.contains("_"))) {
				
				
				List<TmsPlanIO> tmsMsts = findPlanMsts(dlvyDe, username, routeNo);
				Set<String> tRoutes = tmsMsts.stream().map(p -> p.getRouteNo()).collect(Collectors.toCollection(HashSet::new));

				int pRoutesHash = pRoutes.stream().map(s -> s.hashCode()).reduce((a, b) -> a+b).get();
				int RoutesHash = tRoutes.stream().map(s -> s.hashCode()).reduce((a, b) -> a+b).get();
				if(pRoutesHash != RoutesHash) {
					plans.removeIf(p -> !p.getRouteNo().contains("_"));
					plans.addAll((List<TmsPlan>)(Object) makeNewPlan(dlvyDe, tRoutes, tmsMsts));
				}else {

					List<String> stops = plans.stream().filter(p -> FactoryType.FFFF == FactoryType.convert(p.getStopCd())).map(p -> p.getStopCd()).collect(Collectors.toList());
					List<Map<String, String>> nstops = getTmsOrder(dlvyDe, tRoutes.toArray(new String[tRoutes.size()]));

					int oStopHash = stops.stream().map(s -> s.hashCode()).reduce((a, b) -> a+b).get();
					int nStopHash = nstops.stream().map(s -> s.get("stopCd").hashCode()).reduce((a, b) -> a+b).get();

					if(oStopHash != nStopHash) {
						plans.removeIf(p -> !p.getRouteNo().contains("_"));
						plans.addAll((List<TmsPlan>)(Object) makeNewPlan(dlvyDe, tRoutes, tmsMsts));
					}
				}

			}else {
				

				pRoutes = plans.stream().filter(p -> !p.getRouteNo().contains("_")).map(p -> p.getRouteNo()).collect(Collectors.toCollection(HashSet::new));
				List<TmsPlanIO> tmsMsts = getTmsPlanMst(dlvyDe, pRoutes.toArray(new String[pRoutes.size()]));
				Set<String> tRoutes = tmsMsts.stream().map(p -> p.getRouteNo()).collect(Collectors.toCollection(HashSet::new));

				int pRoutesHash = pRoutes.stream().map(s -> s.hashCode()).reduce((a, b) -> a+b).get();
				int RoutesHash = tRoutes.stream().map(s -> s.hashCode()).reduce((a, b) -> a+b).get();

				if(pRoutesHash != RoutesHash) {
					plans.removeIf(p -> !p.getRouteNo().contains("_"));
					plans.addAll((List<TmsPlan>)(Object) makeNewPlan(dlvyDe, tRoutes, tmsMsts));
				}else {
					List<String> stops = plans.stream().filter(p -> FactoryType.FFFF == FactoryType.convert(p.getStopCd())).map(p -> p.getStopCd()).collect(Collectors.toList());
					List<Map<String, String>> nstops = getTmsOrder(dlvyDe, tRoutes.toArray(new String[tRoutes.size()]));

					int oStopHash = stops.stream().map(s -> s.hashCode()).reduce((a, b) -> a+b).get();
					int nStopHash = nstops.stream().map(s -> s.get("stopCd").hashCode()).reduce((a, b) -> a+b).get();

					if(oStopHash != nStopHash) {
						plans.removeIf(p -> !p.getRouteNo().contains("_"));
						plans.addAll((List<TmsPlan>)(Object) makeNewPlan(dlvyDe, tRoutes, tmsMsts));
					}
				}
				return casting (plans);
			}
		}
		return casting (plans);

	}

	private List<TmsPlanIO> findPlanMsts(String dlvyDe, String username, String ... routeNo) {
		return StringUtils.isEmpty(username) ? getTmsPlanMst(dlvyDe, routeNo) : getTmsPlanMst(dlvyDe, username);
	}

	private List<TmsPlanIO> makeNewPlan(String dlvyDe, Set<String> tRoutes, List<TmsPlanIO> tmsMsts) {

		List<Map<String, String>> stops = getTmsOrder(dlvyDe, tRoutes.toArray(new String[tRoutes.size()]));
		if(stops == null || stops.isEmpty()) {
			return tmsMsts;
		}

		List<TmsPlanIO> newTmsPlans = new ArrayList<>();

		Map<String, List<Map<String, String>>> mapped = stops.stream().collect(Collectors.groupingBy(s -> s.get("routeNo")));

		for(TmsPlanIO p : tmsMsts) {

			TmsPlan _p = (TmsPlan) p;
			_p.setStopCd(_p.getFctryCd());
			_p.setDlvyLoNm(_p.getFctryCd());
			_p.setLdngSt(_p.getScheDlvyStTime());
			_p.setArrivePlanTime(_p.getScheDlvyStTime());
			_p.setArriveSatTime(_p.getScheDlvyEdTime());
			_p.setArriveSunTime(_p.getScheDlvyEdTime());

			_p.setLng(factryLatLngMapped.get(_p.getFctryCd()).lng);
			_p.setLat(factryLatLngMapped.get(_p.getFctryCd()).lat);
			_p.setStopSeq(0);

			newTmsPlans.add(_p);

			List<Map<String, String>> _stops = mapped.get(p.getRouteNo());

			if(_stops != null) {
				int seq = 0;
				for(Map<String, String> stop : _stops) {
					TmsPlan _stop = new TmsPlan();
					BeanUtils.copyProperties(p, _stop);
					
					_stop.setStopCd(stop.get("stopCd"));
					_stop.setLat(stop.get("lat"));
					_stop.setLng(stop.get("lng"));
					_stop.setArriveDayTime(stop.get("newReqTime"));
					_stop.setArriveSatTime(stop.get("newReqTimeSat"));
					_stop.setArriveSunTime(stop.get("newReqTimeSun"));
					_stop.setBundledDlvyLc(stop.get("representStopCd"));
					_stop.setDlvyLoNm(stop.get("stopNm"));
					_stop.setAddr(stop.get("Addr_Basic"));
					_stop.setShipSeq(++seq + "");
					_stop.setStopSeq(seq);
					_stop.setMobileNo(_p.getMobileNo());
					newTmsPlans.add(_stop);
				}

			}

			TmsPlan fctry = new TmsPlan();
			BeanUtils.copyProperties(_p, fctry);

			
			
			fctry.setStopSeq(newTmsPlans.size() == 0 ? 0 : newTmsPlans.get(newTmsPlans.size()-1).getStopSeq()+1 );
			fctry.setArrivePlanTime(fctry.getScheDlvyEdTime());
			fctry.setArriveSatTime(fctry.getScheDlvyEdTime());
			fctry.setArriveSunTime(fctry.getScheDlvyEdTime());
			fctry.setDepartPlanTime(fctry.getScheDlvyEdTime());
			newTmsPlans.add(fctry);

		}

		return newTmsPlans;

	}

	@SuppressWarnings("unchecked")
	private List<TmsPlanIO> casting(List<TmsPlan> plans){
		return (List<TmsPlanIO>)(Object) plans;
	}

	@Override
	public List<TmsPlanIO> findTmPlansByCustomerMobileNo(String dlvyDe, String mobileNo) {
		QTmsPlan plan = QTmsPlan.tmsPlan;
		List<TmsPlan> plans = builder.create()
		.select(plan)
		.from(plan)
		.where(plan.dlvyDe.eq(dlvyDe).and(plan.cMobileNo.eq(mobileNo).or(plan.csMobileNo.eq(mobileNo))))
		.fetch();
		return casting (plans);
	}

	@Override
	public List<TmsPlanIO> findTmPlansByCustomerMobileNoByErp(String dlvyDe, String mobileNo) {

		Object[] params = {"", dlvyDe, mobileNo};
		List<Object[]> results = builder.storedProcedureResultCall("[tms].[dbo].[SP_2CH_DLVY_LC_CNTRL_BY_MOBILENO]", params);
		List<TmsPlan> plans = new ArrayList<TmsPlan>();
		if ( results != null ) {
			for( int i=0;i<results.size();i++ ) {
				Object[] result = results.get(i);
				TmsPlan plan = new TmsPlan();
				plan.setDlvyDe(		(String)result[0]);	
				plan.setRouteNo(	(String)result[1]);	
				plan.setVrn(		(String)result[2]);	
				plan.setDriverNm(	(String)result[3]);	
				plan.setMobileNo(	(String)result[4]);	
				plan.setCMobileNo( 	mobileNo);	
	
				plans.add(plan);
			}
		}

		log.info("## ARS ## TmsPlanServiceImpl.findTmPlansByCustomerMobileNoByErp  dlvyDe={}, mobileNo={}, planSize={}", dlvyDe, mobileNo, plans.size());
		return casting (plans);
	}

	@Override
	@Transactional
	public boolean updateExitFactory(String userId, String centerCd, String carCd, String dlvyDe, String routeNo, Date date) {

		
		boolean isUpdated = false;
		if(chkPickComplate(carCd, dlvyDe, routeNo)) {
			
			Object[] param = {date, userId, centerCd, carCd, dlvyDe, routeNo};
			Object[] result = builder.storedProcedureSingleResultCall("[DBO].[SP_2CH_ENTRY_DATETIME_REG]", param);
			isUpdated = true;
		}
		return isUpdated;

	}

	@Override
	public boolean chkPickComplate(String carCd, String dlvyDe, String routeNo) {
		return planReop.isPickcomplate(carCd, dlvyDe, routeNo);
	}

	@Override
	public TmsPlanIO findTmPlansByUserAndDeleveryDateAndRouteNoAndStopCd(String driverCd, String dlvyDe, String routeNo,
			String stopCd) {
		QTmsPlan plan = QTmsPlan.tmsPlan;
		return builder.create()
				.selectFrom(plan)
				.where(plan.driverCd.eq(driverCd).and(plan.dlvyDe.eq(dlvyDe).and(plan.routeNo.eq(routeNo).and(plan.stopCd.eq(stopCd)))))
				.fetchOne();
	}

}
