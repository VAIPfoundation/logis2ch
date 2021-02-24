package com.sdc2ch.service.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import org.springframework.data.convert.JodaTimeConverters.LocalDateTimeToDateConverter;
import org.springframework.util.StringUtils;

public class ServiceUtils {
	
	
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}
	
	
	public static boolean isEmpty(String str) {
		return StringUtils.isEmpty(str);
	}
	
	public static String getdayOfWeek_KO(String strDate, String format) {
		try {
			return getdayOfWeek_KO(new SimpleDateFormat(format).parse(strDate));
		} catch (ParseException e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	public static Integer getdayOfWeek_INT(String strDate, String format) {
		try {
			
			Calendar cal = Calendar.getInstance() ;
			cal.setTime(new SimpleDateFormat(format).parse(strDate));
			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			return dayOfWeek;
		} catch (ParseException e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getdayOfWeek_KO(Date targetDate) {
		Calendar cal = Calendar.getInstance() ;
		cal.setTime(targetDate);
		
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		
		switch(dayOfWeek) {
		case 1:
			return "일";
		case 2:
			return "월";
		case 3:
			return "화";
		case 4:
			return "수";
		case 5:
			return "목";
		case 6:
			return "금";
		case 7:
			return "토";
		default:
			return "";
		}
	}

}
