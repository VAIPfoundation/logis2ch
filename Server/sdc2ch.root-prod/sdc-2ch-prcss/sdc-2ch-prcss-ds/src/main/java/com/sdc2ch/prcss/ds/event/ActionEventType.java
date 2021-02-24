package com.sdc2ch.prcss.ds.event;

import java.util.stream.Stream;

public enum ActionEventType {
	MGR_ALLOCATED("[서울우유2CH] %s 차량의 배차가 확정되었습니다."),
	MGR_CANCEL   ("[서울우유2CH] %s 차량의 배차가 취소되었습니다."),
	GEO_ARRIVED  (null),
	GEO_ENTER    (null),
	GEO_EXITED   (null),
	USR_ST       ("[서울우유2CH] %s 운행이 시작되었습니다."),
	USR_FIN      ("[서울우유2CH] %s 운행이 종료되었습니다."),
	USR_CONFIRM  ("[서울우유2CH] %s 차량의 배차가 확정되었습니다."),
	USR_EB       ("[서울우유2CH] %s 차량의 %s고객센터 공상자 입력이 완료되었습니다."),
	NFC_TAG_LDNG ("[서울우유2CH] %s 차량의 상차가 확인되었습니다."),
	NFC_TAG_OFFIC("[서울우유2CH] %s 차량의 출근이 확인되었습니다."),
	BCN_ENTER    ("[서울우유2CH] %s 차량의 공장 도착이 확인되었습니다."),
	BCN_EXITED   ("[서울우유2CH] %s 차량의 공장 출발이 확인되었습니다."),
	SYS_FIN      ("[서울우유2CH] %s 운행이 자동 종료되었습니다."),
	USR_ENTER    ("[서울우유2CH] %s 차량의 공장 도착이 입력되었습니다."),
	USR_EXITED   ("[서울우유2CH] %s 차량의 공장 출발이 입력되었습니다."),
	UNKNOWN      (null),
	;
	public String message;
	ActionEventType(String msg){
		message = msg;
	}
	public static ActionEventType convert(String name) {
		return Stream.of(ActionEventType.values()).filter(a -> a.name().equals(name)).findFirst().orElse(UNKNOWN);
	}
}
