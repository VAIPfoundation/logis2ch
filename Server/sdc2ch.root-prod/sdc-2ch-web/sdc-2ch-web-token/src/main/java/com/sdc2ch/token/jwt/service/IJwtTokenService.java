package com.sdc2ch.token.jwt.service;

import org.springframework.security.core.Authentication;

import com.sdc2ch.core.security.auth.I2CHUserContext;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.token.exceptions.InvalidJwtToken;
import com.sdc2ch.token.exceptions.JwtTokenCreatedException;
import com.sdc2ch.token.jwt.domain.JwtToken;
import com.sdc2ch.token.jwt.domain.JwtTokenImpl;

 
public interface IJwtTokenService {

	
	JwtToken refreshToken(String token) throws InvalidJwtToken;

	
	JwtToken unExpiredToken(I2CHUserContext userContext) throws JwtTokenCreatedException;
	
	
	JwtTokenImpl createToken(Authentication authentication);
	
	
	JwtTokenImpl createToken(IUser user) throws JwtTokenCreatedException;

	
	boolean checkExpiredToken(String tokenPayload);

}
