package com.sdc2ch.service.model.item;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class ItemShipment extends Item {
	private String dlvyDe;
	private Double shipmentQty;
	private Double boxQty;
	private Double paletteQty;
	
	public void decrement(ItemShipment decrement) {
		shipmentQty = new BigDecimal(shipmentQty - decrement.getShipmentQty()).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
		boxQty = new BigDecimal(shipmentQty / getBoxCnt()).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
		paletteQty = new BigDecimal(shipmentQty / getBoxCnt() / getPaletteCnt()).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
		
		if(shipmentQty < 0) {
			log.info("출하량 보다 이고를 더 많이 받았습니다. {} -> {}, {}" , dlvyDe, getItemCd(), getItemNm());
			shipmentQty = 0.0;
			boxQty = 0.0;
			paletteQty = 0.0;
		}
	}

	@Override
	public String toString() {
		return "ItemShipment [dlvyDe=" + dlvyDe + ", shipmentQty=" + shipmentQty + ", boxQty=" + boxQty
				+ ", paletteQty=" + paletteQty + ", getItemCd()=" + getItemCd() + ", getItemNm()=" + getItemNm() + "]";
	}
	
}
