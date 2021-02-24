package com.sdc2ch.prcss.ds.repo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class T_SHIPPING_COMMUTE_TIME_STATE extends T_SHIPPING_STATE2 {
	
	public enum EventName {
		START("운행시작")
		,END("운행종료");
		
		public String eventNm;
		EventName(String eventNm){
			this.eventNm = eventNm;
		}
	}
	@Column(name = "EVENT_NM", length = 20)
	@Enumerated(EnumType.STRING)
	private EventName eventName;
}
