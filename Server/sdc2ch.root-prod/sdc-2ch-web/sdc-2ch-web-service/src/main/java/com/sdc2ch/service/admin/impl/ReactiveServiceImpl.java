package com.sdc2ch.service.admin.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdc2ch.nfc.event.INfcFireEvent;
import com.sdc2ch.require.event.I2ChEvent;
import com.sdc2ch.require.pubsub.I2ChEventConsumer;
import com.sdc2ch.require.pubsub.I2ChEventManager;
import com.sdc2ch.web.websocket.I2ChWebsocketService;
import com.sdc2ch.web.websocket.enums.BrokerDestination;
import com.sdc2ch.web.websocket.enums.BrokerType;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReactiveServiceImpl {
	
	@Autowired I2ChWebsocketService wsSvc;
	private I2ChEventConsumer<INfcFireEvent> sub;
	
	private void sendWS(I2ChEvent<INfcFireEvent> e) {
		log.info("{}", e);
		wsSvc.boradcast(BrokerType.EXETERNAL, BrokerDestination.NFC, e);
	}

	@Autowired
	protected void init(I2ChEventManager manager) {
		sub = manager.subscribe(INfcFireEvent.class);
		sub.filter(e -> sendWS(e));
	}
}
