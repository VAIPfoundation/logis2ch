package com.sdc2ch.aiv.io;

import java.math.BigDecimal;

public interface RealtimeInfoIO {

	Long getId();
	String getFctryCd();
	String getFctryNm();
	String getVhcleTy();
	String getTrnsprtCmpny();
	String getVrn();
	String getDriverCd();
	String getDriverNm();
	String getLdngTy();
	String getMobileNo();
	BigDecimal getAccuracy();
	String getAdres();
	Integer getAltitude();
	String getDataDt();
	String getDgree();
	Integer getDayDistance();
	String getEvent();
	BigDecimal getLat();
	BigDecimal getLng();
	BigDecimal getSpeed();
	String getUpdateDt();
	String getDlvyDe();
	String getRouteNo();
	String getDlvyLcId();
	String getAlcDt();
	String getAlcGroupId();
	String getChain();
	String getHint();
	String getState();
	
}
