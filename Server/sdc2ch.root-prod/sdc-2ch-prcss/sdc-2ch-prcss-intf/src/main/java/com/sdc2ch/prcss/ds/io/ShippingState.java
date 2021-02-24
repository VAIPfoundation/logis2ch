package com.sdc2ch.prcss.ds.io;

public enum ShippingState {

	READY("준비"),
	START("운행시작"),
	ENTER("공장도착"),
	LOADING("상차시작"),
	EXIT("공장출발"),
	ARRIVE("배송지도착"),
	RETURN("회차중"),
	DELIVERY("배송중"),
	TAKEOVER("회수완료"),
	COMPLETE("운행종료"),
	CANCEL("배차취소")
	;
	
	public String state;
	ShippingState(String state){
		this.state =state;
	}
}
