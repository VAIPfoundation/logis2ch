package com.sdc2ch.web.admin.req;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.sdc2ch.web.admin.repo.domain.alloc.type.AlarmSetupType;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AlarmSetupReq {
	@ApiModelProperty(dataType = "long", value = "id", required=false)
	private Long id;

	@ApiModelProperty(dataType = "String", value = "공장코드", required=true)
	@NotBlank
	private String fctryCd;

	@ApiModelProperty(dataType = "setupTy", value = "설정 타입", required=true)
	@NotNull
	private AlarmSetupType setupTy;

	@ApiModelProperty(dataType = "String", value = "설정 값", required=true)
	@NotBlank
	private String value;
}
