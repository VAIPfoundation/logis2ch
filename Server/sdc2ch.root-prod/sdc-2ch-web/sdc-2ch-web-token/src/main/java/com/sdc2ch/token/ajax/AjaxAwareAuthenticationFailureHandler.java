package com.sdc2ch.token.ajax;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.sdc2ch.core.error.CommonErrorBuilder;
import com.sdc2ch.core.security.auth.error.ErrorCode;
import com.sdc2ch.core.security.exceptions.AuthMethodNotSupportedException;
import com.sdc2ch.token.exceptions.InvalidJwtToken;
import com.sdc2ch.token.exceptions.JwtExpiredTokenException;

@Component
public class AjaxAwareAuthenticationFailureHandler implements AuthenticationFailureHandler {
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException e) throws IOException, ServletException {

		String message = "Authentication failed";
		ErrorCode code = ErrorCode.GLOBAL;
		if (e instanceof InvalidJwtToken) {
			message = e.getMessage() + Optional.of(e.getCause()).get().getMessage();
			code = ErrorCode.DAMAAGED_TOKEN;
		} else if (e instanceof JwtExpiredTokenException) {
			message = "Token has expired";
			code = ErrorCode.JWT_TOKEN_EXPIRED;
		} else if (e instanceof AuthMethodNotSupportedException) {
			message = e.getMessage();
			code = ErrorCode.GLOBAL;
		} else if (e instanceof UsernameNotFoundException) {
			message = e.getMessage();
			code = ErrorCode.GLOBAL;
		} else if (e instanceof BadCredentialsException) {
			
			message = "Invalid username or password";
			code = ErrorCode.GLOBAL;
		}
		
		CommonErrorBuilder
		.of()
		.throwError(request.getServletPath(), 
				HttpStatus.UNAUTHORIZED, 
				code, 
				message)
		.sendError(response);
	}
}
