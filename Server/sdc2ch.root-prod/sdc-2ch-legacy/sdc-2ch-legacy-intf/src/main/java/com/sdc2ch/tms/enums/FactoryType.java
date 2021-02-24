package com.sdc2ch.tms.enums;

import java.util.stream.Stream;


public enum FactoryType {
	
	F1D1("1D1", "양주"),
	F2D1("2D1", "용인"),
	F3D1("3D1", "안산"),
	F4D1("4D1", "거창"),
	F5D1("5D1", "양주신공장"),
	
	FFFF("FFFF", "FFFF")
	;
	
	private String code;
	private String name;
	
	FactoryType(String code, String name){
		this.code = code;
		this.name = name;
	}
	
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	
	public static FactoryType convert(String fctryCd) {
		return Stream.of(FactoryType.values()).filter(f -> f.getCode().equals(fctryCd)).findFirst().orElse(FFFF);
	}

}
