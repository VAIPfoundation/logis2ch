package com.sdc2ch.token.jwt.domain;

import org.springframework.hateoas.ResourceSupport;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName="of")
public class JwtTokenImpl extends ResourceSupport {
	private final String token;
	private final String refreshToken;
}
