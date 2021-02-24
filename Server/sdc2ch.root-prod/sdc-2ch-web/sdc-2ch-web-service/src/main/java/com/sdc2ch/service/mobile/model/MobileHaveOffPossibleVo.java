package com.sdc2ch.service.mobile.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class MobileHaveOffPossibleVo {

	@ApiModelProperty(dataType = "string", value = "타겟차량")
	private String carCd;
	@ApiModelProperty(dataType = "string", value = "타겟일자")
	private String targetDate;
	@ApiModelProperty(dataType = "string", value = "요일")
	private String day;
	@ApiModelProperty(dataType = "integer", value = "신청차량대수")
	private Double schCount;
	@ApiModelProperty(dataType = "integer", value = "최대차량대수")
	private Integer schCountMax;
}
