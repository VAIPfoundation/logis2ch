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
public class T_SHIPPING_INOUT_STATE extends T_SHIPPING_STATE2 {

	public enum EventInoutName {
		ENTER("진입"),
		ARRIVE("도착"),
		DEPART("출발"),
		OUT("진출");
		
		final String name;
		EventInoutName(String name){
			this.name = name;
		}
		public String getName() {
			return name;
		}
	}
	@Column(name = "EVENT_NM", length = 20)
	@Enumerated(EnumType.STRING)
	private EventInoutName eventName;
	@Column(name = "ROUTE_NO", length = 20)
	private String routeNo;
	@Column(name = "STOP_CD", length = 20)
	private String stopCd;
	
	
}
