package com.sdc2ch.web.admin.req;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalsDlvyStdTimeReq {
	@ApiModelProperty(dataType = "long", value = "ID", required=false)
	protected Long id;

	@ApiModelProperty(dataType = "string", value = "공장코드", required=true)
	@NotEmpty
	private String fctryCd;

	@ApiModelProperty(dataType = "String", value = "배차유형", required=true)
	private String caralcTy;

	@ApiModelProperty(dataType = "String", value = "차종(톤수)", required=true)
	private String vhcleTy;

	@ApiModelProperty(dataType = "String", value = "노선번호", required=false)
	@NotEmpty
	private String routeNo;

	@ApiModelProperty(dataType = "Long", value = "기준시간(Min)", required=true)
	@NotNull
	private Long stdTime;

	@ApiModelProperty(dataType = "Long", value = "조정시간(Min)", required=true)
	@NotNull
	private Long adjustTime;

	@ApiModelProperty(dataType = "boolean", value = "사용여부", required=true)
	@NotEmpty
	private boolean useYn;

	@ApiModelProperty(dataType = "boolean", value = "기본값여부")
	private boolean base;
}
