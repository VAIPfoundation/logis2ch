package com.sdc2ch.web.admin.repo.domain.alloc.type;



public enum TrnsmisType {
	SMS("SMS"),
	PUSH("PUSH"),
	MSNGR_MSG("메신저");
	public String trnsmisTyNm;
	TrnsmisType(String name){
		this.trnsmisTyNm  = name;
	}
}
