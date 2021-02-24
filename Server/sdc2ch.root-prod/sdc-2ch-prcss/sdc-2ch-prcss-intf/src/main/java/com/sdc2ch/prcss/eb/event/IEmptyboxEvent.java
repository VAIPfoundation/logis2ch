package com.sdc2ch.prcss.eb.event;

import com.sdc2ch.require.event.I2ChInternalEvent;


public interface IEmptyboxEvent extends I2ChInternalEvent<IEmptyboxEvent>{
	long getTimeStamp();
	String getDlvyLcId();
	String getDlvyLcNm();
	String getRouteNo();
	String getDlvyDe();
	String getCause();
	
	
	public int getSquareBoxQty();
	
	public int getTriangleBoxQty();
	
	public int getYodelryBoxQty();
	
	public int getPalletQty();
}
