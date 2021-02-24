package com.sdc2ch.prcss.ds.event;

import java.util.Date;

import com.sdc2ch.prcss.ds.event.IShippingContextEvent.EventAction;
import com.sdc2ch.prcss.ds.event.IShippingContextEvent.EventBy;
import com.sdc2ch.prcss.ds.event.IShippingContextEvent.EventNm;
import com.sdc2ch.prcss.ds.io.ShippingState2;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.enums.SetupLcType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ShippingStateEvent implements IShippingEvent {
	
	private ShippingState2 state;
	private Date eventDt;
	private EventBy eventBy;
	private EventAction eventAct;
	private String routeNo;
	private String stopCd;
	private SetupLcType setupLc;
	private String cause;
	private IUser user;
	private EventNm evnetNm;
	private String dlvyLcNm;
	
	private int squareBoxQty;
	
	
	private int triangleBoxQty;
	
	
	private int yodelryBoxQty;

	
	private int palletQty;

	@Override
	public IUser user() {
		return user;
	}

	@Override
	public ShippingEventTy getShippingEventTy() {
		return ShippingEventTy.STATE_EVENT;
	}
}
