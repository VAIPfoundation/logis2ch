package com.sdc2ch.tms.enums;


public enum DeliveryType {
	A("일반"), B("학교"), C("군"), D("직판"),

	FFFF("알수없음");

	private String nm;

	DeliveryType(String nm) {
		this.nm = nm;
	}

	public String getNm() {
		return nm;
	}
}
