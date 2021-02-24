package com.sdc2ch.service.mobile.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class MobileHaveOffRouteInfoVo {

	@ApiModelProperty(dataType = "string", value = "공장코드")
	private String centerCd;
	@ApiModelProperty(dataType = "string", value = "노선번호")
	private String routeNo;
	@ApiModelProperty(dataType = "string", value = "시작시간")
	private String startTm;
}
