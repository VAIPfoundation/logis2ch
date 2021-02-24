package com.sdc2ch.nfc.enums;

import java.util.Arrays;

 
public enum NfcEventType {

	
	VERIFY_FAIL_ID(4353, "인증 실패(ID)"),

	
	VERIFY_FAIL_PIN(4355, "인증 실패(PIN)"),

	
	VERIFY_SUCCESS_ID_PIN(4097, "인증 성공(ID + PIN)"),


	
	VERIFY_FAIL_CARD(4354, "인증 실패(카드)"),
	
	VERIFY_SUCCESS_CARD(4102, "인증 성공(카드)"),

	
	IDENTITY_FAIL_FINGERPRINT(5124, "인증 실패(지문)"),
	
	IDENTITY_SUCCESS_FINGERPRINT(4865, "인증 성공(지문)"),

	
	AUTH_FAILED_INVALID_CREDENTIAL(6146, "잘못된 크리덴셜"),

	
	DEFAULT_EVENT_CODE(0, "알수없음")
	;
	private int code;
	private String desc;
	NfcEventType(int code, String desc){
		this.code = code;
		this.desc = desc;

	}

	public int getEventCode() {
		return code;
	}
	public String getDesc() {
		return desc;
	}

	public boolean contains(final int eventCode) {
		return Arrays.asList(NfcEventType.values()).stream().filter(net -> net.code == eventCode).count() > 0;
	}

	public static NfcEventType valueOf(final int code) {
		return Arrays.asList(NfcEventType.values()).stream().filter(ne -> ne.code == code).findAny().get();
	}

}
