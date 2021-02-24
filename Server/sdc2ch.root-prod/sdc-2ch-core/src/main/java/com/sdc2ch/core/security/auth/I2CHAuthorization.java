package com.sdc2ch.core.security.auth;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;


public interface I2CHAuthorization {
	public I2CHUserContext userContext();
	public List<GrantedAuthority> authorities();
}
