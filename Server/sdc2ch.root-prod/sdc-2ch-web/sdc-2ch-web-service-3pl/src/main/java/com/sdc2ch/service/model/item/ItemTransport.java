package com.sdc2ch.service.model.item;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ItemTransport extends ItemShipment {

	public enum ItemShipType {
		IN,
		OUT
	}
	private String from; 
	private String to;   
	private ItemShipType  itemVolumeType;
	@Override
	public String toString() {
		return from + " -> " + to + " " + super.toString();
	}
	
	
}
