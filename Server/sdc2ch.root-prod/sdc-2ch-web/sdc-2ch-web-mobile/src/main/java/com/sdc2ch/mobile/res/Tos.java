package com.sdc2ch.mobile.res;

import com.sdc2ch.web.admin.repo.enums.ToSRegEnums;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Tos {
	
	@ApiModelProperty(dataType = "long", value = "약관ID")
	private final Long tosId;
	@ApiModelProperty(dataType = "string", value = "약관제목")
	private final String title;
	@ApiModelProperty(dataType = "string", value = "약관내용")
	private final String contents;
	@ApiModelProperty(dataType = "enum", value = "약관타입")
	private final ToSRegEnums type;
}
