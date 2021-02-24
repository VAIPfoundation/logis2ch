
package com.sdc2ch.service.admin.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Lob;

import com.sdc2ch.tms.io.TmsPlanIO;
import com.sdc2ch.web.admin.repo.domain.v.V_CARALC_PLAN;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.Lob;

import com.sdc2ch.tms.io.TmsPlanIO;


@Getter
@Setter
@ToString
public class AdminRoutePathMatrixVo {

	@ApiModelProperty(dataType = "string", value = "")
	private String startPos;
	@ApiModelProperty(dataType = "string", value = "")
	private String startLat;
	@ApiModelProperty(dataType = "string", value = "")
	private String startLng;
	@ApiModelProperty(dataType = "string", value = "")
	private String endPos;
	@ApiModelProperty(dataType = "string", value = "")
	private String endLat;
	@ApiModelProperty(dataType = "string", value = "")
	private String endLng;
	@ApiModelProperty(dataType = "int", value = "")
	private int tmsDistance;
	@ApiModelProperty(dataType = "int", value = "")
	private int newDistance;
	@ApiModelProperty(dataType = "int", value = "")
	private int tmsTollCost;
	@ApiModelProperty(dataType = "int", value = "")
	private int newTollCost;

	@ApiModelProperty(dataType = "string", value = "")
	private String dlvyDe;
	@ApiModelProperty(dataType = "string", value = "")
	private String routeNo;
	@ApiModelProperty(dataType = "string", value = "")
	private String vrn;

	@ApiModelProperty(dataType = "int", value = "")
	private int routePathCount;
	@ApiModelProperty(dataType = "string", value = "")
	private String routePathString;

	@Lob
	@ApiModelProperty(dataType = "string", value = "")
	private String jsonData;

	@ApiModelProperty(dataType = "List<TmsPlanIO>", value = "")
	private List<TmsPlanIO> tmsPlan;



}
