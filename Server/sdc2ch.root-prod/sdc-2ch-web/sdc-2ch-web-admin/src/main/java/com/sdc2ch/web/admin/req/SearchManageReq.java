package com.sdc2ch.web.admin.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchManageReq extends SearchReq {

	@ApiModelProperty(dataType = "long", value = "id")
	private Long id;
	@ApiModelProperty(dataType = "string", value = "차종")
	private String vhcleTy;
	@ApiModelProperty(dataType = "string", value = "연락처")
	private String mobileNo;
	@ApiModelProperty(dataType = "string", value = "년월")
	private String month;
	@ApiModelProperty(dataType = "string", value = "시작일")
	private String fromDe;
	@ApiModelProperty(dataType = "string", value = "종료일")
	private String toDe;
	@ApiModelProperty(dataType = "string", value = "시작일시(yyyy-MM-dd HH:mm)",example="2019-01-01 00:00")
	private String fromDt;
	@ApiModelProperty(dataType = "string", value = "종료일시(yyyy-MM-dd HH:mm)",example="2019-01-02 00:00")
	private String toDt;
	@ApiModelProperty(dataType = "string", value = "기사명")
	private String driverNm;
	@ApiModelProperty(dataType = "string", value = "거점")
	private String changeBase;

	@ApiModelProperty(dataType = "string", value = "노선분류(배송/이고)")
	private String RouteTy2;
	@ApiModelProperty(dataType = "string", value = "배차유형")
	private String dlvyTy;
	@ApiModelProperty(dataType = "Boolean", value = "GPS상태")
	private Boolean gpsYn;
	@ApiModelProperty(dataType = "string", value = "APP 아이디")
	private String appId;
	@ApiModelProperty(dataType = "string", value = "APP 이름")
	private String appName;
	@ApiModelProperty(dataType = "string", value = "APP 버전")
	private String appVersion;
	@ApiModelProperty(dataType = "string", value = "아이피 주소", example="XXX.XXX.XXX.XXX")
	private String ip;
	@ApiModelProperty(dataType = "string", value = "호스트")
	private String host;
	@ApiModelProperty(dataType = "string", value = "호스트")
	private Boolean sttus;
	@ApiModelProperty(dataType = "string", value = "APP 파일 사이즈")
	private Long size;
	@ApiModelProperty(dataType = "string", value = "APP 경로")
	private String path;
	@ApiModelProperty(dataType = "string", value = "알람설정 Type")
	private String alarmType;
	@ApiModelProperty(dataType = "string", value = "알람설정 value")
	private String alarmValue;
	@ApiModelProperty(dataType = "string", value = "전화번호", example="01011112222 OR 010-1111-2222")
	private String mdn;
	@ApiModelProperty(dataType = "string", value = "창고 코드")
	private String setupLcTy;
	@ApiModelProperty(dataType = "string", value = "약관구분 코드")
	private String tosRegType;


}
