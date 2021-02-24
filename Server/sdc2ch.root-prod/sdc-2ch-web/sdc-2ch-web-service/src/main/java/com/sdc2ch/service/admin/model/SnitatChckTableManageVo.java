package com.sdc2ch.service.admin.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class SnitatChckTableManageVo {

	@ApiModelProperty(dataType = "long", value = "id")
	private Long id;

	@ApiModelProperty(dataType = "string", value = "배송일자")
	private String dlvyDe;

	@ApiModelProperty(dataType = "string", value = "등록일시")
	private String regDt;

	@ApiModelProperty(dataType = "string", value = "수신자명")
	private String rcverNm;

	@ApiModelProperty(dataType = "string", value = "수신자번호")
	private String rcverPhone;

	@ApiModelProperty(dataType = "string", value = "알람유형")
	private String alramTy;

	@ApiModelProperty(dataType = "string", value = "운수회사")
	private String trnsprtCmpny;

	@ApiModelProperty(dataType = "string", value = "차종")
	private String vhcleTy;

	@ApiModelProperty(dataType = "string", value = "차량번호")
	private String vrn;

	@ApiModelProperty(dataType = "string", value = "기사명")
	private String driverNm;

	@ApiModelProperty(dataType = "string", value = "1월")
	private String month1;

	@ApiModelProperty(dataType = "string", value = "2월")
	private String month2;

	@ApiModelProperty(dataType = "string", value = "3월")
	private String month3;

	@ApiModelProperty(dataType = "string", value = "4월")
	private String month4;

	@ApiModelProperty(dataType = "string", value = "5월")
	private String month5;

	@ApiModelProperty(dataType = "string", value = "6월")
	private String month6;

	@ApiModelProperty(dataType = "string", value = "7월")
	private String month7;

	@ApiModelProperty(dataType = "string", value = "8월")
	private String month8;

	@ApiModelProperty(dataType = "string", value = "9월")
	private String month9;

	@ApiModelProperty(dataType = "string", value = "10월")
	private String month10;

	@ApiModelProperty(dataType = "string", value = "11월")
	private String month11;

	@ApiModelProperty(dataType = "string", value = "12월")
	private String month12;
}