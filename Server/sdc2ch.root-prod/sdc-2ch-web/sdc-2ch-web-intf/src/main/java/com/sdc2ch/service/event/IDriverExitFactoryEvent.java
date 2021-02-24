package com.sdc2ch.service.event;

import java.util.Date;

public interface IDriverExitFactoryEvent  extends IMobileEvent<IDriverExitFactoryEvent>{
	public String getRouteNo();
	public Date getEventDt();
	public String getDlvyDe();
	public String getFctryCd();
}
