package com.sdc2ch.tms.event;

import com.sdc2ch.require.event.I2ChInternalEvent;

 
public interface ISmsEvent extends I2ChInternalEvent<ISmsEvent> {
	
	public enum SmsType {
		OTP,
		SMS
	}
	
	String getContents();
	
	
	String getMobileNo();
	
	String getSender();
	String getSenderTel();
	
	SmsType getSmsTy();
	
}
