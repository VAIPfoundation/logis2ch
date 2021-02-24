package com.sdc2ch.service.mobile.model;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MobileCaralcInfVo {
	@ApiModelProperty(dataType = "long", value = "배송고유번호")
	private Long id;
	@ApiModelProperty(dataType = "string", value = "배송일자")
	private String dlvyDe;
	@ApiModelProperty(dataType = "array", value = "회차정보")
	private List<MobileRouteInfoVo> rotations;
	@ApiModelProperty(dataType = "int", value = "상태코드")
	private int resultCd;
	
	@ApiModelProperty(dataType = "boolean", value = "업무시작 확정여부")
	private boolean confStartJob;
	@ApiModelProperty(dataType = "boolean", value = "업무종료 확정여부")
	private boolean confEndJob;
}
