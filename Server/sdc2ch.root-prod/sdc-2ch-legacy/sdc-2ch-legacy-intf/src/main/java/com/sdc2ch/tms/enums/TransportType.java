package com.sdc2ch.tms.enums;


public enum TransportType {
	
	FTF("공장간이고"),
	HY("HY:창고"),
	OEM("OEM:밴더"),
	
	FFFF("이고아님")
	
	;
	
	private String codeName;
	
	TransportType(String codeName){
		this.codeName = codeName;
	}
	public String getCodeName() {
		return codeName;
	}
}
