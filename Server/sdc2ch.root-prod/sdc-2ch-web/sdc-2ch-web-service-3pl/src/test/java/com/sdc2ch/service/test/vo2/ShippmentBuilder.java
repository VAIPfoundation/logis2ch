package com.sdc2ch.service.test.vo2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import com.sdc2ch.service.model.item.ItemShipment;
import com.sdc2ch.service.test.ProductFactory;
import com.sdc2ch.service.test.TransferNewRoute;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class ShippmentBuilder {

	private Map<ProductFactory, List<ItemShipment>> shipmentsMapped = new HashMap<>();
	private List<TransferFactory> productFs;
	private ProductFactory toFactory;
	private String currentDe;
	private String transFactory;
	private List<TransferNewRoute> newroutes = new ArrayList<>();
	public ShippmentBuilder(String currentDe) {
		this.currentDe= currentDe;
	}
	public void setTransFactory(String transFactory) {
		this.transFactory = transFactory;
	}
	public void putShippments(ProductFactory transferFctry, List<ItemShipment> shipments) {
		this.shipmentsMapped.put(transferFctry, shipments);
	}
	
	public void build() {
		
		try {
			
			for(ProductFactory tf : shipmentsMapped.keySet()) {
				
				BlockingQueue<ItemShipment> order = new LinkedBlockingQueue<>(shipmentsMapped.get(tf));
				
				while(!order.isEmpty()) {
					ItemShipment shipment = order.poll();
					List<TransferFactory> supports = findSupports(shipment);
					supports.forEach(f -> f.product(shipment, tf));
					if(supports.isEmpty()) {



						log.info("생산공장이 없은 이고 재품, {},{},{}", shipment.getShipmentQty(), shipment.getItemCd(), shipment.getItemNm());

					}
				}
			}
			
			productFs.stream().forEach(p ->newroutes.addAll( p.getEgo(currentDe)));
			
		}finally {
		}

		
	}
	private List<TransferFactory> findSupports(ItemShipment shipment) {
		return productFs.stream().filter(f -> f.supported(shipment)).collect(Collectors.toList());
	}
	public void setProductFactorys(List<TransferFactory> productFs) {
		this.productFs = productFs;
	}
	public void setToFactory(ProductFactory orgFactory) {
		this.toFactory = orgFactory;
	}
	public List<TransferNewRoute> getNewRoutes() {
		return newroutes;
	}

}
