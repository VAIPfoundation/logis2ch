package com.sdc2ch.mobile.res;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CarAlcInfRotats {
	
	@ApiModelProperty(dataType = "string", value = "노선번호")
	private String routeNumber;
	@ApiModelProperty(dataType = "integer", value = "회차")
	private int rotationNumber; 
	@ApiModelProperty(dataType = "integer", value = "도크순번")
	private int dockSerialNumber;
	@ApiModelProperty(dataType = "string", value = "차량톤수")
	private float carTon;
	@ApiModelProperty(dataType = "string", value = "상차시작시각")
	private String loadingStartTime;
	@ApiModelProperty(dataType = "string", value = "상차종료시각")
	private String loadingEndTime;
	@ApiModelProperty(dataType = "Array", value = "배송지정보")
	private List<CarAlcInfRotatDtl> details;

}
