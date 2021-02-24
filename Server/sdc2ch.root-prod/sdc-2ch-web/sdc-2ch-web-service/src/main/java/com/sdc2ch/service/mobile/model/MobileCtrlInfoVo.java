package com.sdc2ch.service.mobile.model;

import java.math.BigDecimal;
import java.util.List;

import com.sdc2ch.web.admin.repo.domain.v.V_CARALC_PLAN;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class MobileCtrlInfoVo {
	
	@ApiModelProperty(dataType = "string", value = "배송일자")
	private String dlvyDe;
	@ApiModelProperty(dataType = "string", value = "도착예정시간")
	private String arvlExpcTime; 
	@ApiModelProperty(dataType = "string", value = "고객센터코드")
	private String dlvyLcCd; 
	@ApiModelProperty(dataType = "string", value = "고객센터명")
	private String dlvyLcNm; 
	@ApiModelProperty(dataType = "string", value = "고객센터명")
	private String vrn; 
	@ApiModelProperty(dataType = "string", value = "기사코드")
	private String driverCd; 
	@ApiModelProperty(dataType = "string", value = "기사명")
	private String driverNm; 
	@ApiModelProperty(dataType = "string", value = "주소")
	private String adres; 
	@ApiModelProperty(dataType = "string", value = "위도")
	private BigDecimal lat; 
	@ApiModelProperty(dataType = "string", value = "경도")
	private BigDecimal lng; 
	@ApiModelProperty(dataType = "string", value = "상태")
	private String state; 

	private List<V_CARALC_PLAN> dlvyPlan; 
	private String factoryExpcStart; 

}
