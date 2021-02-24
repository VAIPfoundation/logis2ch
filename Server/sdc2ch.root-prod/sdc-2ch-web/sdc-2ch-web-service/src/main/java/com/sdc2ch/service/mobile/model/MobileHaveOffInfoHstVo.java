package com.sdc2ch.service.mobile.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class MobileHaveOffInfoHstVo {

	@ApiModelProperty(dataType = "string", value = "차량번호")
	private String carCd;
	@ApiModelProperty(dataType = "string", value = "대상일자(yyyyMMdd)")
	private String targetDate;
	@ApiModelProperty(dataType = "integer", value = "휴무신청_RowID")
	private Integer schId;
	@ApiModelProperty(dataType = "string", value = "휴무신청_승인여부")
	private String schConfirm;
	@ApiModelProperty(dataType = "string", value = "휴무신청_휴무시작일")
	private String schStartDate;
	@ApiModelProperty(dataType = "string", value = "휴무신청_휴무종료일(휴무시작일+1)")
	private String schEndDate;
	@ApiModelProperty(dataType = "string", value = "휴무신청_휴무타입")
	private String schType;
	@ApiModelProperty(dataType = "string", value = "휴무신청_휴무일수")
	private String schUnit;
	@ApiModelProperty(dataType = "string", value = "휴무신청_선택_RouteNo")
	private String schRouteNo;
	@ApiModelProperty(dataType = "string", value = "휴무신청_비고")
	private String schBigo;
	@ApiModelProperty(dataType = "string", value = "휴무신청_등록ID")
	private String schRegUserId;
	@ApiModelProperty(dataType = "string", value = "휴무신청_등록일자")
	private String schRegDateTime;
	@ApiModelProperty(dataType = "string", value = "휴무신청_Desc")
	private String schDayoffDesc;

	@ApiModelProperty(dataType = "integer", value = "휴무확정_RowId")
	private Integer exeId;
	@ApiModelProperty(dataType = "string", value = "휴무확정_휴무일자")
	private String exeDayoffDate;
	@ApiModelProperty(dataType = "string", value = "휴무확정_휴무타입")
	private String exeType;
	@ApiModelProperty(dataType = "string", value = "휴무확정_휴무일수")
	private String exeUnit;
	@ApiModelProperty(dataType = "string", value = "휴무확정_비고")
	private String exeBigo;
	@ApiModelProperty(dataType = "string", value = "휴무확정_등록자Id")
	private String exeRegUserId;
	@ApiModelProperty(dataType = "string", value = "휴무확정_등록일자")
	private String exeRegDateTime;
	@ApiModelProperty(dataType = "string", value = "휴무확정_Desc")
	private String exeDayoffDesc;
	
	
}
