package com.sdc2ch.service.common.model.alarm;

import com.sdc2ch.web.admin.repo.domain.alloc.type.AlarmType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ChkResultDto {
	private AlarmType alarmTy;
	private String fctryCd;
	private String dlvyDe;
	private String vrn;
	private String routeNo;
	private String driverNm;
	private String mobileNo;
	private String chkBaseDt;
	private String chkDt;
	private Long overTime;
	private String rm; 
}
