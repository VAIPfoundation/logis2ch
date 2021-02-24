package com.sdc2ch.aiv.event;

import com.sdc2ch.require.event.I2ChInternalEvent;

 
public interface IFirebaseNotificationEvent extends I2ChInternalEvent<IFirebaseNotificationEvent> {
	
	public enum Priority {
		high,
		normal
	}
	
	
	String getAppKey();
	
	String getContents();
	
	Object getDatas();
	
	
	String getMobileNo();
	
	Priority getPriority();
	
}
