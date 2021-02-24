package com.sdc2ch.api;

import com.sdc2ch.api.callevent.EICNCallEvent;
import com.sdc2ch.api.request.EventRequest;
import com.sdc2ch.ars.event.IArsEvent;
import com.sdc2ch.tms.io.TmsPlanIO;

import lombok.Getter;
import lombok.Setter;

public class CallEventSession {

	@Getter @Setter
	private IArsEvent event;
	@Getter @Setter
	private EventRequest request;
	@Getter @Setter
	private TmsPlanIO plan;
	@Getter @Setter
	private String caller;

	private int count;
	public void addCount() {
		count = count + 1;
	}
	public boolean isExpired() {
		return count > 3;
	}
}
