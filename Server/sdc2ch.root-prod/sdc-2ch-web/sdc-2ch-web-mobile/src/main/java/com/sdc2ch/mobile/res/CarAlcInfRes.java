package com.sdc2ch.mobile.res;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class CarAlcInfRes{

	
	@ApiModelProperty(dataType = "String", value = "배송일자")
	private String deliveryDate;
	@ApiModelProperty(dataType = "String", value = "회차정보")
	private List<CarAlcInfRotats> rotations;
	
	public String getDeliveryDate() {	
		return deliveryDate;
	}
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public List<CarAlcInfRotats> getRotations() {
		return rotations;
	}
	public void setRotations(List<CarAlcInfRotats> rotations) {
		this.rotations = rotations;
	}
}
