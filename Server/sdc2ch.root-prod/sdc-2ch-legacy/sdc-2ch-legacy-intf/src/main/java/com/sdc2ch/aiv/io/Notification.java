package com.sdc2ch.aiv.io;

import com.sdc2ch.aiv.event.IFirebaseNotificationEvent.Priority;

public abstract class Notification {
	
	private Priority priority;
	private String to;
	
	public Priority getPriority() {
		return priority;
	}
	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	
	public enum commandType {
	    
	    START_JOB,
	    
	    END_JOB,

	    
	    CUR_LOC,

	    
	    CALL_WEBVIEW,
	    
	    HEALTH_CHK
	    ,
	    
	    OPEN_BR,
	    
	    LIVE_LOCATION_START,
	    
	    
	    LIVE_LOCATION_STOP,
	    
	    SETTINGS_INTERVAL,
	    
	    READ_TTS, 
	    
	    PUSH,
	    
	    CALL_END_EVENT,
	    
	    SDC2CH_FCM_EVENT
	}
	
}
