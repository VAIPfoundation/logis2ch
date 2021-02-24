package com.sdc2ch.service.admin.model;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class VhcleCntrlVo {

	@ApiModelProperty(dataType = "long", value = "id")
	private Long id;

	@ApiModelProperty(dataType = "string", value = "이벤트일시")
	private Date eventDt;

	@ApiModelProperty(dataType = "string", value = "이벤트명")
	private String eventNm;

	@ApiModelProperty(dataType = "string", value = "TMS 계획시간")
	private Date tmsPlanDt;

}