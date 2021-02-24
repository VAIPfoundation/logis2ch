package com.sdc2ch.service.event;

import com.sdc2ch.require.event.I2ChIncommingEvent;


public interface IAdminEvent<T extends IAdminEvent<T>> extends I2ChIncommingEvent<T> {
	
	Long getAllocatedGroupId();
}
