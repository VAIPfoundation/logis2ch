package com.sdc2ch.mobile.res.start;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor(staticName="of")
public class StartDrive implements MenuDetails { 

	@JsonIgnore
	private final String pgmId;
	@Setter
	private String className;
	
	private final String state;
	
	private final String curDlvylcNm;
	
	private final String curArrivedScheduledTime;
	
	
	private final String nextDlvylcNm;
	
	private final String nextArrivedScheduledTime;

}
