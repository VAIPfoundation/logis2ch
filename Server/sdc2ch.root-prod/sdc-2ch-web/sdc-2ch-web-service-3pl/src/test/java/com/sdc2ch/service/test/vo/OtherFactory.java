package com.sdc2ch.service.test.vo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import com.sdc2ch.service.model.item.ItemTransport;
import com.sdc2ch.service.test.ProductFactory;
import com.sdc2ch.service.test.TransferNewRoute;
import com.sdc2ch.service.test.TransferRouteGenerator;
import com.sdc2ch.service.test.Vehicles.Vehicle;

public class OtherFactory extends AbstractFactory {

	private BlockingQueue<ItemTransport> product;
	public OtherFactory(ProductFactory f) {
		super(f);
		product = new LinkedBlockingQueue<>();
	}

	public String getId() {
		return current.getId();
	}

	public void setProductPlan(List<ItemTransport> list) {
		

		
		product.addAll(list);
	}

	public TransferNewRoute transferOrder(ProductFactory to, String dlvyDe, List<String> transferItems) {













		return null;
	}

	public void clear() {
		current.clear();
	}
}
