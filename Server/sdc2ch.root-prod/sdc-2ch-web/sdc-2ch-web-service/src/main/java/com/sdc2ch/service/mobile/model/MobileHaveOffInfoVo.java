package com.sdc2ch.service.mobile.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class MobileHaveOffInfoVo {
	
	@ApiModelProperty(dataType = "integer", value = "키ID")
	private Integer id;
	
	@ApiModelProperty(dataType = "string", value = "승인여부")
	private String confirm;
	@ApiModelProperty(dataType = "string", value = "차량번호")
	private String carCd;
	@ApiModelProperty(dataType = "string", value = "시작일자")
	private String startDate;
	@ApiModelProperty(dataType = "string", value = "종료일자")
	private String endDate;
	@ApiModelProperty(dataType = "string", value = "유형")
	private String type;
	@ApiModelProperty(dataType = "string", value = "휴무내용")
	private String reason;
	@ApiModelProperty(dataType = "string", value = "휴무일수")
	private String unit;
	@ApiModelProperty(dataType = "string", value = "운행노선")
	private String routeNo;
	@ApiModelProperty(dataType = "string", value = "비고")
	private String bigo;
	
	@ApiModelProperty(dataType = "string", value = "등록자")
	private String regUserId;
	@ApiModelProperty(dataType = "string", value = "등록일시")
	private String regDateTime;
	
	@ApiModelProperty(dataType = "integer", value = "결과코드")
	private Integer retCd;
	@ApiModelProperty(dataType = "string", value = "결과값")
	private String retMessage;
}
