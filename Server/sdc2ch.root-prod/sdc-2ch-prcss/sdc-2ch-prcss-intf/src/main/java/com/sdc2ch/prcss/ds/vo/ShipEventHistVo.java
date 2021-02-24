package com.sdc2ch.prcss.ds.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ShipEventHistVo {

	private Long aclGroupId;
	private String dlvyDe;
	private String routeNo;
	private String vrn;
	private String fctryCd;
	private String driverCd;
	private String MobileNo;
	private String batchNo;
	private String dlvyLcCd;
	private String dlvyLcNm;
}
