package com.sdc2ch.service.test.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.sdc2ch.service.model.item.ItemTransport;
import com.sdc2ch.service.model.item.ItemTransport.ItemShipType;
import com.sdc2ch.service.test.ProductFactory;
import com.sdc2ch.service.test.TransferVolume.ItemChulHa;


public class SufficientFactory extends AbstractFactory {
	
	private List<EgoOrder> egoOrders = new ArrayList<>();

	public SufficientFactory(ProductFactory f) {
		super(f);
	}

	
	
	public boolean supported(ItemChulHa item) {
		return stockQty > 0;
	}

	public EgoOrder order(ItemChulHa item) {
		EgoOrder order = new EgoOrder();
		ItemTransport ego = new ItemTransport();
		BeanUtils.copyProperties(item, ego);
		ego.setFrom(current.getId());
		ego.setTo(item.getPt().getId());
		ego.setItemVolumeType(ItemShipType.OUT);
		stockQty =- item.getShipmentQty();
		order.item = item;
		order.ego = ego;
		egoOrders.add(order);
		return order;
	}
	
	
	public class EgoOrder {
		private ItemChulHa item;
		private ItemTransport ego;
		public ItemChulHa getItem() {
			return item;
		}
		public ItemTransport getEgo() {
			return ego;
		}
	}

}
