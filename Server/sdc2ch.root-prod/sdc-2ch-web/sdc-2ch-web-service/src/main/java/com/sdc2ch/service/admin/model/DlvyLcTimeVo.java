package com.sdc2ch.service.admin.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class DlvyLcTimeVo {

	@ApiModelProperty(dataType = "long", value = "id")
	private Long id;

	@ApiModelProperty(dataType = "string", value = "배송일자")
	private String dlvyDe;



























}