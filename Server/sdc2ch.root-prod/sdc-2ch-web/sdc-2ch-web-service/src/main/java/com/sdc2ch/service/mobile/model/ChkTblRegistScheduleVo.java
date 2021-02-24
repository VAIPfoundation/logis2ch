package com.sdc2ch.service.mobile.model;

import java.time.DayOfWeek;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChkTblRegistScheduleVo {
	
	public enum State {
		COMPLETE,
		DAYOFF,
		NOTYET
	}
	private LocalDate regDe;
	private DayOfWeek dayOfWeek;
	private State state;
	
	public ChkTblRegistScheduleVo addState(State state) {
		this.state = state;
		return this;
	}
	public String getdayOfWeek_KO() {
		switch(dayOfWeek) {
		case FRIDAY:
			return "금";
		case MONDAY:
			return "월";
		case SATURDAY:
			return "토";
		case SUNDAY:
			return "일";
		case THURSDAY:
			return "목";
		case TUESDAY:
			return "화";
		case WEDNESDAY:
			return "수";
		default:
			return "";
		}
	}

}
