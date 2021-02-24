package com.sdc2ch.service.test.vo;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.sdc2ch.service.model.cost.CarTonType;
import com.sdc2ch.service.model.item.ItemShipment;
import com.sdc2ch.service.test.ProductFactory;
import com.sdc2ch.service.test.TransferOperations;

public abstract class AbstractFactory {
	
	protected ProductFactory current;
	protected double orgSumShpimentQty; 
	
	protected double newSumShpimentQty; 
	
	protected double stockQty;
	
	protected TransferOperations operations;
	
	public AbstractFactory(ProductFactory f) {
		this.current = f;
	}

	public void setOperations(TransferOperations operations) {
		this.operations = operations;
	}
	public void setOrgSumShpimentQty(double orgSumShpimentQty) {
		this.orgSumShpimentQty = orgSumShpimentQty;
	}

	public void setNewSumShpimentQty(double newSumShpimentQty) {
		this.newSumShpimentQty = newSumShpimentQty;
		this.stockQty = newSumShpimentQty;
	}
	public List<CarTonType> sort(ProductFactory current){
		return current.getNumOfRetention().keySet()
				.stream().filter(c -> current.getNumOfRetention().get(c).getNumOfRetention() != 0)
				.sorted(Comparator.comparingDouble(CarTonType::getParretVolume).reversed())
				.collect(Collectors.toList());
	}
	
	public String getId() {
		return current.getId();
	}
	
	public boolean supported(ItemShipment shipment) {
		return current.getItems().stream().anyMatch(item ->item.getItemCd().equals(shipment.getItemCd()) && item.getRatio() != 0);
	}
}
