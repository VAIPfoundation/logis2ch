package com.sdc2ch.core.error;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.sdc2ch.core.error.exception.Custom2ChException;
import com.sdc2ch.core.security.auth.I2CHAuthorization;
import com.sdc2ch.core.security.auth.error.ErrorCode;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
	
	private static final String UNDOCUMENT = "UN_DOCUMENT";
	private I2CHAuthorization auth;
	
	@Autowired
	public void initMapperConfig(I2CHAuthorization auth) {
		this.auth = auth;
	}













	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public void unknownException(MethodArgumentTypeMismatchException ex, WebRequest webRequest) throws IOException {
		console(ex, webRequest).write(webRequest, HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, emptyErrorMessage(ex.getMessage()));
	}
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public void unknownException(HttpMessageNotReadableException ex, WebRequest webRequest) throws IOException {
		console(ex, webRequest).write(webRequest, HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATA, emptyErrorMessage(ex.getMessage()));
	}
    @ExceptionHandler(Exception.class)
    public void unknownException(Exception ex, WebRequest webRequest) throws IOException {
    	log.error("{}", ex);
    	console(ex, webRequest).write(webRequest, HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, emptyErrorMessage(ex.getMessage()));
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public void unknownException(AccessDeniedException ex, WebRequest webRequest) throws IOException {
    	ex.printStackTrace();
    	console(ex, webRequest).write(webRequest, HttpStatus.UNAUTHORIZED, ErrorCode.AUTHENTICATION, ex.getMessage());
    }

	@ExceptionHandler(Custom2ChException.class)
    public void badRequest(Custom2ChException ex, WebRequest webRequest) throws IOException {
		console(ex, webRequest).write(webRequest, ex.getStatus(), ex.getError(), ex.getMessage());
    }
	
	private void write(WebRequest webRequest, HttpStatus status, ErrorCode error, String message) throws IOException {
		
		HttpServletResponse response = getResponse(webRequest).orElse(null);
		
		if (response != null && !response.isCommitted()) {
			
			try {
				CommonErrorBuilder.of()
					.throwError(path(webRequest), status, error, message)
					.sendError(response);
			} catch (Exception e) {
				response.sendError(500, e.getMessage());
			}
		}
		
	}
    
    private Optional<HttpServletRequest> getRequest(WebRequest webRequest) {
    	return checkRequest(webRequest) ? 
    			Optional.of(((ServletWebRequest) webRequest).getRequest()) : 
    				Optional.empty();
    }
    private Optional<HttpServletResponse> getResponse(WebRequest webRequest) {
		return checkRequest(webRequest) ? 
				Optional.of(((ServletWebRequest) webRequest).getResponse()) : 
					Optional.empty();
    }
    
    private boolean checkRequest(WebRequest webRequest) {
    	return webRequest instanceof ServletWebRequest;
    }
    
    private GlobalExceptionHandler console(Exception ex, WebRequest webRequest) {
    	log.warn("[{}] {} error: {}", auth.userContext().getUsername(), path(webRequest), findErrorMessage(ex));
		return this;
	}
    
    private String path(WebRequest webRequest) {
    	return getRequest(webRequest).isPresent() ? getRequest(webRequest).get().getServletPath() : "";
    }
    
    private String findErrorMessage(Exception e) {
    	StackTraceElement el = Arrays.asList(e.getStackTrace()).stream().findFirst().orElse(null);
    	return StringUtils.isEmpty(e.getMessage()) ? el == null ? "" : el.toString(): e.getMessage();
    }
    
    private String emptyErrorMessage(String message) {
    	return StringUtils.isEmpty(message) ? UNDOCUMENT : message;
    }
    
}
