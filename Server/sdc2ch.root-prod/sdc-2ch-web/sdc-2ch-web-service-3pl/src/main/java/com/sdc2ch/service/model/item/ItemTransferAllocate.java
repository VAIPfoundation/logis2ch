package com.sdc2ch.service.model.item;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ItemTransferAllocate extends ItemShipment {
	
	private Long modelId;
	private String dlvyDe;
	private String from;
	private String to;
	private int ton_14; 
	private int ton_8;
	private int ton_7_5;
	private int ton_5;
	private int ton_4_5;
	private int ton_3_5;
	private int ton_2_5;
	private float palletQty;
	private ItemShipment[] itemShipMents;
	
}
