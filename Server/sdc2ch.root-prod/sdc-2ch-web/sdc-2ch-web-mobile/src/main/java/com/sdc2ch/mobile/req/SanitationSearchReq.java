package com.sdc2ch.mobile.req;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SanitationSearchReq {
	
	@NotNull
	@ApiModelProperty(dataType = "String", value = "소속공장")
	private String fctryCd;
	@NotNull
	@Size(min = 8, max = 8)
	@ApiModelProperty(dataType = "String", value = "작성일")
	private String regDe;
	
	@NotNull
	@ApiModelProperty(dataType = "String", value = "차량번호")
	private String carCd;
}	
