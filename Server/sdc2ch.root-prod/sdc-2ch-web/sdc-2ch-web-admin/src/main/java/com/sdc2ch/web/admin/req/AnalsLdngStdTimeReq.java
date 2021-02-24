package com.sdc2ch.web.admin.req;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalsLdngStdTimeReq {
	@ApiModelProperty(dataType = "long", value = "ID")
	protected Long id;

	@ApiModelProperty(dataType = "string", value = "공장코드")
	@NotEmpty
	private String fctryCd;

	@ApiModelProperty(dataType = "String", value = "배차유형")
	@NotEmpty
	private String caralcTy;

	@ApiModelProperty(dataType = "String", value = "차종(톤수)")
	@NotEmpty
	private String vhcleTy;

	@ApiModelProperty(dataType = "String", value = "노선번호")
	private String routeNo;

	@ApiModelProperty(dataType = "Long", value = "기준시간(Min)")
	@NotNull
	private Long stdTime;

	@ApiModelProperty(dataType = "Long", value = "조정시간(Min)")
	private Long adjustTime;

	@ApiModelProperty(dataType = "boolean", value = "사용여부")
	@NotEmpty
	private boolean useYn;

	@ApiModelProperty(dataType = "boolean", value = "기본값여부")
	private boolean base;
}
