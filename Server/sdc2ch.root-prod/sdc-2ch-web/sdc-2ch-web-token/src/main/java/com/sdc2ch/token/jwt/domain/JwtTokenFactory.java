package com.sdc2ch.token.jwt.domain;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.sdc2ch.core.security.auth.I2CHUserContext;
import com.sdc2ch.core.security.model.Scopes;
import com.sdc2ch.token.config.JwtSettings;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Component
public class JwtTokenFactory {
    private final JwtSettings settings;

    @Autowired
    public JwtTokenFactory(JwtSettings settings) {
        this.settings = settings;
    }

    
    public AccessJwtToken createAccessJwtToken(I2CHUserContext userContext) {
        return createAccessJwtToken(userContext, settings.getTokenExpirationTime());
    }
    public AccessJwtToken createAccessJwtToken(I2CHUserContext userContext, int expireTime) {
    	if (StringUtils.isEmpty(userContext.getUsername())) 
    		throw new IllegalArgumentException("Cannot create JWT Token without username");
    	
    	if (userContext.getAuthorities() == null || userContext.getAuthorities().isEmpty()) 
    		throw new IllegalArgumentException("User doesn't have any privileges");
    	
    	Claims claims = Jwts.claims().setSubject(userContext.getUsername());
    	claims.put("scopes", userContext.getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toList()));
    	
    	LocalDateTime currentTime = LocalDateTime.now();
    	
    	String token = Jwts.builder()
    			.setClaims(claims)
    			.setIssuer(settings.getTokenIssuer())
    			.setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
    			.setExpiration(Date.from(currentTime
    					.plusMinutes(expireTime)
    					.atZone(ZoneId.systemDefault()).toInstant()))
    			.signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
    			.compact();
    	
    	return new AccessJwtToken(token, claims);
    }

    public JwtToken createRefreshToken(I2CHUserContext userContext) {
        return createRefreshToken(userContext, settings.getRefreshTokenExpTime());
    }
    
    public JwtToken createRefreshToken(I2CHUserContext userContext, int ExpiredTime) {
    	if (StringUtils.isEmpty(userContext.getUsername())) {
    		throw new IllegalArgumentException("Cannot create JWT Token without username");
    	}
    	
    	LocalDateTime currentTime = LocalDateTime.now();
    	
    	Claims claims = Jwts.claims().setSubject(userContext.getUsername());
    	claims.put("scopes", Arrays.asList(Scopes.REFRESH_TOKEN.authority()));
    	
    	String token = Jwts.builder()
    			.setClaims(claims)
    			.setIssuer(settings.getTokenIssuer())
    			.setId(UUID.randomUUID().toString())
    			.setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
    			.setExpiration(Date.from(currentTime
    					.plusMinutes(ExpiredTime)
    					.atZone(ZoneId.systemDefault()).toInstant()))
    			.signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
    			.compact();
    	
    	return new AccessJwtToken(token, claims);
    }
}
