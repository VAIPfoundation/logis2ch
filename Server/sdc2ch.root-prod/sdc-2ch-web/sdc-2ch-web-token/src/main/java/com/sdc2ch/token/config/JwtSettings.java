package com.sdc2ch.token.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.sdc2ch.token.jwt.domain.JwtToken;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "demo.security.jwt")
@PropertySource(value = {"classpath:jwt-dev.properties"})
public class JwtSettings {
	
    public static final String AUTHENTICATION_HEADER_NAME = "Authorization";
    
    
    private Integer tokenExpirationTime;

    
    private String tokenIssuer;
    
    
    private String tokenSigningKey;
    
    
    private Integer refreshTokenExpTime;
    
}
