package com.sdc2ch.service.admin.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class LdngTimeVo {

	@ApiModelProperty(dataType = "long", value = "id")
	private Long id;

	@ApiModelProperty(dataType = "string", value = "배송일자")
	private String dlvyDe;

	@ApiModelProperty(dataType = "string", value = "배차유형")
	private String caralcTy;
	@ApiModelProperty(dataType = "string", value = "차종")
	private String vhcleTy;
	@ApiModelProperty(dataType = "string", value = "평균상차시간")
	private String avgLdngTime;
	@ApiModelProperty(dataType = "string", value = "기준시간")
	private String stdTime;
	@ApiModelProperty(dataType = "string", value = "A")
	private String gradA;
	@ApiModelProperty(dataType = "string", value = "B")
	private String gradB;
	@ApiModelProperty(dataType = "string", value = "C")
	private String gradC;
	@ApiModelProperty(dataType = "string", value = "수정일시")
	private String updtDt;
	@ApiModelProperty(dataType = "string", value = "수정시간")
	private String updtTime;
	@ApiModelProperty(dataType = "string", value = "수정자")
	private String updtUserId;
	@ApiModelProperty(dataType = "string", value = "등급 시간 영역")
	private String gradTimeDesc;

	@ApiModelProperty(dataType = "string", value = "건수")
	private String cnt;
	@ApiModelProperty(dataType = "string", value = "퍼센테이지")
	private String pt;
	@ApiModelProperty(dataType = "string", value = "기존 기준시간 대비 증감")
	private String upDownTmplat;
	@ApiModelProperty(dataType = "string", value = "상차소요시간")
	private String ldngReqreTime;
	@ApiModelProperty(dataType = "string", value = "등급")
	private String grad;
	@ApiModelProperty(dataType = "string", value = "차량번호")
	private String vrn;
	@ApiModelProperty(dataType = "string", value = "기사명")
	private String driverNm;

}