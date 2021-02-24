package com.sdc2ch.mobile.res;

import com.sdc2ch.core.PointType;

import io.swagger.annotations.ApiModelProperty;

public class CarAlcInfRotatDtl {
	@ApiModelProperty(dataType = "String", value = "배송시각")
	private String deliveryTime;
	@ApiModelProperty(dataType = "String", value = "배송지 고유번호")
	private String id;
	@ApiModelProperty(dataType = "String", value = "배송지명")
	private String name;
	@ApiModelProperty(dataType = "String", value = "배송지타입")
	private PointType pointType;
	
	@ApiModelProperty(dataType = "Location", value = "위치정보")
	private Object location;
	

}
