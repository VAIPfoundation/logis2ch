package com.sdc2ch.tms.service.impl;

import java.time.LocalTime;
import java.util.Arrays;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.sdc2ch.tms.domain.view.TmsPlan;
import com.sdc2ch.tms.enums.DeliveryType;
import com.sdc2ch.tms.enums.FactoryType;
import com.sdc2ch.tms.enums.SeasonType;
import com.sdc2ch.tms.enums.ShippingType;
import com.sdc2ch.tms.enums.TransportType;
import com.sdc2ch.tms.io.TmsPlanIO;
import com.sdc2ch.tms.service.ITmsShippingService;
import com.sdc2ch.tms.utils.TmsUtils;

@Service
public class TmsShippingServiceImpl implements ITmsShippingService {

	
	@Override
	public FactoryType findFactoryType(String routeNo) {
		Assert.notNull(routeNo, "RouteNo can not be null");

		String cd = routeNo.substring(0, 1);
		switch (cd) {
		case "1":
			return FactoryType.F1D1;
		case "2":
			return FactoryType.F2D1;
		case "3":
			return FactoryType.F3D1;
		case "4":
			return FactoryType.F4D1;
		}
		return FactoryType.FFFF;
	}

	
	@Override
	public ShippingType findShippingType(String routeNo) {
		Assert.notNull(routeNo, "RouteNo can not be null");

		String cd = routeNo.substring(1, 3);
		switch (cd) {
		case "9N":
			return ShippingType.TRANSPORT;
		case "9M":
			return ShippingType.TRANSPORT;
		default:
			return ShippingType.DELEVERY;
		}
	}

	
	@Override
	public SeasonType findSeasonType(String routeNo) {
		Assert.notNull(routeNo, "RouteNo can not be null");
		String cd = routeNo.substring(2, 3);
		switch (cd) {
		case "A":
			return SeasonType.PEEK;
		case "B":
			return SeasonType.LOW;
		case "C":
			return SeasonType.LOW_SUMMER;
		default:
			return SeasonType.FFFF;
		}
	}

	
	@Override
	public DeliveryType findDeliveryType(String routeNo) {
		Assert.notNull(routeNo, "RouteNo can not be null");
		String cd = routeNo.substring(3, 4);
		return Arrays.asList(DeliveryType.values())
				.stream()
				.filter(d -> d.name().equals(cd))
				.findAny().orElse(DeliveryType.FFFF);
	}

	
	@Override
	public TransportType findTransportType(String routeNo) {
		Assert.notNull(routeNo, "RouteNo can not be null");
		String cd = routeNo.substring(1, 3);
		
		switch(cd) {
		case "9M":
			return TransportType.FTF;
		case "9N":
			return routeNo.contains("HY") ? TransportType.HY : TransportType.FTF;
		case "9O":
			return TransportType.OEM;
		}
		return TransportType.FFFF;
	}

	@Override
	public String getTmsDeliveryRuleDate() {
		return TmsUtils.deliveryDeteFormatString(TmsUtils.ruleDate());
	}

	@Override
	public String replaceMobileNo(String mobileNo) {
		Assert.notNull(mobileNo, "mobileNo can not be null");
		return TmsUtils.replaceMobileNo(mobileNo);
	}

	public boolean isFactory(String dlvyLcId) {
		return Arrays.asList(FactoryType.values())
				.stream().anyMatch(f -> f.getCode().equals(dlvyLcId));
	}

	public static boolean isNumeric(String s) {
	    return s != null && s.matches("[-+]?\\d*\\.?\\d+");
	}

	@Override
	public String getPlanedArriveTime(String dlvyDe, String stopCd, String stopTy, String routeNo, Integer stopSeq, String ldngSt, String departPlanTime, String arrivePlanTime) {
		Assert.notNull(dlvyDe, "dlvyDe can not be null");
		Assert.notNull(stopCd, "stopCd can not be null");
		Assert.notNull(routeNo, "routeNo can not be null");
		Assert.notNull(stopSeq, "stopSeq can not be null");
		TmsPlan tmsPlan = new TmsPlan();
		tmsPlan.setDlvyDe(dlvyDe);
		tmsPlan.setStopCd(stopCd);
		tmsPlan.setRouteNo(routeNo);
		tmsPlan.setStopSeq(stopSeq);
		tmsPlan.setLdngSt(ldngSt);
		tmsPlan.setStopTy(stopTy);
		tmsPlan.setDepartPlanTime(departPlanTime);
		tmsPlan.setArrivePlanTime(arrivePlanTime);
		return getPlanedArriveTime(tmsPlan);
	}

	@Override
	public String getPlanedArriveTime(TmsPlanIO plan) {

		if(plan == null)
			return null;

		boolean isFactory = false;
		if(plan.getStopCd() == null) {
			isFactory = "공장".equals(plan.getStopTy());
		}else {
			isFactory = isFactory(plan.getStopCd());

		}
		ShippingType shippingType = findShippingType(plan.getRouteNo());
		String time = "";
		if(isFactory) {

			if(shippingType == ShippingType.DELEVERY) {
				time = plan.getStopSeq() == 0 ? plan.getLdngSt() : plan.getDepartPlanTime();
			}else {
				time = plan.getDepartPlanTime();
			}
		}else if(ShippingType.DELEVERY == shippingType && isNumeric(plan.getStopCd())) {
			switch(TmsUtils.dayOfTheEeek(plan.getDlvyDe())) {
			case SATURDAY:
				time = plan.getArriveSatTime();
				break;
			case SUNDAY:
				time =  plan.getArriveSunTime();
				break;
			case WEEKDAY:
				time =  plan.getArriveDayTime();
				break;
			default:
				time =  plan.getArrivePlanTime();
				break;
			}
			if(StringUtils.isEmpty(time))
				time = plan.getArrivePlanTime();
		}else {
			time = plan.getArrivePlanTime();
		}
		
		try {
			LocalTime.parse(time);
		} catch (Exception e) {
			if (time != null) {
				time = time.replaceAll(";", ":");
			}
		}
		return time;
	}
}
