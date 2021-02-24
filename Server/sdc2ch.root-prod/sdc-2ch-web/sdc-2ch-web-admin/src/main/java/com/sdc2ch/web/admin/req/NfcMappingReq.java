package com.sdc2ch.web.admin.req;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.sdc2ch.require.enums.SetupLcType;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NfcMappingReq {

	@ApiModelProperty(dataType = "long", value = "id", required=false)
	private Long id;

	@ApiModelProperty(dataType = "String", value = "nfc이름", required=false)
	private String nfcName;

	@ApiModelProperty(dataType = "String", value = "메모/비고", required=false)
	private String rm;

	@ApiModelProperty(dataType = "String", value = "nfc 단말 ID", required=true)
	@NotNull
	private Integer nfcId;

	@ApiModelProperty(dataType = "String", value = "공장코드", required=true)
	@NotBlank
	private String fctryCd;

	@ApiModelProperty(dataType = "String", value = "위치 코드", required=true)
	@NotBlank
	private SetupLcType setupLc;
}
