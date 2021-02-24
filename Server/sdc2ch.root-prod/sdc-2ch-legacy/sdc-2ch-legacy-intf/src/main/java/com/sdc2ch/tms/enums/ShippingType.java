package com.sdc2ch.tms.enums;


public enum ShippingType {

	
	DELEVERY("배송"),
	
	TRANSPORT("이고");
	
	public String nm;
	ShippingType(String nm){
		this.nm = nm;
	}

}
