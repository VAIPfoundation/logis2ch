package com.sdc2ch.aiv.event;

import com.sdc2ch.require.enums.SetupLcType;
import com.sdc2ch.require.event.I2ChIncommingEvent;
import com.sdc2ch.tms.enums.FactoryType;

public interface IBeaconFireEvent extends I2ChIncommingEvent<IBeaconFireEvent> {
	
	public enum  InoutType {
		IN,
		OUT
	}
	InoutType getInoutTy();
	long getTimestamp();
	String getDeviceId();
	FactoryType getFactoryTy();
	SetupLcType getSetupLcTy();
}
