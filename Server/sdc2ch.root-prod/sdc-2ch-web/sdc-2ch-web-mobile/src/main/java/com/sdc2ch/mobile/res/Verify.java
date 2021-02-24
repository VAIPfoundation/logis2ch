package com.sdc2ch.mobile.res;

import com.sdc2ch.mobile.enums.VerifyEnums;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Verify {
	












	
	@ApiModelProperty(dataType = "enum", value = "검증타입")
	private VerifyEnums type;
	@ApiModelProperty(dataType = "string", value = "검증타입명")
	private String name;
	@ApiModelProperty(dataType = "boolean", value = "검증결과")
	private boolean verify;
	@ApiModelProperty(dataType = "boolean", value = "예외처리여부")
	private boolean exception;
	@ApiModelProperty(dataType = "string", value = "예외처리 메시지")
	private String message;
}
