package com.sdc2ch.mobile.enums;

import lombok.Getter;

@Getter
public enum VerifyEnums {
	
	AUTH_TOKEN("인증토큰"), TOS("약관동의"), USER("인가사용자"), ACL("접근권한"), APP_VER("앱버전"), APP_TOKEN("앱토큰"), PUSH_TOKEN("푸시토큰");
	
	private String nm;
	
	VerifyEnums(String nm){
		this.nm = nm;
	}
	
	
}
