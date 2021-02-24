package com.sdc2ch.legacy.endpoint.response;

public class RefreshTokenResponse {
	
    private boolean success;
    private int code;
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
}
