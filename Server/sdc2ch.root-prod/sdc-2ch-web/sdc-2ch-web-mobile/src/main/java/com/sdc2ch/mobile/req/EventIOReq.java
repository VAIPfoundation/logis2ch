package com.sdc2ch.mobile.req;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventIOReq {
	@NotNull
	@ApiModelProperty(dataType = "long", value = "배차ID")
	private Long id;



	@NotEmpty
	@ApiModelProperty(dataType = "String", value = "배송일자", notes="yyyyMMdd 또는 yyyy-MM-dd")
	private String dlvyDe;
	@NotEmpty
	@ApiModelProperty(dataType = "String", value = "노선번호")
	private String routeNo;
	
	@ApiModelProperty(dataType = "String", value = "공장")
	private String fctryCd;
}
