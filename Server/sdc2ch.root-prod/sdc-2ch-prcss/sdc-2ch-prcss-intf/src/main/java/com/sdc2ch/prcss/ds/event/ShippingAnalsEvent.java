package com.sdc2ch.prcss.ds.event;

import com.sdc2ch.require.domain.IUser;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ShippingAnalsEvent implements IShippingEvent {
	
	public enum AnalsTy {
		TRIP_DRV,
		CONTI_DRV
	}
	
	private long contiStDrvTime;
	private long contiEdDrvTime;
	private long contiDrvTime;
	private long tripDrvTime;
	private long tripDrvStTime;
	private long tripDrvEdTime;
	private IUser user;
	private AnalsTy analsTy;
	private double accDistance;
	
	
	@Override
	public IUser user() {
		return user;
	}
	
	@Override
	public ShippingEventTy getShippingEventTy() {
		return ShippingEventTy.ANALS_EVENT;
	}


}
