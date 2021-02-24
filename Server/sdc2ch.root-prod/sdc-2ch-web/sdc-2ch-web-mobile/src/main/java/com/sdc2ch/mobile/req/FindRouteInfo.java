package com.sdc2ch.mobile.req;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindRouteInfo {

	@ApiModelProperty(dataType = "long", value = "배차ID")
	private Long id;

	@NotNull
	@ApiModelProperty(dataType = "String", value = "차량번호")
	private String carCd;

	@ApiModelProperty(dataType = "String", value = "공장코드")
	private String centerCd;

	@ApiModelProperty(dataType = "String", value = "배송일자")
	private String dlvyDe;

	@ApiModelProperty(dataType = "String", value = "노선번호")

	private String routeNo;
}
