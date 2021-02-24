package com.sdc2ch.token;

import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.sdc2ch.core.security.auth.I2CHAuthorization;
import com.sdc2ch.core.security.auth.I2CHUserContext;
import com.sdc2ch.token.jwt.domain.RawAccessJwtToken;


public class JwtAuthenticationToken extends AbstractAuthenticationToken implements I2CHAuthorization {
    private static final long serialVersionUID = 2877954820905567501L;

    private RawAccessJwtToken rawAccessToken;
    private I2CHUserContext userContext;

    public JwtAuthenticationToken(RawAccessJwtToken unsafeToken) {
        super(null);
        this.rawAccessToken = unsafeToken;
        this.setAuthenticated(false);
    }

    public JwtAuthenticationToken(I2CHUserContext userContext, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.eraseCredentials();
        this.userContext = userContext;
        super.setAuthenticated(true);
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return rawAccessToken;
    }

    @Override
    public Object getPrincipal() {
        return this.userContext;
    }

    @Override
    public void eraseCredentials() {        
        super.eraseCredentials();
        this.rawAccessToken = null;
    }

	@Override
	public I2CHUserContext userContext() {
		return this.userContext;
	}

	@Override
	public List<GrantedAuthority> authorities() {
		return this.userContext == null ? null : this.userContext.getAuthorities();
	}
}
