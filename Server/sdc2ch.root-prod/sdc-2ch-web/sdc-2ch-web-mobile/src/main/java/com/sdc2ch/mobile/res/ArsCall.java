package com.sdc2ch.mobile.res;

import com.sdc2ch.ars.enums.CallType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArsCall {
	
	private Long id;
	private String dlvyDe;	
	private String routeNo;	
	private String dlvyLcCd;	
	private CallType type;

}
