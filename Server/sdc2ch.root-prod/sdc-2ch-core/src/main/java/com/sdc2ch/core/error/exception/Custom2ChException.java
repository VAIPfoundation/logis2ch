package com.sdc2ch.core.error.exception;

import org.springframework.http.HttpStatus;

import com.sdc2ch.core.security.auth.error.ErrorCode;

import lombok.Getter;

@Getter
public class Custom2ChException extends Exception {

	private static final long serialVersionUID = -7371441553487143483L;

	private HttpStatus status;
	private ErrorCode error;
	private String message;
	
	public Custom2ChException(HttpStatus status, ErrorCode error, String message) {
		super(message);
		this.status = status;
		this.error = error;
		this.message = message;
	}
	
}
