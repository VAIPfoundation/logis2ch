package com.sdc2ch.web.websocket.controller;

import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import com.sdc2ch.web.websocket.message.LocationMessage;
import com.sdc2ch.web.websocket.message.VipMessage;

@Controller
public class VIpWebsocketController {

    @SendTo("/topic/log/{mdn}")
    @SubscribeMapping("/log/{mdn}")
    public VipMessage greeting(@PathVariable String mdn,  VipMessage message) throws Exception {
    	
    	
    	System.out.println("aaaaaa");
        return message;
    }
    
    @SendTo("/topic/log/{mdn}")
    @SubscribeMapping("/tracking/{mdn}")
    public VipMessage greeting2(@PathVariable String mdn,  String json) throws Exception {
    	
    	
    	
    	System.out.println("ddddddd");
        return new VipMessage(json);
    }
    
    @SendTo("/topic/location")
    @SubscribeMapping("/location")
    public LocationMessage greeting3(LocationMessage json) throws Exception {
    	
    	
    	
    		System.out.println(json);
        return json;
    }
    
    @SendTo("/dtg/{svcid}")
    @SubscribeMapping("/dtg/{svcid}")
    public String greeting4(@PathVariable String svcid, String json) throws Exception {
    	
    	
    	

        return json;
    }
    
    @SendTo("/nfc/event")
    @SubscribeMapping("/nfc/event")
    public String greeting5(String json) throws Exception {
    	
    	
    	
    		System.out.println(json);
        return json;
    }
}
