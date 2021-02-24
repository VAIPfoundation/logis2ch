package com.sdc2ch.web.websocket.enums;

import com.sdc2ch.web.websocket.I2ChWebsocketService;


public enum BrokerDestination {
	
	
	NFC("/nfc/event")
	
	;
	
	private String dest;
	
	BrokerDestination(String dest){
		this.dest = dest;
	}

	public String getDest() {
		return dest;
	}
}
