package com.sdc2ch.token;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdc2ch.core.domain.LoginRequest;
import com.sdc2ch.core.security.exceptions.AuthMethodNotSupportedException;




public class PortalUsernameAuthenticationToken extends UsernamePasswordAuthenticationToken {

	
	private static final long serialVersionUID = 1L;

	public PortalUsernameAuthenticationToken(Object principal, Object credentials) {
		super(principal, credentials);
		
	}

}
