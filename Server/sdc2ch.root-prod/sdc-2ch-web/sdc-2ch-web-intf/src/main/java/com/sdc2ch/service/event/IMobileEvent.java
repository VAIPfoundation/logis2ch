package com.sdc2ch.service.event;

import com.sdc2ch.require.event.I2ChIncommingEvent;


public interface IMobileEvent<T extends IMobileEvent<T>> extends I2ChIncommingEvent<T> {
	
	Long getAllocatedGroupId();
	MobileEventType getMobileEventType();
	public enum MobileEventType{
		FINISH_JOB,
		START_JOB,
		ALLOCATE_CONFIRM,
		EXIT_FACTORY,
		ENTER_FACTORY
	}
}
