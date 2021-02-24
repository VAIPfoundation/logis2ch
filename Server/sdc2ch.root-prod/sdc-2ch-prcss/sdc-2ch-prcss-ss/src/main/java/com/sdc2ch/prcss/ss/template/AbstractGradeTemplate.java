package com.sdc2ch.prcss.ss.template;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public abstract class AbstractGradeTemplate {
	
	public enum GradeTy {
		A,
		B,
		C, FF
	}
	
	
	public LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
		return dateToConvert.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime();
	}
	
	public String convertLocalTimeToTimeString(Date dt) {
		
		if(dt == null)
			return null;
		LocalDateTime ldt = convertToLocalDateTimeViaInstant(dt);
		return LocalTime.of(ldt.getHour(), ldt.getMinute()).toString();
	}
	public String convertLocalTimeToDeString(Date dt) {
		if(dt == null)
			return null;
		return new SimpleDateFormat("yyyyMMdd").format(dt);
	}
}
