package com.sdc2ch.service.mobile.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class MobileWashCarInfoVo {
	
	@ApiModelProperty(dataType = "string", value = "세차일자")
	private String washDe;
	@ApiModelProperty(dataType = "string", value = "세차장")
	private String wcomCd;
	@ApiModelProperty(dataType = "string", value = "차량번호")
	private String carCd;
	@ApiModelProperty(dataType = "string", value = "처리상태")
	private String procState;
	@ApiModelProperty(dataType = "string", value = "등록자")
	private String regUserId;
	@ApiModelProperty(dataType = "string", value = "등록일시")
	private String regDateTime;
}
