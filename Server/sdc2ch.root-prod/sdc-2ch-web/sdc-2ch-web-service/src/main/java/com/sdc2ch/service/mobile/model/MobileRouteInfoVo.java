package com.sdc2ch.service.mobile.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class MobileRouteInfoVo {

	@ApiModelProperty(dataType = "string", value = "노선번호")
	private String routeNo;
	@ApiModelProperty(dataType = "integer", value = "회차")
	private Integer rotationNo; 
	@ApiModelProperty(dataType = "integer", value = "도크순번")
	private Integer dockSerialNo;
	@ApiModelProperty(dataType = "string", value = "차량톤수")
	private BigDecimal carTon;
	@ApiModelProperty(dataType = "string", value = "상차시작시각")
	private String loadingStartTime;
	@ApiModelProperty(dataType = "string", value = "상차종료시각")
	private String loadingEndTime;
	@ApiModelProperty(dataType = "Array", value = "배송지정보")
	private List<MobileDeliveryInfoVo> deliveryInfos;
	@ApiModelProperty(dataType = "string", value =  "확정회전")
	private String confRtateRate;
	@ApiModelProperty(dataType = "string", value =  "확정거리")
	private String confDistance;
	@ApiModelProperty(dataType = "string", value =  "확정톨비")
	private String confTollCost;
	@ApiModelProperty(dataType = "string", value =  "확정주유량")
	private String carOil;
	@ApiModelProperty(dataType = "Boolean", value = "배송여부 true = 배송")
	private boolean dlvyTy;
	@JsonIgnore
	private LocalDateTime time;
	
	@ApiModelProperty(dataType = "Boolean", value = "공장도착 확정여부")
	private boolean confFctryArrive;
	@ApiModelProperty(dataType = "Boolean", value = "공장출발 확정여부")
	private boolean confFctryDepart;

}
