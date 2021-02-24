package com.sdc2ch.token;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;

import com.sdc2ch.core.security.auth.I2CHUserContext;


public class JwtUserContext implements I2CHUserContext {
    private final String username;
    private final List<GrantedAuthority> authorities;

    private JwtUserContext(String username, List<GrantedAuthority> authorities) {
        this.username = username;
        this.authorities = authorities;
    }
    
    public static JwtUserContext create(String username, List<GrantedAuthority> authorities) {
        if (StringUtils.isEmpty(username)) throw new IllegalArgumentException("Username is blank: " + username);
        return new JwtUserContext(username, authorities);
    }

    public String getUsername() {
        return username;
    }

    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }

	@Override
	public String toString() {
		return "JwtUserContext [username=" + username + ", authorities=" + authorities + "]";
	}
    
}
