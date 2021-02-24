package com.sdc2ch.token.error;

import java.util.Date;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdc2ch.core.security.auth.error.ApiException;
import com.sdc2ch.core.security.auth.error.ErrorCode;

import lombok.Builder;

@Builder
public class AuthErrorBuilder {
	
	@Builder.Default
	private ObjectMapper mapper = new ObjectMapper();
	private HttpStatus status;
	private String message;
	private Throwable t;
	private String path;
	private ErrorCode code;
	
	
	public String toJson() {

		try {
			return mapper.writeValueAsString(ApiException.builder().error(status.getReasonPhrase()).errorCode(code).message(message)
					.path(path).status(status.value()).timestamp(new Date()).build());
		} catch (JsonProcessingException e) {
			t = e;
		}
		return t.getMessage();
	}
	
	public ApiException throwError() {
		return ApiException.builder().error(status.getReasonPhrase()).errorCode(code).message(message)
				.path(path).status(status.value()).timestamp(new Date()).build();
	}
	

}
