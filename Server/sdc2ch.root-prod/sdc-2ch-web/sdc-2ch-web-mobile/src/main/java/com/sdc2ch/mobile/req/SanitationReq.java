package com.sdc2ch.mobile.req;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SanitationReq {
	
	@ApiModelProperty(dataType = "long", value = "위생점검 고유번호")
	private Long id;
	@NotNull
	@Size(min = 8, max = 8)
	@ApiModelProperty(dataType = "String", value = "작성일자")
	private String regDe;
	@NotNull
	@ApiModelProperty(dataType = "String", value = "아이템01")
	private String item01;
	@NotNull
	@ApiModelProperty(dataType = "String", value = "아이템02")
	private String item02;
	@NotNull
	@ApiModelProperty(dataType = "String", value = "아이템03")
	private String item03;
	@NotNull
	@ApiModelProperty(dataType = "String", value = "아이템04")
	private String item04;
	@NotNull
	@ApiModelProperty(dataType = "String", value = "아이템05")
	private String item05;
	@NotNull
	@ApiModelProperty(dataType = "String", value = "아이템06")
	private String item06;
	@NotNull
	@ApiModelProperty(dataType = "String", value = "아이템07")
	private String item07;
	@NotNull
	@ApiModelProperty(dataType = "String", value = "아이템08")
	private String item08;
	@NotNull
	@ApiModelProperty(dataType = "String", value = "아이템09")
	private String item09;
	@NotNull
	@ApiModelProperty(dataType = "String", value = "아이템10")
	private String item10;
	@NotNull
	@ApiModelProperty(dataType = "String", value = "기사휴대전화번호")
	private String vrn;
}
