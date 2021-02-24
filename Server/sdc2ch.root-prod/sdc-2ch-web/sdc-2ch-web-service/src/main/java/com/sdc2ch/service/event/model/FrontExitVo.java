package com.sdc2ch.service.event.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FrontExitVo {
	
	private String routeNo;
	private String dlvyDe;
	private String vrn;
	private String fctryCd;
	private String driverCd;
	private LocalDateTime frontExitTime;

}
