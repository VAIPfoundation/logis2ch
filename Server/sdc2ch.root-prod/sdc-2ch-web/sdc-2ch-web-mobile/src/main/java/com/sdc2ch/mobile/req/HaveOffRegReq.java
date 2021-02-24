package com.sdc2ch.mobile.req;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HaveOffRegReq {
	
	@ApiModelProperty(dataType = "integer", value = "휴가등록 고유번호")
	private Integer id;
	
	
	@ApiModelProperty(dataType = "String", value = "소속공장")
	private String fctryCd;
	
	@Size(min = 8, max = 8)
	@ApiModelProperty(dataType = "String", value = "휴무시작일자(yyyyMMdd)")
	private String startDate;
	
	@Size(min = 8, max = 8)
	@ApiModelProperty(dataType = "String", value = "휴무종료일자(yyyyMMdd)")
	private String endDate;
	
	@ApiModelProperty(dataType = "String", value = "차량번호")
	private String carCd;
	
	@ApiModelProperty(dataType = "String", value = "유형")
	private String type;
	
	@ApiModelProperty(dataType = "String", value = "내용")
	private String reason;
	
	@ApiModelProperty(dataType = "String", value = "일수")
	private String unit;
	
	@ApiModelProperty(dataType = "String", value = "운행노선")
	private String routeNo;
	
	@ApiModelProperty(dataType = "String", value = "비고")
	private String bigo;
	
}
