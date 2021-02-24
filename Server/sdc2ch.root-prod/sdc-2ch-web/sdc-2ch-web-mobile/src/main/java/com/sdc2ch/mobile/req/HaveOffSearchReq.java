package com.sdc2ch.mobile.req;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HaveOffSearchReq {
	
	@NotNull
	@ApiModelProperty(dataType = "String", value = "소속공장")
	private String fctryCd;
	@NotNull
	@Size(min = 6, max = 6)
	@ApiModelProperty(dataType = "String", value = "휴무월(yyyyMM)")
	private String searchMonth;
	@NotNull
	@ApiModelProperty(dataType = "String", value = "차량번호")
	private String carCd;
}
