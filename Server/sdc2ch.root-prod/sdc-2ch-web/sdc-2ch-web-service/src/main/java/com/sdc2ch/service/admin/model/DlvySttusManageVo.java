package com.sdc2ch.service.admin.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class DlvySttusManageVo {

	@ApiModelProperty(dataType = "long", value = "id")
	private Long id;

	@ApiModelProperty(dataType = "string", value = "배송일자")
	private String dlvyDe;

	@ApiModelProperty(dataType = "string", value = "공장명")
	private String fctryNm;

	@ApiModelProperty(dataType = "string", value = "차량번호")
	private String vrn;

	@ApiModelProperty(dataType = "string", value = "기사명")
	private String driverNm;

	@ApiModelProperty(dataType = "string", value = "운수회사")
	private String trnsprtCmpny;

	@ApiModelProperty(dataType = "string", value = "차종")
	private String vhcleTy;

	@ApiModelProperty(dataType = "string", value = "운행상태")
	private String driveSttus;

	@ApiModelProperty(dataType = "string", value = "온도1")
	private String tempt1;

	@ApiModelProperty(dataType = "string", value = "온도2")
	private String tempt2;

	@ApiModelProperty(dataType = "string", value = "최종보고일시")
	private String lastReportDt;

	@ApiModelProperty(dataType = "string", value = "연비(km/ℓ)")
	private String mileg;

	@ApiModelProperty(dataType = "string", value = "운송중량합계(ton)")
	private String wtSm;

	@ApiModelProperty(dataType = "string", value = "운행거리(km)")
	private String driveDstnc;

	@ApiModelProperty(dataType = "string", value = "공차이동거리(km)")
	private String emptVhcleMoveDstnc;

	@ApiModelProperty(dataType = "string", value = "영차이동거리(km)")
	private String ldngVhcleMoveDstnc;

	@ApiModelProperty(dataType = "string", value = "톤킬로(ton.km)")
	private String tonkm;

	@ApiModelProperty(dataType = "string", value = "연료소모량(ℓ)")
	private String fuelUsgqty;

	@ApiModelProperty(dataType = "string", value = "탄소배출량(kg.CO₂.eq)")
	private String co2Dscamt;

	@ApiModelProperty(dataType = "string", value = "에너지효율지표(ℓ/ton.km)")
	private String energyEfcIdx;

	@ApiModelProperty(dataType = "string", value = "원단위(g.CO₂.eq/ton.km)")
	private String unitTonkm;

}