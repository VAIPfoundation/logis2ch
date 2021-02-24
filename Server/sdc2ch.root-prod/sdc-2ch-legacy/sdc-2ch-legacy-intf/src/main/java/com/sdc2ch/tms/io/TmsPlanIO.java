package com.sdc2ch.tms.io;

public interface TmsPlanIO {

	Long getId();
	int getStopSeq();
	String getRouteNo();
	String getConfRtateRate();	
	String getDlvyDe();
	String getArriveSatTime();
	String getArriveSunTime();
	String getArriveDayTime();
	String getArrivePlanTime();
	String getDepartPlanTime();
	String getStopCd();
	String getDriverCd();
	String getDriverNm();
	String getVrn();
	String getDlvyLoNm();
	Boolean getEmpty();
	String getFctryCd();
	String getMobileNo();
	String getCaraclTy();
	int hashCode1();
	int hashCode2();
	int hashCode3();
	String getCarWegit();
	String getDockNo();
	String getLat();
	String getLng();
	String getBundledDlvyLc();
	String getOrgBundledDlvyLc();
	String getLdngSt();
	String getLdngEd();
	String getLdngStDe();
	String getLdngEdDe();
	String getStopTy();
	String getManageCd();
	
	String getScheDlvyStDe();
	String getScheDlvyStTime();
	String getScheDlvyEdDe();
	String getScheDlvyEdTime();
	
	String getCMobileNo();
	String getCsMobileNo();
	
	String getAddr();
	String getConfDistance();
	String getConfTollCost();
	String getCarOil();
	
	String getCarOilQty();
	String getFreezingOilQty();
	String getTimeZoneNm();
	
	int getUniqueSequence();
}
