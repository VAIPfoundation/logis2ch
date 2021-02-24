package com.sdc2ch.core.security.auth.error;

import com.fasterxml.jackson.annotation.JsonValue;


public enum ErrorCode {
	INTERNAL_SERVER_ERROR(1, "서버내부예외"),
    GLOBAL(2, "정의되지 않은 예외"),
    DAMAAGED_TOKEN(3, "훼손된토큰"),
    INVALID_REQUEST(4, "잘못된 요청"),
    NOT_FOUND(5, "요청값의 정보가 서버에 없음"),
    VERIFY_FAIL(6, "인증실패"),
    JSON_PARSE_ERROR(7, "잘못된 Json 데이터 파싱 오류"),
    INVALID_DATA(8, "요청값에 유효하지않은 값이 포함됨"),
    INVALID_TOKEN(9, "유효하지않은토큰"),
    AUTHENTICATION(10, "권한없음"), 
    JWT_TOKEN_EXPIRED(11, "토큰만료"),
	REFLASH_TOKEN_EXPIRED(12, "갱신토큰만료");
    
    private int errorCode;

    private String message;
    private ErrorCode(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    @JsonValue
    public int getErrorCode() {
        return errorCode;
    }
	public String getMessage() {
		return message;
	}

}
