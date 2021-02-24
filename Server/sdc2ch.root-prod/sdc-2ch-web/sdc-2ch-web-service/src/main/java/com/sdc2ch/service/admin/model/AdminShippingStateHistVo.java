package com.sdc2ch.service.admin.model;

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
public class AdminShippingStateHistVo {

	@ApiModelProperty(dataType = "BigDecimal", value = "")
	private BigDecimal rowid;
	@ApiModelProperty(dataType = "string", value = "공장코드")
	private String fctryCd;
	@ApiModelProperty(dataType = "string", value = "")
	private String fctryNm;
	@ApiModelProperty(dataType = "string", value = "")
	private String dlvyDe;
	@ApiModelProperty(dataType = "string", value = "")
	private String routeNo;
	@ApiModelProperty(dataType = "string", value = "")
	private String dlvyLcCd;
	@ApiModelProperty(dataType = "string", value = "")
	private String dlvyLcNm;
	@ApiModelProperty(dataType = "string", value = "")
	private Integer dlvySeq;
	@ApiModelProperty(dataType = "string", value = "")
	private Integer stopSeq;
	@ApiModelProperty(dataType = "string", value = "")
	private String scheStartDate;
	@ApiModelProperty(dataType = "string", value = "")
	private String scheStartTime;

	@ApiModelProperty(dataType = "string", value = "")
	private String vrn;
	@ApiModelProperty(dataType = "string", value = "")
	private String driverCd;
	@ApiModelProperty(dataType = "string", value = "")
	private String driverNm;
	@ApiModelProperty(dataType = "string", value = "")
	private String mobileNo;
	@ApiModelProperty(dataType = "double", value = "")
	private Double vhcleTy;

	@ApiModelProperty(dataType = "string", value = "")
	private String state;
	@ApiModelProperty(dataType = "string", value = "")
	private String setupLc;
	@ApiModelProperty(dataType = "string", value = "")
	private String chain;
	@ApiModelProperty(dataType = "string", value = "")
	private String createDt;
	@ApiModelProperty(dataType = "string", value = "")
	private String dataTy;
	@ApiModelProperty(dataType = "string", value = "")
	private String emptyboxYn;
	@ApiModelProperty(dataType = "string", value = "")
	private String sttus;

	@ApiModelProperty(dataType = "integer", value = "")
	private Integer total;
	@ApiModelProperty(dataType = "double", value = "")
	private Double perTotal;
	@ApiModelProperty(dataType = "integer", value = "")
	private Integer mgrAllocated;
	@ApiModelProperty(dataType = "integer", value = "")
	private Integer usrSt;
	@ApiModelProperty(dataType = "integer", value = "")
	private Integer bcnEnter;
	@ApiModelProperty(dataType = "integer", value = "")
	private Integer nfcTagOffic;


	@ApiModelProperty(dataType = "integer", value = "")
	private Integer nfcTagLdngA1cold;
	@ApiModelProperty(dataType = "integer", value = "")
	private Integer nfcTagLdngA2cold;
	@ApiModelProperty(dataType = "integer", value = "")
	private Integer nfcTagLdngB1cold;
	@ApiModelProperty(dataType = "integer", value = "")
	private Integer nfcTagLdngPrcssgd;
	@ApiModelProperty(dataType = "integer", value = "")
	private Integer nfcTagLdngSterilized;
	@ApiModelProperty(dataType = "integer", value = "")
	private Integer nfcTagLdngCu;
	@ApiModelProperty(dataType = "integer", value = "")
	private Integer nfcTagLdngHosang;
	@ApiModelProperty(dataType = "integer", value = "")
	private Integer nfcTagLdngCheese;
	@ApiModelProperty(dataType = "integer", value = "")
	private Integer nfcTagLdngFlat;
	@ApiModelProperty(dataType = "integer", value = "")
	private Integer bcnExited;
	@ApiModelProperty(dataType = "integer", value = "")
	private Integer geoEnter;
	@ApiModelProperty(dataType = "integer", value = "")
	private Integer geoArrived;
	@ApiModelProperty(dataType = "integer", value = "")
	private Integer usrEb;
	@ApiModelProperty(dataType = "integer", value = "")
	private Integer geoExited;
	@ApiModelProperty(dataType = "integer", value = "")
	private Integer usrFin;

	@ApiModelProperty(dataType = "integer", value = "")
	private Integer dlvyCnt;
	@ApiModelProperty(dataType = "integer", value = "")
	private Integer emptyboxDlvyCnt;
	@ApiModelProperty(dataType = "integer", value = "")
	private Integer routeCnt;
	@ApiModelProperty(dataType = "integer", value = "")
	private Integer dayCnt;
	@ApiModelProperty(dataType = "integer", value = "")
	private Integer eventSum;
	@ApiModelProperty(dataType = "integer", value = "")
	private Integer eventCnt;

}