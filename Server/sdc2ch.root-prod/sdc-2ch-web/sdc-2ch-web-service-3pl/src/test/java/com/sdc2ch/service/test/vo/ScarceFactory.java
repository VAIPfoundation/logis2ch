package com.sdc2ch.service.test.vo;

import java.util.List;

import com.sdc2ch.service.test.ProductFactory;
import com.sdc2ch.service.test.TransferVolume.ItemChulHa;


public class ScarceFactory extends AbstractFactory {

	private List<ItemChulHa> scarceItems;

	public ScarceFactory(ProductFactory f) {
		super(f);
	}

	public List<ItemChulHa> getScarceItems() {
		return scarceItems;
	}
	public void setScarceItems(List<ItemChulHa> scarceItems) {
		this.scarceItems = scarceItems;
	}
}
