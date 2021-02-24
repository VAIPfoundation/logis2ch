package com.sdc2ch.service.admin.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class IndvdVhcleVo {

	@ApiModelProperty(dataType = "long", value = "id")
	private Long id;

	@ApiModelProperty(dataType = "string", value = "배송일자")
	private String dlvyDe;

	@ApiModelProperty(dataType = "string", value = "출발지")
	private String loadPlaceNm;

	@ApiModelProperty(dataType = "string", value = "도착지")
	private String dstn;

	@ApiModelProperty(dataType = "string", value = "담당자명")
	private String chargerNm;

	@ApiModelProperty(dataType = "string", value = "주소")
	private String adres;

	@ApiModelProperty(dataType = "string", value = "도착지연락처")
	private String dstnTellNo;

	@ApiModelProperty(dataType = "string", value = "제품유형")
	private String gudsTy;

	@ApiModelProperty(dataType = "string", value = "제품명")
	private String gudsNm;

	@ApiModelProperty(dataType = "string", value = "수량")
	private String qy;

	@ApiModelProperty(dataType = "string", value = "중량")
	private String wt;

	@ApiModelProperty(dataType = "string", value = "파렛트")
	private String pallet;

	@ApiModelProperty(dataType = "string", value = "주문상태")
	private String orderSttus;

	@ApiModelProperty(dataType = "string", value = "확인일시")
	private String cnfirmDt;

	@ApiModelProperty(dataType = "string", value = "차량대수")
	private String vhcleCnt;

	@ApiModelProperty(dataType = "string", value = "차종")
	private String vhcleTy;

	@ApiModelProperty(dataType = "string", value = "차량번호")
	private String vrn;

	@ApiModelProperty(dataType = "string", value = "휴대전화번호")
	private String mobileNo;

	@ApiModelProperty(dataType = "string", value = "거리")
	private String dstnc;

	@ApiModelProperty(dataType = "string", value = "TMS/EMS 전송여부")
	private String TmsEmsSyncYn;

	@ApiModelProperty(dataType = "string", value = "주문등록일시")
	private String regDt;

	@ApiModelProperty(dataType = "string", value = "배차등록일시")
	private String caralcRegDt;

	@ApiModelProperty(dataType = "string", value = "최종위치")
	private String lastLc;

	@ApiModelProperty(dataType = "string", value = "최종보고일시")
	private String lastReportDt;

}