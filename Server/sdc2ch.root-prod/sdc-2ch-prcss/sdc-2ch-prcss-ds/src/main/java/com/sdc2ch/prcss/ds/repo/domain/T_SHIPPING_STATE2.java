package com.sdc2ch.prcss.ds.repo.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import com.sdc2ch.prcss.ds.event.IShippingContextEvent.EventAction;
import com.sdc2ch.prcss.ds.event.IShippingContextEvent.EventBy;
import com.sdc2ch.prcss.ds.io.ShippingState2;
import com.sdc2ch.require.repo.T_ID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class T_SHIPPING_STATE2 extends T_ID{
	
	@Column(name = "ALC_GROUP_ID_FK")
	private Long allocatedGId;
	@Column(name = "EVENT_BY", length = 20)
	@Enumerated(EnumType.STRING)
	private EventBy eventBy;
	@Column(name = "EVENT_ACT", length = 20)
	@Enumerated(EnumType.STRING)
	private EventAction eventAct;
	@Column(name = "EVENT_DT")
	private Date eventDt;
	@Column(name = "DRIVER_CD")
	private String driverCd;
	@Column(name = "STATE")
	@Enumerated(EnumType.STRING)
	private ShippingState2 state;
	








}
