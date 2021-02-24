package com.sdc2ch.service;

import java.util.List;

import com.sdc2ch.service.model.item.Item;
import com.sdc2ch.service.model.item.ItemProduct;
import com.sdc2ch.service.model.item.ItemShipment;
import com.sdc2ch.service.model.item.ItemShipmentByRoute;
import com.sdc2ch.service.model.item.ItemTransfer;
import com.sdc2ch.service.model.item.ItemTransferAllocate;
import com.sdc2ch.service.model.item.ItemTransport;
import com.sdc2ch.service.transfer.TransferPlan;


public interface ILgistTransService {
	
	
	String ITEM_LIST = "[dbo].[SP_2CH_LGIST_TRANS_ITEM_LIST]";
	String SHIP_QTY_SP = "[dbo].[SP_2CH_LGIST_ROUTE_ITEM_LIST]";
	String SHIP_QTY_SP_ALL = "[dbo].[SP_2CH_LGIST_ROUTE_ITEM_LIST_ALL]";
	String SHIP_QTY = "[dbo].[SP_2CH_LGIST_TRANS_SHIP_QTY]";
	String incomming = "[dbo].[SP_2CH_LGIST_TRANS_INCOMMING]";
	String outgoing = "[dbo].[SP_2CH_LGIST_TRANS_OUTGOING]";
	

	String ITEM_PRODUCT_WHERE = "where t1.ITEM_CD in (\n" + 
			"select distinct item_cd from tms.dbo.m_order where 1 =1\n" + 
			"and delivery_date = '#1'\n" + 
			"and route_no in (#2))";
	
	String ITEM_PRODUCT = "[dbo].[SP_2CH_LGIST_TRANS_ITEM_PRODUCT]";
	String ITEM_ROUTE_SHIPMENT = "[dbo].[SP_2CH_LGIST_ROUTE_SHIPMENT]";

	
	String route_no = "	SELECT ROUTE_NO\n" + 
			"	FROM [2CH].[dbo].[T_LGIST_ROUTE] where id_lgist_model_fk = ?1";
	
	public List<Item> findAll();
	
	
	public List<ItemShipment> searchShipmentItems(String fctryCd, String fromDe, String toDe);
	public List<ItemShipment> searchShipmentItems(Long modelId, String fctryCd, String fromDe, String toDe);
	
	public List<ItemProduct> searchProductItems(String fctryCd, String productDe);
	public List<ItemProduct> searchProductItems(Long modelId, String fctryCd, String productDe);
	public List<ItemProduct> searchProductItems(Long modelId, String fctryCd, String fromDe, String toDe);
	
	public List<ItemTransport> searchInCommingTransportItems(String fctryCd, String fromDe, String toDe);
	public List<ItemTransport> searchInCommingTransportItems(Long modelId, String fctryCd, String fromDe, String toDe);
	
	public List<ItemTransport> searchOutGoingTransportItems(String fctryCd, String fromDe, String toDe);
	public List<ItemTransport> searchOutGoingTransportItems(Long modelId, String fctryCd, String fromDe, String toDe);
	
	
	public List<ItemTransfer> searchItemTransferById(Long modelId);
	
	public List<ItemTransfer> searchItemTransferById(String fctryCd, String fromDe, String toDe, final String toFctryCd);
	public List<ItemTransfer> searchItemTransferById(Long modelId, String fctryCd, String fromDe, String toDe, final String toFctryCd);
	
	
	public int insertNewRoute(Long modelId, ItemTransferAllocate itemTransferAllocate);
	public int insertNewRoute(ItemTransferAllocate itemTransferAllocate);
	
	
	public void endOfNewRoute(Long modelId);
	
	
	
	public List<TransferPlan> getProductPlanItems(Long id);
	
	
	public List<ItemShipmentByRoute> searchAllShipmentQty(Long modelId, String from, String to);

}
