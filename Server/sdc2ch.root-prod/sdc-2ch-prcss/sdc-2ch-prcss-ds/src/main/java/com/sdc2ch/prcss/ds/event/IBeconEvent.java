package com.sdc2ch.prcss.ds.event;

public interface IBeconEvent extends IOTEvent {

	public enum BeconEventType {
		BCON_ENTER,
		BCON_EXITD,
	}
	BeconEventType getBconEvent();
}
