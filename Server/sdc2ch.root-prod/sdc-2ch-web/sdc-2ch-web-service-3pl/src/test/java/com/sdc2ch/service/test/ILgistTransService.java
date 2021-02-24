package com.sdc2ch.service.test;

import java.time.LocalDate;
import java.util.List;

import com.sdc2ch.service.model.item.ItemShipment;
import com.sdc2ch.service.model.item.ItemShipmentByRoute;
import com.sdc2ch.service.model.item.ItemTransferAllocate;
import com.sdc2ch.service.model.item.ItemTransport;
import com.sdc2ch.service.model.item.ProductItem;
import com.sdc2ch.service.transfer.TransferPlan;


public interface ILgistTransService {
	
	
	public List<ItemShipment> searchShipmentItems(LocalDate fromDe, LocalDate toDe, ProductFactory pt);
	
	public List<ItemTransport> searchInCommingTransportItems(LocalDate fromDe, LocalDate toDe, String string);
	
	public List<ItemTransport> searchOutGoingTransportItems(LocalDate fromDe, LocalDate toDe, String string);
	
	
	public int write(ItemTransferAllocate itemTransferAllocate);
	
	
	public void endOfNewRoute(Long modelId);

	public ShipmentVolumeGroup searchVolumeItems(LocalDate fromDe, LocalDate toDe, Long id);
	

	public List<ItemShipmentByRoute> getShipmentItems(LocalDate fromDe, LocalDate toDe, Long id);
	
	public List<ItemShipment> searchItemshipment(LocalDate fromDe, LocalDate toDe, String[] array);
	
	public List<ProductItem> getProductItems(String id);
	public List<TransferPlan> getProductPlanItems(Long id);
	public void insert(List<TransferNewRoute> newroure);

}
