package com.sdc2ch.prcss.ds.event;

import com.sdc2ch.require.event.I2ChInternalEvent;

public interface IProcessEvent extends I2ChInternalEvent<IProcessEvent>{
	long getTimeStamp();
	ActionEventType getActionEventTy();
	int hashCode();
}
