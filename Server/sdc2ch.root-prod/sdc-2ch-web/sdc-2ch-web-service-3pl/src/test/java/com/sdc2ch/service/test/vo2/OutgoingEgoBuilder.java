package com.sdc2ch.service.test.vo2;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sdc2ch.service.model.item.ItemTransport;
import com.sdc2ch.service.test.ProductFactory;
import com.sdc2ch.service.test.TransferNewRoute;

public class OutgoingEgoBuilder {
	
	private List<ItemTransport> oupgoings;
	private String dlvyDe;
	private List<TransferFactory> productFs;
	private ProductFactory toFactory;
	private String fctId;
	
	public OutgoingEgoBuilder(String currentDe) {
		this.dlvyDe = currentDe;
	}
	
	public void setProductFactorys(List<TransferFactory> productFs) {
		this.productFs = productFs;
	}
	public void setToFactory(ProductFactory orgFactory) {
		this.toFactory = orgFactory;
	}
	public void setOutgoings(List<ItemTransport> oupgoings) {
		this.oupgoings = oupgoings;
	}
	
	public void build() {
		BlockingQueue<ItemTransport> order = new LinkedBlockingQueue<>(oupgoings);
		while(!order.isEmpty()) {
			for(TransferFactory productF : productFs) {
				if(productF.getId().equals(order.peek().getTo())) {
					productF.addIncomming(order.poll());
					break;
				}
			}
		}
		for(TransferFactory productF : productFs) {
			List<ItemTransport> unknowns = productF.order(productFs);
			
			for(ItemTransport u : unknowns) {

				
				if(!u.getTo().equals(fctId)) {
					productFs.stream().filter(p -> p.getId().equals(fctId)).findFirst().get().product(u, productF.find(u.getTo()));
				}
				
			}
		}
	}
	public void setTransFactory(String id) {
		this.fctId = id;
	}
}
