package com.sdc2ch.tms.service;

import java.util.List;

import com.sdc2ch.tms.io.TmsDayOffIO;

public interface ITmsDayOffService {


	
	public TmsDayOffIO selectOnebyId(String rowId);

	
	public int getDayoffCount(String fctryCd, String carCd, String dayoffDate);


	
	public List<TmsDayOffIO> getDayoffCountV2(String fctryCd, String carCd, String dayoffMonth);

	
	public int getDayoffLimitCnt(String fctryCd, String carCd);


	
	public List<TmsDayOffIO> getListDayOffHstInfo(String fctryCd, String carCd, String dayoffMonth);

	

	public int getCntExeDayOff(String fctryCd, String carCd, String targetDate);
	public int getCntExeDayOff(String fctryCd, String carCd, String targetDate, double unit);
	
	public boolean validOverPaidDayoff(String fctryCd, String carCd, String startDate, String endDate, double dayoffValue);

	
	public boolean validDayoffSch(String fctryCd, String carCd, String targetDate);


	
	public boolean validLimitSchV2(String fctryCd, String carCd, String targetDate, String unit);

	
	public double getSumDayoffCnt(String centerCd,  String year, String month, double dayoffCnt);

}


