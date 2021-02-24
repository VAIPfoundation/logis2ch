package com.sdc2ch.service.model.item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductItem extends Item {
	private double ratio;

	@Override
	public String toString() {
		return "ProductItem [ratio=" + ratio + ", getItemCd()=" + getItemCd() + ", getItemNm()=" + getItemNm() + "]";
	}
	
}
