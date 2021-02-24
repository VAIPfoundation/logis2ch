package com.sdc2ch.mobile.req;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WashCarReq {
	
	@ApiModelProperty(dataType = "long", value = "세차등록 고유번호")
	private Long id;
	
	@NotNull
	@Size(min = 8, max = 8)
	@ApiModelProperty(dataType = "String", value = "세차일자")
	private String washDe;
	@NotNull
	@ApiModelProperty(dataType = "String", value = "세차장")
	private String wcomCd;
	@NotNull
	@ApiModelProperty(dataType = "String", value = "차량번호")
	private String carCd;
}
