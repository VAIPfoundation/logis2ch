package com.sdc2ch.web.admin.repo.domain.alloc.type;


public enum AlarmSetupType {

	
	FCT_ENTER_LDNG_RANGE("30"),

	
	NO_LDNG_RANGE("30"),
	
	CARALC_NOCNFIRM_RANGE("30"),

	
	CARALC_NOCNFIRM_CYCLE("30"),

	
	GPS_OFF_RANGE("30"),

	
	GPS_OFF_CYCLE("30"),

	
	ETY_BOX_NOCNFIRM_RANGE("30"),

	
	ETY_BOX_NOCNFIRM_CYCLE("30"),

	
	DLVYLC_ARVL_DELAY_RANGE("30"),

	
	WORK_NO_REPORT_RANGE("30"),

	
	CTNU_DRIVE_OVER_RANGE("30"),

	
	CTNU_DRIVE_OVER_CYCLE("30"),

	
	IDLING_RANGE("30"),
	
	ALARM_TARGET_USER("aivision12"),
	
	USE_PUSH("false")
	;
	private String defaultValue;
	public String getDefaultValue() {
		return this.defaultValue;
	}

	AlarmSetupType(String defaultValue){
		this.defaultValue  = defaultValue;
	}
}
