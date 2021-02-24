package com.sdc2ch.api.enums;

public enum CustomerScenario {
	C_NO_REGIST("서울우유 투채널 고객센터 서비스입니다. 죄송합니다. 고객님의 고객센터 정보를 찾을 수 없습니다. 서울우유 담당 물류팀으로 문의바랍니다."),

	C_WELCOME_CALL("반갑습니다. 서울우유 투채널 고객센터 서비스입니다."),
	C_FCTRY_CALL("%s 물류팀으로 연결합니다. 전화가 연결되면 통화내용이 녹음됩니다 잠시만 기다려주세요 전화연결이 안되면 잠시후 다시 걸어주시기 바랍니다"),
	C_DRIVER_CALL("%s 배송기사 전화로 연결합니다. 전화가 연결되면 통화내용이 녹음됩니다 잠시만 기다려주세요 전화연결이 안되면 잠시후 다시 걸어주시기 바랍니다"),
	C_RECVE_CALL("안녕하세요. %s 고객센터에서 걸려온 전화입니다. 전화가 연결되면 통화내용이 녹음됩니다."),
	C_GATHER_TIMEOUT_CALL("입력시간이 초과되었습니다."),
	C_GATHER_HANGUP_CALL("다음에 다시걸어주세요."),
	C_GATHER_CALL("배송기사 연결은 1번, 공장물류팀 연결은 2번을 눌러주세요.");

	;

	private String message;
	CustomerScenario(String message){
		this.message = message;
	}
	public String getMessage() {
		return message;
	}

}
