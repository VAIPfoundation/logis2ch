package com.sdc2ch.web.websocket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.sdc2ch.web.websocket.I2ChWebsocketService;
import com.sdc2ch.web.websocket.enums.BrokerDestination;
import com.sdc2ch.web.websocket.enums.BrokerType;

@Service
public class WebsocketService implements I2ChWebsocketService {

	@Autowired SimpMessageSendingOperations smso;
	@Override
	public void boradcast(BrokerType broker, BrokerDestination dest, Object data) {
		smso.convertAndSend(makeDest(broker, dest), data);
	}

	private String makeDest(BrokerType broker, BrokerDestination dest) {
		return broker.getName() + dest.getDest();
	}
	
	

}
