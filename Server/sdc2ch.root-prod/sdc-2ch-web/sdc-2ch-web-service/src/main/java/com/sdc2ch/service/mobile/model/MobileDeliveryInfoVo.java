package com.sdc2ch.service.mobile.model;

import com.sdc2ch.core.PointType;
import com.sdc2ch.core.domain.Location;
import com.sdc2ch.prcss.eb.vo.EmptyboxVo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MobileDeliveryInfoVo {
	@ApiModelProperty(dataType = "String", value = "배송시각")
	private String dlvyTime;
	@ApiModelProperty(dataType = "long", value = "배송지 고유번호")
	private Long id;
	@ApiModelProperty(dataType = "String", value = "배송지명")
	private String name;
	@ApiModelProperty(dataType = "String", value = "배송지타입")
	private PointType pointType;
	@ApiModelProperty(dataType = "Location", value = "위치정보")
	private Location location;
	@ApiModelProperty(dataType = "String", value = "주소")
	private String addr;
	@ApiModelProperty(dataType = "String", value = "노선번호")
	private String routeNo;
	@ApiModelProperty(dataType = "String", value = "공장코드")
	private String fctryCd;
	@ApiModelProperty(dataType = "EmptyboxVo", value = "공상자정보")
	private EmptyboxVo emptyboxInfo;
	
	@ApiModelProperty(dataType = "Boolean", value = "공장도착 확정여부")
	private boolean confFctryArrive;
	@ApiModelProperty(dataType = "Boolean", value = "공장출발 확정여부")
	private boolean confFctryDepart;
}
