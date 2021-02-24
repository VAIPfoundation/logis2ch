package com.sdc2ch.tms.io;

import java.util.Date;

public interface TmsDayOffIO {
	
	
	public Long getId();
	public String getCenterCd();
	public String getDayoffDate();
	public String getEndDate();
	public String getCarCd();
	public String getDayoffDesc();
	public String getDayoffType();
	public Integer getDayoffCnt();
	public String getRegFlag();
	public String getRegUserId();
	public Date getRegDateTime();
	public String getOtherComment();
	public Boolean getIsConfirm();
	public String getDayoffRoute();
	public Integer getSchId();
	public String getSchConfirm();
	public String getSchStartDate();
	public String getSchEndDate();
	public String getSchType();
	public String getSchUnit(); 
	public String getSchRouteNo(); 
	public String getSchBigo(); 
	public String getSchRegUserId(); 
	public String getSchRegDateTime();
	public Integer getExeId();
	public String getExeDayoffDate();
	public String getExeType();
	public String getExeUnit(); 
	public String getExeBigo();
	public String getExeRegUserId();
	public String getExeRegDateTime();
	public String getExeDayoffDesc();
	public String getSchDayoffDesc();
	public String getTargetDate();
	public Double getSchCount();
	public Integer getSchCountMax();
	
}
