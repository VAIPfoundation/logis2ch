package com.sdc2ch.core.security.auth;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

public interface I2CHUserContext {
	
    public String getUsername();
    public List<GrantedAuthority> getAuthorities();

}
