package com.sdc2ch.ars.event;

import java.util.Set;

import com.sdc2ch.ars.enums.CallType;
import com.sdc2ch.ars.enums.SenderType;
import com.sdc2ch.require.event.I2ChIncommingEvent;

public interface IArsEvent extends I2ChIncommingEvent<IArsEvent>{
	CallType getCallType();
	String getCommandName();
	SenderType getSenderType();
	String getRouteNo();
	String getDriverMobile();
	String getFctryCd();
	Set<String> getCallers();
	String getDlvyLcNm();
	String getDlvyLcMobile();
	String getDriverNm();
	String getDlvyDe();
}
