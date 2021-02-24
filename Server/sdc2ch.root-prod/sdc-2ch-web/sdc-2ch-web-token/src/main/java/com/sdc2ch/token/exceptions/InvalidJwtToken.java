package com.sdc2ch.token.exceptions;

import org.springframework.security.authentication.BadCredentialsException;


public class InvalidJwtToken extends BadCredentialsException {
    private static final long serialVersionUID = -294671188037098603L;
    
    public InvalidJwtToken(String message, Exception e) {
    	super(message, e);
    }
}
