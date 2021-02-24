package com.sdc2ch.service.event;

public interface IDriverEnterFactoryEvent  extends IMobileEvent<IDriverEnterFactoryEvent>{
	public String getRouteNo();
	public String getFctryCd();
}
