package com.sdc2ch.web.websocket;

import com.sdc2ch.web.websocket.enums.BrokerDestination;
import com.sdc2ch.web.websocket.enums.BrokerType;

 
public interface I2ChWebsocketService {

	
	void boradcast(BrokerType broker, BrokerDestination dest, Object data);
}
