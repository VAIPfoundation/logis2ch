package com.sdc2ch.service.admin.model;

import java.util.Date;

import javax.persistence.Column;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class StatsCommonVo {

	@ApiModelProperty(dataType = "string", value = "공장코드")
	private String fctryCd;
	@ApiModelProperty(dataType = "string", value = "공장명")
	private String fctryNm;
	@ApiModelProperty(dataType = "string", value = "배송일자")
	private String dlvyDe;
	@ApiModelProperty(dataType = "string", value = "차량번호")
	private String vrn;
	@ApiModelProperty(dataType = "string", value = "회사명")
	private String trnsprtCmpny;
	@ApiModelProperty(dataType = "string", value = "적재타입")
	private String ldngTy;
	@ApiModelProperty(dataType = "string", value = "차종")
	private String vhcleTy;
	@ApiModelProperty(dataType = "string", value = "기사코드")
	private String driverCd;
	@ApiModelProperty(dataType = "string", value = "기사명")
	private String driverNm;
	@ApiModelProperty(dataType = "string", value = "연락처")
	private String mobileNo;


	@ApiModelProperty(dataType = "string", value = "배차유형")
	private String caralcTy;

	@ApiModelProperty(dataType = "string", value = "고객센터코드")
	private String dlvyLcCd;
	@ApiModelProperty(dataType = "string", value = "고객센터명")
	private String dlvyLcNm;
	@ApiModelProperty(dataType = "string", value = "배송지유형")
	private String dlvyLcTy;

	@ApiModelProperty(dataType = "string", value = "상차 등급 - A 갯수")
	private String ldngGradCntA;
	@ApiModelProperty(dataType = "string", value = "상차 등급 - B 갯수")
	private String ldngGradCntB;
	@ApiModelProperty(dataType = "string", value = "상차 등급 - C 갯수")
	private String ldngGradCntC;

	@ApiModelProperty(dataType = "string", value = "배송 등급 - A 갯수")
	private String dlvyGradCntA;
	@ApiModelProperty(dataType = "string", value = "배송 등급 - B 갯수")
	private String dlvyGradCntB;
	@ApiModelProperty(dataType = "string", value = "배송 등급 - C 갯수")
	private String dlvyGradCntC;

	@ApiModelProperty(dataType = "string", value = "도착 등급 - A 갯수")
	private String arvlGradCntA;
	@ApiModelProperty(dataType = "string", value = "도착 등급 - B 갯수")
	private String arvlGradCntB;
	@ApiModelProperty(dataType = "string", value = "도착 등급 - C 갯수")
	private String arvlGradCntC;

	@ApiModelProperty(dataType = "string", value = "평균 하차 시간")
	private String avgUnldngTime;



	@ApiModelProperty(dataType = "string", value = "알람 갯수")
	private String alarmCnt;
	@ApiModelProperty(dataType = "string", value = "회전율")
	private String rtateRate;
	@ApiModelProperty(dataType = "string", value = "노선수")
	private String routeCnt;
	@ApiModelProperty(dataType = "date", value = "종료보고시간")
	private Date endReportTime;

	@ApiModelProperty(dataType = "string", value = "노선번호")
	private String routeNo;
	@ApiModelProperty(dataType = "string", value = "통문(입)")
	private Date ftEnter;
	@ApiModelProperty(dataType = "string", value = "상차대기시간(s)")
	private String ldngWaitTime;
	@ApiModelProperty(dataType = "string", value = "상차시작")
	private Date ldngSt;
	@ApiModelProperty(dataType = "string", value = "상차종료")
	private Date ldngEd;
	@ApiModelProperty(dataType = "string", value = "상차소요시간(s)")
	private String ldngReqreTime;
	@ApiModelProperty(dataType = "string", value = "상차등급")
	private Date ldngGrad;
	@ApiModelProperty(dataType = "string", value = "통문(출)")
	private Date ftExit;
	@ApiModelProperty(dataType = "string", value = "회차시간")
	private Date ftTurn;

	@ApiModelProperty(dataType = "date", value = "배송지(진입)")
	private Date ccEnter;
	@ApiModelProperty(dataType = "date", value = "배송지도착")
	private Date ccArrive;
	@ApiModelProperty(dataType = "date", value = "상자회수(배송지)")
	private Date ccTakeover;
	@ApiModelProperty(dataType = "date", value = "배송지출발")
	private Date ccDepart;
	@ApiModelProperty(dataType = "date", value = "배송지도착 예정시간")
	private Date ccExpcTime;
	@ApiModelProperty(dataType = "date", value = "배송지도착 예정시간 오차")
	private String ccExpcTimeDiff;
	@ApiModelProperty(dataType = "string", value = "점착등급")
	private String arvlGrad;

	public String convertString(Object val) {
		return val == null ? null : String.valueOf(val);
	}
}
