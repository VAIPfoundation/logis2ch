package com.sdc2ch.service.admin.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class UnstoringManageVo {

	@ApiModelProperty(dataType = "long", value = "id")
	private Long id;

	@ApiModelProperty(dataType = "string", value = "배송일자")
	private String dlvyDe;

	@ApiModelProperty(dataType = "string", value = "공장코드")
	private String fctryCd;

	@ApiModelProperty(dataType = "string", value = "공장명")
	private String fctryNm;

	@ApiModelProperty(dataType = "string", value = "노선번호")
	private String routeNo;

	@ApiModelProperty(dataType = "string", value = "배차유형")
	private String caralcTy;

	@ApiModelProperty(dataType = "string", value = "차종")
	private String carWeight;

	@ApiModelProperty(dataType = "string", value = "차량번호")
	private String vrn;

	@ApiModelProperty(dataType = "string", value = "근무기간")
	private String workPd;

	@ApiModelProperty(dataType = "string", value = "배송지현황")
	private String dlvyLcSttus;

	@ApiModelProperty(dataType = "string", value = "공장출발예정")
	private String fctryExpcStart;

	@ApiModelProperty(dataType = "string", value = "공장출발시간")
	private String fctryStartTime;

	@ApiModelProperty(dataType = "string", value = "지연시간")
	private String delayTime;

	@ApiModelProperty(dataType = "string", value = "지연사유")
	private String delayResn;

	@ApiModelProperty(dataType = "string", value = "상차계획시간")
	private String ldngExpcTime;

	@ApiModelProperty(dataType = "string", value = "접차시간")
	private String parkngTime;

	@ApiModelProperty(dataType = "string", value = "상차시간")
	private String ldngTime;

	@ApiModelProperty(dataType = "string", value = "차이")
	private String dffrnc;

	@ApiModelProperty(dataType = "string", value = "상차기준시간")
	private String ldngStdTime;

}