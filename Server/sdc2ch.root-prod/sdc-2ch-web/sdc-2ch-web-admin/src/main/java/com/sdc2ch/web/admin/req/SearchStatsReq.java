package com.sdc2ch.web.admin.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchStatsReq extends SearchReq {
	
	@ApiModelProperty(dataType = "string", value = "상차등급")
	private String ldngGrad;
	
	@ApiModelProperty(dataType = "string", value = "도착등급")
	private String arvlGrad;
}
