package com.sdc2ch.api.req;

import org.springframework.util.StringUtils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchReq {
	
	
	@ApiModelProperty(dataType = "string", value = "공장코드", required = true)
	private String fctryCd;
	
	@ApiModelProperty(dataType = "string", value = "일자")
	private String dlvyDe;
	
	@ApiModelProperty(dataType = "string", value = "시작일")
	private String fromDe;
	
	@ApiModelProperty(dataType = "string", value = "종료일")
	private String toDe;
	
	@ApiModelProperty(dataType = "string", value = "배차유형")
	private String carAlcTy;
	
	@ApiModelProperty(dataType = "string", value = "노선번호")
	private String routeNo;
	
	@ApiModelProperty(dataType = "string", value = "차량번호")
	private String vrn;
	
	
	
	public String getDlvyDe() {
		return convertDate(this.dlvyDe);
	}
	public String getFromDe() {
		return convertDate(this.fromDe);
	}
	public String getToDe() {
		return convertDate(this.toDe);
	}
	public String convertDate(String date) {
		if(!StringUtils.isEmpty(date)) {
			date = date.replaceAll("-", "");
		}
		return date;
	}
	
}
