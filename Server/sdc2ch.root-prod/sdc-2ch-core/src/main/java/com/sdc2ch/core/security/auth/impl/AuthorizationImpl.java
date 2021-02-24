package com.sdc2ch.core.security.auth.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.sdc2ch.core.security.auth.I2CHAuthorization;
import com.sdc2ch.core.security.auth.I2CHUserContext;

 
@Component
class AuthorizationImpl implements I2CHAuthorization {
	
	private final AnonymousAuthorizationImpl anonymous = new AnonymousAuthorizationImpl();

	@Override
	public I2CHUserContext userContext() {
		I2CHAuthorization auth = findAuth();
		return auth.userContext();
	}

	@Override
	public List<GrantedAuthority> authorities() {
		I2CHAuthorization auth = findAuth();
		return auth.authorities();
	}
	
	@Override
	public int hashCode() {
		return anonymous.ctx.hashCode();
	}
	
	private I2CHAuthorization findAuth() {
		
		Object auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(!(auth != null && auth instanceof I2CHAuthorization)) {
			auth = anonymous;
		}
		
		

		return (I2CHAuthorization) auth;
	}
	
	private class AnonymousAuthorizationImpl implements I2CHAuthorization {
		
		private AnonymousContext ctx = new AnonymousContext();

		@Override
		public I2CHUserContext userContext() {
			return ctx;
		}

		@Override
		public List<GrantedAuthority> authorities() {
			return ctx.getAuthorities();
		}
		private class AnonymousContext implements I2CHUserContext {

			private int hashCode = new Random().nextInt();
			@Override
			public String getUsername() {
				return "anonymous user";
			}

			@Override
			public List<GrantedAuthority> getAuthorities() {
				return Arrays.asList("ANONYMOUS").stream().map(s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList());
			}
			
			@Override
			public int hashCode() {
				return hashCode;
			}
			
		}
	}
}
