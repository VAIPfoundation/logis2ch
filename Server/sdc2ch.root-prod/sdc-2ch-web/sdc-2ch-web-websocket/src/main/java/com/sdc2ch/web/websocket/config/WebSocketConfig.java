package com.sdc2ch.web.websocket.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.ByteArrayMessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurationSupport;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import com.sdc2ch.web.websocket.enums.BrokerType;
import com.sdc2ch.web.websocket.handler.CustomSubProtocolWebSocketHandler;
import com.sdc2ch.web.websocket.interceptor.ChannelInterceptorImpl;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends WebSocketMessageBrokerConfigurationSupport implements WebSocketMessageBrokerConfigurer {
	

	
	private String[] DEST_Prefixes = {"/terminal", "/android", "/iphone"};

	private String[] BROKERS = {BrokerType.EXETERNAL.getName()};
	private String[] END_POINTS = {"/put", "/get", "/dtg"};
	
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        super.configureWebSocketTransport(registry);
        registry.setMessageSizeLimit(1024 * 1024 * 50);
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
    	
    		messageConverters.add(new ByteArrayMessageConverter());
        return super.configureMessageConverters(messageConverters);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
    	
    		registration.interceptors(getRegstrationinterceptors());
        super.configureClientInboundChannel(registration);
    }

    @Bean
    public ChannelInterceptor getRegstrationinterceptors() {
		return new ChannelInterceptorImpl();
	}
    
	@Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        super.configureClientOutboundChannel(registration);
    }


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        super.addReturnValueHandlers(returnValueHandlers);
    }
	
	
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(BROKERS);
        config.setApplicationDestinationPrefixes(DEST_Prefixes);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(END_POINTS).setAllowedOrigins("*").withSockJS();
    }
    
    @Bean
    @Override
    public WebSocketHandler subProtocolWebSocketHandler() {
        return new CustomSubProtocolWebSocketHandler(clientInboundChannel(), clientOutboundChannel());
    }

}