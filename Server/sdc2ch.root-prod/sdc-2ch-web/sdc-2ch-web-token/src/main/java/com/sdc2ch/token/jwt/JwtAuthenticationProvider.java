package com.sdc2ch.token.jwt;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.sdc2ch.token.JwtAuthenticationToken;
import com.sdc2ch.token.JwtUserContext;
import com.sdc2ch.token.config.JwtSettings;
import com.sdc2ch.token.jwt.domain.JwtToken;
import com.sdc2ch.token.jwt.domain.RawAccessJwtToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

 
@Component
@SuppressWarnings("unchecked")
public class JwtAuthenticationProvider implements AuthenticationProvider {
	private final JwtSettings jwtSettings;

	@Autowired
	public JwtAuthenticationProvider(JwtSettings jwtSettings) {
		this.jwtSettings = jwtSettings;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		RawAccessJwtToken rawAccessToken = (RawAccessJwtToken) authentication.getCredentials();

		Jws<Claims> jwsClaims = rawAccessToken.parseClaims(jwtSettings.getTokenSigningKey());
		String subject = jwsClaims.getBody().getSubject();
		List<String> scopes = jwsClaims.getBody().get("scopes", List.class);
		List<GrantedAuthority> authorities = scopes.stream().map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		JwtUserContext context = JwtUserContext.create(subject, authorities);

		return new JwtAuthenticationToken(context, context.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
	}
	
	public boolean checkExpireToken(RawAccessJwtToken rawAccessToken) {
		
		try {
			Jws<Claims> jwsClaims = rawAccessToken.parseClaims(jwtSettings.getTokenSigningKey());
			String subject = jwsClaims.getBody().getSubject();
			List<String> scopes = jwsClaims.getBody().get("scopes", List.class);
			List<GrantedAuthority> authorities = scopes.stream().map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());

			JwtUserContext context = JwtUserContext.create(subject, authorities);
			return context != null;
			
		}catch (Exception e) {
			
		}
		return false;
	}
}
