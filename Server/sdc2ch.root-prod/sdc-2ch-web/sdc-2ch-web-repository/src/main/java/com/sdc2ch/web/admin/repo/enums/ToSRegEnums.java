package com.sdc2ch.web.admin.repo.enums;

public enum ToSRegEnums {
	PRIVATE("개인정보"),
	LOCATION("위치정보")
	;
	public String tosTyNm;
	ToSRegEnums(String name){
		this.tosTyNm  = name;
	}
}
