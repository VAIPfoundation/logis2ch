package com.sdc2ch.web.admin.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SearchCaralcReq extends SearchReq {

	@ApiModelProperty(dataType = "string", value = "차종")
	private String vhcleTy;

	@ApiModelProperty(dataType = "string", value = "지연여부")
	private String delayYn;

	@ApiModelProperty(dataType = "string", value = "배차여부")
	private String caralcYn;

	@ApiModelProperty(dataType = "string", value = "출발지명")
	private String fromLcNm;

	@ApiModelProperty(dataType = "string", value = "도착지명")
	private String toLcNm;

	@ApiModelProperty(dataType = "string", value = "키워드")
	private String keyword;

	@ApiModelProperty(dataType = "string", value = "시작시간")
	private String fromTime;

	@ApiModelProperty(dataType = "string", value = "종료시간")
	private String toTime;

	@ApiModelProperty(dataType = "string", value = "태깅미실시여부")
	private String noTag;

}
