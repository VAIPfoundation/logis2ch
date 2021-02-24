package com.sdc2ch.service.mobile.model;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class MobileSanitationListInfoVo {
	@ApiModelProperty(dataType = "bigdecimal", value = "아이디")
	private BigDecimal id;
	@ApiModelProperty(dataType = "string", value = "작성일자")
	private String regDe;
	@ApiModelProperty(dataType = "string", value = "아이템01")
	private String item01;
	@ApiModelProperty(dataType = "string", value = "아이템02")
	private String item02;
	@ApiModelProperty(dataType = "string", value = "아이템03")
	private String item03;
	@ApiModelProperty(dataType = "string", value = "아이템04")
	private String item04;
	@ApiModelProperty(dataType = "string", value = "아이템05")
	private String item05;
	@ApiModelProperty(dataType = "string", value = "아이템06")
	private String item06;
	@ApiModelProperty(dataType = "string", value = "아이템07")
	private String item07;
	@ApiModelProperty(dataType = "string", value = "아이템08")
	private String item08;
	@ApiModelProperty(dataType = "string", value = "아이템09")
	private String item09;
	@ApiModelProperty(dataType = "string", value = "아이템10")
	private String item10;
	@ApiModelProperty(dataType = "string", value = "차량번호")
	private String carCd;
	
}
