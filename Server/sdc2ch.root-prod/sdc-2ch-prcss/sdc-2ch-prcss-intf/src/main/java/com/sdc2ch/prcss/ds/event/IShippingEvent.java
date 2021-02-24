package com.sdc2ch.prcss.ds.event;

import com.sdc2ch.require.event.I2ChInternalEvent;

public interface IShippingEvent extends I2ChInternalEvent<IShippingEvent> {
	public enum ShippingEventTy {
		STATE_EVENT,
		ANALS_EVENT
	}
	public ShippingEventTy getShippingEventTy();
}
