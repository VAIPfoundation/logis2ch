package com.sdc2ch.mobile.res;

import org.springframework.hateoas.ResourceSupport;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName="of")
public class Me extends ResourceSupport {
	
	@ApiModelProperty(dataType = "string", value = "사용자명")
	private final String userNm;
	@ApiModelProperty(dataType = "string", value = "소속공장")
	private final String fctryCd;
	@ApiModelProperty(dataType = "string", value = "차량번호")
	private final String vrn;

}
