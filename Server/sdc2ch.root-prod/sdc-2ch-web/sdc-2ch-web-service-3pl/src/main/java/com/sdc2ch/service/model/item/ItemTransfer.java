package com.sdc2ch.service.model.item;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ItemTransfer extends ItemShipment {
	private String from; 
	private String to; 
	private float ratio; 
}
