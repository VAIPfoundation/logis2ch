package com.sdc2ch.service.model.item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {

	private String itemCd; 
	private String itemNm;
	private Integer boxCnt; 
	private Integer paletteCnt; 
	@Override
	public String toString() {
		return "Item [itemCd=" + itemCd + ", itemNm=" + itemNm + ", boxCnt=" + boxCnt + ", paletteCnt=" + paletteCnt
				+ "]";
	}
	@Override
	public boolean equals(Object item) {
		if(item != null || item instanceof Item) {
			return this.itemCd.equals(((Item) item).getItemCd());
		}
		return false;
	}
}
