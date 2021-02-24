package com.sdc2ch.service.common.exception;

import org.springframework.http.HttpStatus;

import com.sdc2ch.core.error.exception.Custom2ChException;
import com.sdc2ch.core.security.auth.error.ErrorCode;

public class ServiceException extends Custom2ChException {

	
	private static final long serialVersionUID = -580546136563082518L;

	public ServiceException(HttpStatus status, ErrorCode error, String message) {
		super(status, error, message);
	}

}
