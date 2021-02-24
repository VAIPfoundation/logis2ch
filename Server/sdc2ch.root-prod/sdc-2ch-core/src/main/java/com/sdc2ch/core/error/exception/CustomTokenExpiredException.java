package com.sdc2ch.core.error.exception;

import org.springframework.http.HttpStatus;

import com.sdc2ch.core.security.auth.error.ErrorCode;

public class CustomTokenExpiredException extends Custom2ChException {

	public CustomTokenExpiredException(HttpStatus status, ErrorCode error, String message) {
		super(status, error, message);
	}
	private static final long serialVersionUID = 7371441553487145483L;

}
