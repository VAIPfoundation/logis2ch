package com.sdc2ch.mobile.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CntrlReq {
	
	@ApiModelProperty(dataType = "string", value = "일자")
	private String dlvyDe;
	
	@ApiModelProperty(dataType = "string", value = "일시")
	private String dlvyDt;
	
	@ApiModelProperty(dataType = "string", value = "차량번호")
	private String vrn;
	
	@ApiModelProperty(dataType = "string", value = "배송지코드")
	private String dlvyLcCd;

}
