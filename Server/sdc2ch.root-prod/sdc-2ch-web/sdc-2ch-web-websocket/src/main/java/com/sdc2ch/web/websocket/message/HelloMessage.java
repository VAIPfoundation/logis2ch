package com.sdc2ch.web.websocket.message;

public class HelloMessage {

    private String log;

    public HelloMessage() {
    }

    public HelloMessage(String log) {
        this.log = log;
    }

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

}
