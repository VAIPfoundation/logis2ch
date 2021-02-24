package com.sdc2ch.token.domain;

import java.util.List;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName="of")
public class Me extends ResourceSupport {
	
	private final String username;
	private final List<GrantedAuthority> authorities;

}
