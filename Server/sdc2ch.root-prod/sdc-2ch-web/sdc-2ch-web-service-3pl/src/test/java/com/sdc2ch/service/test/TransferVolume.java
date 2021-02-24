package com.sdc2ch.service.test;

import java.util.List;
import java.util.stream.Collectors;

import com.sdc2ch.service.model.item.ItemShipment;
import com.sdc2ch.service.model.item.ItemShipmentByRoute;
import com.sdc2ch.service.model.item.ItemTransport;
import com.sdc2ch.service.util.Utils;

import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class TransferVolume {
	private static final String SHIPMENT_NAME = "_chulha";
	private Long id;
	private TransferFTF transferTFT;
	private List<String> transferItems;
	private String volumeName;
	private List<ItemShipmentByRoute> shipments;
	private List<ItemTransport> outgoings;
	

	@Override
	public String toString() {
		return Utils.toJsonString(this);
	}
	public List<ItemTransport> getOutgoings(String currentDe) {
		if(outgoings == null) {
			TransferOperations operations = transferTFT.getOperations();
			outgoings = operations.getOutgoings();
		}
		return outgoings.stream().filter(s -> s.getDlvyDe().equals(currentDe)).collect(Collectors.toList());
	}
	public TransferOperations getOperations() {
		return transferTFT.getOperations();
	}
	public List<ItemShipmentByRoute> getShipmentItems(String currentDe) {
		
		if(shipments == null) {
			shipments = transferTFT.getOperations().getShipmentItems(id);
		}
		






		
		
		
		
		return shipments.stream().filter(s -> s.getDlvyDe().equals(currentDe)).collect(Collectors.toList());
	}
	public class ItemChulHa extends ItemShipment {
		private ProductFactory pt;

		public ProductFactory getPt() {
			return pt;
		}
		public void setPt(ProductFactory pt) {
			this.pt = pt;
		}
	}

}
