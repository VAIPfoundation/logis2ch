package com.sdc2ch.prcss.ds.event;

public interface IGeoFenceEvent extends IChainLocEvent {
	
	public enum GeoFenceEvent {
		GEO_ENTER,
		GEO_ARRIVE,
		GEO_EXIT
	}
	
	GeoFenceEvent getGeoEvent();
	double getDistance();
	long getIncrement();
	String getStopCd();
	String getRouteNo();

}
