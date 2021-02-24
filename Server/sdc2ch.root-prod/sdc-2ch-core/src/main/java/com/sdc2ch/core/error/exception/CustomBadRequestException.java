package com.sdc2ch.core.error.exception;

import org.springframework.http.HttpStatus;

import com.sdc2ch.core.security.auth.error.ErrorCode;

public class CustomBadRequestException extends Custom2ChException {

	private static final long serialVersionUID = -7371441553487145483L;
	
	public CustomBadRequestException(HttpStatus status, ErrorCode error, String message) {
		super(status, error, message);
	}

}
