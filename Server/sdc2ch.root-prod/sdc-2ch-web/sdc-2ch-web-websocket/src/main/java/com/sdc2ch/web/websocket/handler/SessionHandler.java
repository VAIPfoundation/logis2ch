package com.sdc2ch.web.websocket.handler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class SessionHandler {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionHandler.class);
    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
    
    private static final String LOCAL_SESSION_KEY = SessionHandler.class.getSimpleName() + ".timestamp";
    private static final long LOCAL_SESSION_TIMEOUT = 24 * 60 * 60 * 1000;



    public void register(WebSocketSession session) {
    	
    		session.getAttributes().put(LOCAL_SESSION_KEY, System.currentTimeMillis());
        sessionMap.put(session.getId(), session);
    }
    
    public void unregister(WebSocketSession session) {	
	    sessionMap.remove(session.getId());
    }
    
    @Scheduled(fixedRate = 10000 * 6)
    public void checkSession() {
    	
		WebSocketSession session;
		LOGGER.debug("checkSession() current session size {}", sessionMap.keySet());
		for(String k : sessionMap.keySet()) {
			
			session = sessionMap.get(k);
			
			if(session.isOpen()) {
				
	            try {
	            	
		            	if(session.getAttributes() != null) {
		            		
		            		long time = (long) session.getAttributes().get(LOCAL_SESSION_KEY);
		            		long timediffer = System.currentTimeMillis() - time;
		            		
		            		if(LOCAL_SESSION_TIMEOUT < timediffer) {
		            			LOGGER.info("expired session: {}", session.getId());
			            		session.close();
			            		sessionMap.remove(k);
		            		}
		            	}
	            } catch (IOException e) {
	                LOGGER.error("Error while closing websocket session: {}", e);
	            }
			}
		}
    	
    }

	public WebSocketSession getSession(String id) {
		return sessionMap.get(id);
	}

}
