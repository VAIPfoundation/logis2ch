package com.sdc2ch.service.model.item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemShipmentByRoute {
	private String fctryCd;
	private String dlvyDe;
	private String routeNo;
	private String itemCd;
	private String itemNm;
	private double shipQty;
	private int has;
	private String newPoint;
	private Integer boxCnt;
	private Integer paletteCnt;
}
