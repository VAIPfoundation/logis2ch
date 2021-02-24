package com.sdc2ch.web.websocket.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

import com.sdc2ch.web.websocket.handler.SessionHandler;

public class ChannelInterceptorImpl extends ChannelInterceptorAdapter{


	@Autowired SessionHandler handler;
	@Override
	public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
		
		
		MessageHeaders headers = message.getHeaders();
		StompCommand o = (StompCommand) headers.get("stompCommand");
		@SuppressWarnings("unused")
		String destination = SimpMessageHeaderAccessor.getDestination(headers);
		
		
		switch (o) {
		case SUBSCRIBE:
		case UNSUBSCRIBE:
		case MESSAGE:
			
			handler.getSession("");
			break;

		default:
			break;
		}
		






	}
	@Override
	public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
		
		










	}
	
	@Override
	public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {





		
	}
}
