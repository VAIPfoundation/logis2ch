package com.sdc2ch.web.admin.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchAnalsReq extends SearchReq {

	@ApiModelProperty(dataType = "string", value = "용차여부")
	private String indvdlVhcleYn;

	@ApiModelProperty(dataType = "string", value = "준비여부")
	private String readyYn;

	@ApiModelProperty(dataType = "string", value = "상차여부")
	private String ldngYn;

	@ApiModelProperty(dataType = "string", value = "배송여부")
	private String dlvyYn;

	@ApiModelProperty(dataType = "string", value = "회차")
	private String rtnDriveYn;

	@ApiModelProperty(dataType = "string", value = "완료")
	private String comptYn;

	@ApiModelProperty(dataType = "string", value = "휴무")
	private String hvofYn;

	@ApiModelProperty(dataType = "string", value = "고객센터 도착지연 위험 차량")
	private String arvlDelayRiskYn;

	@ApiModelProperty(dataType = "string", value = "고객센터 도착 지연 차량")
	private String arvlDelayYn;

	@ApiModelProperty(dataType = "string", value = "연속 4시간 이상 주행 차량")
	private String overCtnuDriveYn;


	@ApiModelProperty(dataType = "string", value = "")
	private String wkdayYn;
	@ApiModelProperty(dataType = "string", value = "")
	private String satYn;
	@ApiModelProperty(dataType = "string", value = "")
	private String sunYn;


	@ApiModelProperty(dataType = "string", value = "")
	private String vhcleTy;
	@ApiModelProperty(dataType = "long", value = "기준시간")
	private Long stdTime;
	@ApiModelProperty(dataType = "long", value = "보정시간")
	private Long adjustTime;


	@ApiModelProperty(dataType = "string", value = "상차등급")
	private String ldngGrad;
	@ApiModelProperty(dataType = "string", value = "배송등급")
	private String dlvyGrad;
	@ApiModelProperty(dataType = "string", value = "고객센터코드(콤보박스)")
	private String dlvyLcCd;
	@ApiModelProperty(dataType = "string", value = "고객센터명(택스트박스)")
	private String dlvyLcNm;

	@Deprecated
	@ApiModelProperty(dataType = "string", value = "고객센터점착시간(콤보박스)")
	private String arvlStdTime;
	@ApiModelProperty(dataType = "string", value = "고객센터점착시간(콤보박스)")
	private String dlvyLcTime;


	@ApiModelProperty(dataType = "string", value = "기준시간(분)")
	private String stdTimeHHMI;

	@ApiModelProperty(dataType = "string", value = "운수회사")
	private String trnsprtCmpny;

	@ApiModelProperty(dataType = "string", value = "주소")
	private String adres;
	@ApiModelProperty(dataType = "string", value = "경도")
	private String lng;
	@ApiModelProperty(dataType = "string", value = "위도")
	private String lat;



}
