package com.sdc2ch.tms.service;

import com.sdc2ch.tms.enums.DeliveryType;
import com.sdc2ch.tms.enums.FactoryType;
import com.sdc2ch.tms.enums.SeasonType;
import com.sdc2ch.tms.enums.ShippingType;
import com.sdc2ch.tms.enums.TransportType;
import com.sdc2ch.tms.io.TmsPlanIO;

public interface ITmsShippingService {

	
	FactoryType findFactoryType(String routeNo);

	
	ShippingType findShippingType(String routeNo);

	
	SeasonType findSeasonType(String routeNo);

	
	DeliveryType findDeliveryType(String routeNo);

	
	TransportType findTransportType(String routeNo);

	
	String getTmsDeliveryRuleDate();

	
	String replaceMobileNo(String mobileNo);

	
	String getPlanedArriveTime(TmsPlanIO plan);

	String getPlanedArriveTime(String dlvyDe, String stopCd, String stopTy, String routeNo, Integer stopSeq,
			String ldngSt, String departPlanTime, String arrivePlanTime);

}
