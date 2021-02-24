package com.sdc2ch.web.websocket.enums;

public enum BrokerType {
	
	EXETERNAL("/ext")
	
	;
	
	private String _name;
	
	BrokerType(String name){
		this._name = name;
	}
	
	public String getName() {
		return _name;
	}

}
