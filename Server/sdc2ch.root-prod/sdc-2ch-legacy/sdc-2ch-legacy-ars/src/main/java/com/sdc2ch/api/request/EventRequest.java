package com.sdc2ch.api.request;

import java.util.List;

import com.sdc2ch.api.callevent.EICNCallEvent;
import com.sdc2ch.api.version.Version;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventRequest {
	private String callSid;
	private List<EICNCallEvent> event;
	private Version version;
	private int port;
}
