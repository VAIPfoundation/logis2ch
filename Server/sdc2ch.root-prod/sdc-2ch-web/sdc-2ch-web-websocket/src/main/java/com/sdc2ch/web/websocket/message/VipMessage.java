package com.sdc2ch.web.websocket.message;

public class VipMessage {

    private String log;
    public VipMessage(String json) {
    		this.log = json;
    }
    public VipMessage() {
    }

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

}
