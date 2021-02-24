package com.sdc2ch.service.mobile.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class MobileHaveOffSummaryInfoVo {

	@ApiModelProperty(dataType = "double", value = "유급일수_사용")
	private double weekday;
	@ApiModelProperty(dataType = "double", value = "주말휴일일수_사용")
	private double holiday;
	@ApiModelProperty(dataType = "double", value = "결행_사용")
	private double absent;
	@ApiModelProperty(dataType = "double", value = "유급일수_잔여")
	private double weekdayRemain;
	@ApiModelProperty(dataType = "double", value = "주말휴일일수_잔여")
	private double holidayRemain;
	@ApiModelProperty(dataType = "double", value = "결행_잔여")
	private double absentRemain;
}
