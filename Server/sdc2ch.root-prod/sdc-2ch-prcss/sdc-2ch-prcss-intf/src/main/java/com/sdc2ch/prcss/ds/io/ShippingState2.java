package com.sdc2ch.prcss.ds.io;

public enum ShippingState2 {

	READY("준비"),
	START("운행시작"),
	ENTER("통문(입)"),
	LOADING("상차시작"),
	EXIT("통문(출)"),
	ENTER2("배송지진입"),
	ARRIVE("배송지도착"),
	TAKEOVER("공상자인계"),
	DEPART("배송지진출"),
	COMPLETE("운행종료"),
	CANCEL("배차취소")
	;
	
	public String state;
	ShippingState2(String state){
		this.state =state;
	}
}
