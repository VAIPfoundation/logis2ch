package com.sdc2ch.agent.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdc2ch.core.security.auth.I2CHAuthorization;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "request.process")
public class RequestParamInterceptor extends HandlerInterceptorAdapter {

	private static final String SESSION_ATTR_KEY = RequestParamInterceptor.class.getSimpleName() + ".milli";

	private ObjectMapper mapper;
	private I2CHAuthorization auth;

	public RequestParamInterceptor(I2CHAuthorization auth, ObjectMapper mapper) {
		this.auth = auth;
		this.mapper = mapper;
	}

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        request.getSession().setAttribute(SESSION_ATTR_KEY, System.currentTimeMillis());
		return true;
    }

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {
		Long st = (Long) request.getSession().getAttribute(SESSION_ATTR_KEY);
		log.info("[{}] {} {} ms", auth.userContext().getUsername(), request.getServletPath(), System.currentTimeMillis() - st);
	}
}
