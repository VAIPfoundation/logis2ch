package com.sdc2ch.service.admin.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class AlarmSttusVo {

	@ApiModelProperty(dataType = "long", value = "id")
	private Long id;

	@ApiModelProperty(dataType = "string", value = "배송일자")
	private String dlvyDe;

	@ApiModelProperty(dataType = "string", value = "노선번호")
	private String routeNo;

	@ApiModelProperty(dataType = "string", value = "배차유형")
	private String caralcTy;

	@ApiModelProperty(dataType = "string", value = "차량번호")
	private String vrn;

	@ApiModelProperty(dataType = "string", value = "배차내역미확인 알람건수")
	private String noCnfrmCaralcDtlsAlramCnt;

	@ApiModelProperty(dataType = "string", value = "업무시작미실시 알람건수")
	private String noStartWorkAlramCnt;

	@ApiModelProperty(dataType = "string", value = "미통문 알람건수")
	private String noPassageAlramCnt;

	@ApiModelProperty(dataType = "string", value = "미상차 알람건수")
	private String noLdngAlramCnt;

	@ApiModelProperty(dataType = "string", value = "공장출발 알람건수")
	private String fctryStartAlramCnt;

	@ApiModelProperty(dataType = "string", value = "공상자확정미실시 알람건수")
	private String noEtyBoxDcsnAlramCnt;

	@ApiModelProperty(dataType = "string", value = "업무종료미실시 알람건수")
	private String noEndWorkAlramCnt;

	@ApiModelProperty(dataType = "string", value = "반품입고 알람건수")
	private String rtngudWrhousngAlramCnt;

	@ApiModelProperty(dataType = "string", value = "GPS꺼짐 알람건수")
	private String gpsOffAlramCnt;

}