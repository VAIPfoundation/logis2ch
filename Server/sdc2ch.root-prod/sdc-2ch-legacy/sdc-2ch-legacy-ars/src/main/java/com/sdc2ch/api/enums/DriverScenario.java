package com.sdc2ch.api.enums;

public enum DriverScenario {
	D_NO_REGIST("서울우유 투채널 고객센터 서비스입니다. 죄송합니다. 고객님의 정보를 찾을 수 없습니다. 서울우유 담당 물류팀으로 문의바랍니다."),
	D_WELCOME_CALL("반갑습니다. %s 배송기사님 서울우유 투채널 고객센터 서비스입니다."),
	D_FCTRY_CALL("%s 물류팀으로 연결합니다. 전화가 연결되면 통화내용이 녹음됩니다 잠시만 기다려주세요 전화연결이 안되면 잠시후 다시 걸어주시기 바랍니다"),
	D_DRIVER_CALL("%s 고객센터 전화로 연결합니다. 전화가 연결되면 통화내용이 녹음됩니다 잠시만 기다려주세요 전화연결이 안되면 잠시후 다시 걸어주시기 바랍니다"),
	D_RECVE_CALL("안녕하세요. %s 고객센터에서 걸려온 전화입니다. 전화가 연결되면 통화내용이 녹음됩니다."),
	D_GATHER_TIMEOUT_CALL("입력시간이 초과되었습니다."),
	D_GATHER_HANGUP_CALL("다음에 다시걸어주세요."),
	D_GATHER_CALL("고객센터 연결은 1번, 공장물류팀 연결은 2번을 눌러주세요."),
	D_NO_REGIST_CUSTOMER("죄송합니다. 요청하신 고객사의 담당자정보를 찾을 수 없습니다. 담당 물류팀에 문의하여주세요.")

	;

	private String message;
	DriverScenario(String message){
		this.message = message;
	}
	public String getMessage() {
		return message;
	}

}
