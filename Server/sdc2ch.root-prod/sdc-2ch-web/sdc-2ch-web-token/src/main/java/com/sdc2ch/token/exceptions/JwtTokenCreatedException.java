package com.sdc2ch.token.exceptions;

public class JwtTokenCreatedException extends Exception {
	private static final long serialVersionUID = -4240908794948848150L;

	private static final String MESSAGE = "시스템에 접근권한이 있는 사용자는 영구적으로 사용가능한 토큰발급을 제한합니다.";
	public JwtTokenCreatedException() {
		super(MESSAGE);
	}
	public JwtTokenCreatedException(String message) {
		super(message);
	}
}
