package com.sdc2ch.web.admin.repo.domain.alloc.type;

public enum AlarmType {

	
	NO_CNFRM_CARALC_DTLS("배차내역 미확인", ""),

	
	NO_START_WORK("업무 시작 미실시", ""),

	
	NO_PASSAGE("미통문", ""),

	
	NO_LOADING("미상차", ""),

	
	ETY_BOX_DCSN_NO_OPRTN("공상자 확정 미실시", "/ %s / %s / %s 공상자 입력바랍니다."),

	
	NO_END_WORK("업무 종료 미실시", ""),

	
	RTNGUD_WRHOUSNG("반품 입고", ""),

	
	GPS_OFF("GPS 꺼짐", "");

	public String alarmTyNm;
	private String pushMsg;

	public String getPushMsg() {
		return this.pushMsg;
	}

	AlarmType(String alarmTyNm, String pushMsg){
		this.alarmTyNm  = alarmTyNm;
		this.pushMsg = pushMsg;
	}

}
