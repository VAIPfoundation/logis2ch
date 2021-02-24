package com.sdc2ch.aiv.event;

import com.sdc2ch.require.event.I2ChIncommingEvent;

public interface IGpsEvent extends I2ChIncommingEvent<IGpsEvent> {
	
	double getLat();
	double getLng();
	long getTimeStamp();
	String getSpeed();
}
