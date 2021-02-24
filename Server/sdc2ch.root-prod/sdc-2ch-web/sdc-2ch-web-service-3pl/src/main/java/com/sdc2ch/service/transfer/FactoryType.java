package com.sdc2ch.service.transfer;

import java.util.stream.Stream;

public enum FactoryType {
	
	_1D1("1D1", "양주공장"),
	_2D1("2D1", "용인공장"),
	_3D1("3D1", "안산공장"),
	_4D1("4D1", "거창공장"),
	_5D1("5D1", "양주신공장"),
	FFFF("9D1", "가상공장");
	
	public final String id;
	public final String name;
	FactoryType(String id, String name){
		this.id = id;
		this.name= name;
	}
	
	public static FactoryType valueOfId(String id) {
		return Stream.of(FactoryType.values()).filter(f -> f.id.equals(id)).findFirst().orElse(FFFF);
	}

}
