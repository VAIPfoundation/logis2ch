package com.sdc2ch.tms.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class TmsUtils {

	
	private static final int BATCH_RULE_TIME = 14;

	private static SimpleDateFormat dlvyDeFormat = new SimpleDateFormat("yyyyMMdd");

	
	public enum DayOfWeek {
		
		WEEKDAY,
		
		SUNDAY,
		
		SATURDAY
	}

	
	public static String replaceMobileNo(String mobileNo) {
		if (!StringUtils.isEmpty(mobileNo)) {
			mobileNo = mobileNo.replaceAll("-", "").replaceAll(" ", "").trim();
		}
		return mobileNo;
	}

	
	public static String deliveryDeteFormatString(Date date) {
		Assert.notNull(date, "date can not be null");
		return dlvyDeFormat.format(date);
	}

	
	public static Date deliveryDeteFormatDate(String date) {
		try {
			Assert.notNull(date, "date can not be null");
			return dlvyDeFormat.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	
	public static DayOfWeek dayOfTheEeek(String dlvyDe) {

		Assert.notNull(dlvyDe, "dlvyDe can not be null");

		Calendar cal = Calendar.getInstance();
		cal.setTime(deliveryDeteFormatDate(dlvyDe));

		
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);

		switch (dayWeek) {
		case 1:
			return DayOfWeek.SUNDAY;
		case 7:
			return DayOfWeek.SATURDAY;
		default:
			return DayOfWeek.WEEKDAY;
		}

	}

	
	public static Date ruleDate() {
		Calendar cal = Calendar.getInstance();
		
		if (cal.get(Calendar.HOUR_OF_DAY) >= BATCH_RULE_TIME) {
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}

		return cal.getTime();
	}

	public static boolean isNumeric(String s) {  
	    return s != null && s.matches("[-+]?\\d*\\.?\\d+");  
	}

}
