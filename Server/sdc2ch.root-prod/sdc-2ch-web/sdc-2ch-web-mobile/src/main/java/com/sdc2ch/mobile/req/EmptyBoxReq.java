package com.sdc2ch.mobile.req;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class EmptyBoxReq {
	
	@NotNull
	@ApiModelProperty(dataType = "long", value = "배송정보 고유번호")
	private Long allocatedGroupId;
	@ApiModelProperty(dataType = "long", value = "공상자등록 고유번호")
	private Long id;
	@NotNull
	@Size(min = 8, max = 8)
	@ApiModelProperty(dataType = "String", value = "배송일")
	private String dlvyDe;
	@NotNull
	@ApiModelProperty(dataType = "String", value = "노선번호")
	private String routeNo;
	@NotNull
	@ApiModelProperty(dataType = "String", value = "배송지번호")
	private String dlvyLoId;

	@ApiModelProperty(dataType = "int", value = "사각상자수량")
	private int squareBoxQty;
	
	@ApiModelProperty(dataType = "int", value = "삼각상자수량")
	private int triangleBoxQty;

	@ApiModelProperty(dataType = "int", value = "요델리상자수량")
	private int yodelryBoxQty;
	
	@ApiModelProperty(dataType = "int", value = "파렛트수량")
	private int palletQty;
	
	@ApiModelProperty(dataType = "string", value = "사유")
	private String cause;
	
	@ApiModelProperty(dataType = "string", value = "수정사유")
	private String modifyCause;
	
	@ApiModelProperty(dataType = "byte", value = "사진파일")
	private byte[] picture;
	
	public String getStopCd() {
		return dlvyLoId;
	}
	
}
