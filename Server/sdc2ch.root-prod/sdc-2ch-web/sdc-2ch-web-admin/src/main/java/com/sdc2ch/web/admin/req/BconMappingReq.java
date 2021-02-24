package com.sdc2ch.web.admin.req;

import javax.validation.constraints.NotBlank;

import com.sdc2ch.require.enums.SetupLcType;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BconMappingReq {

	@ApiModelProperty(dataType = "long", value = "id", required=false)
	private Long Id;
	@ApiModelProperty(dataType = "SetupLcType", value = "설치 위치 코드", required=true)
	@NotBlank
	private SetupLcType setupLc;
	@ApiModelProperty(dataType = "String", value = "비콘번호", required=true)
	@NotBlank
	private String bconId;
	@ApiModelProperty(dataType = "String", value = "비콘이름", required=true)
	@NotBlank
	private String bconName;
	@ApiModelProperty(dataType = "String", value = "공장 코드", required=true)
	@NotBlank
	private String fctryCd;
}
