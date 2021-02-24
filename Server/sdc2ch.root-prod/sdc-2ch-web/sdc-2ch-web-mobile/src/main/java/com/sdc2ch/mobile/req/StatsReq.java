package com.sdc2ch.mobile.req;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatsReq {
	


	
	@NotNull
	@Size(min = 6, max = 6)
	@ApiModelProperty(dataType = "String", value = "용역비게산월")
	private String dlvyDe;
	@NotNull
	@ApiModelProperty(dataType = "String", value = "차량번호")
	private String vrn;
}
