package com.sdc2ch.service.tm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sdc2ch.service.ILgistTransService;
import com.sdc2ch.service.model.item.Item;
import com.sdc2ch.service.model.item.ItemProduct;
import com.sdc2ch.service.model.item.ItemShipment;
import com.sdc2ch.service.model.item.ItemShipmentByRoute;
import com.sdc2ch.service.model.item.ItemTransfer;
import com.sdc2ch.service.model.item.ItemTransferAllocate;
import com.sdc2ch.service.model.item.ItemTransport;
import com.sdc2ch.service.transfer.TransferPlan;

public class Model {
	private Graph graph = null;
	
	private int totalNumOfItem = 0;
	private IItem items[] = null;
	private HashMap<String, Integer> itemHashmap = null;
	
	private ArrayList<Transfer>[] items_transfered = null;
	
	public ArrayList<Route> oldRouteList = null;
	public ArrayList<Route> newRouteList = null;
	public HashMap<String, Route> oldRouteHashMap = null;
	public HashMap<String, Route> newRouteHashMap = null;
	
	
	private double ship_2d1_has_1 = 0.0;
	private double ship_2d1_has_0 = 0.0;
	
	private double ship_2d1_has_1_[] = {0.0, 0.0, 0.0, 0.0};
	
	
	
	private double ship_2d1_has_0_T1 = 0.0;
	private double ship_2d1_has_0_T1_[] = {0.0, 0.0, 0.0, 0.0};
	
	
	
	private double ship_2d1_has_0_T3 = 0.0;
	private double ship_2d1_has_0_T3_[] = {0.0, 0.0, 0.0, 0.0};
	
	
	
	private double ship_2d1_has_0_T4 = 0.0;
	private double ship_2d1_has_0_T4_[] = {0.0, 0.0, 0.0, 0.0}; 
	
	
	
	Double[] d_has1 = null;
	Double[][] d_has1_sub = null;
	
	Double[] d_has0 = null;
	Double[][] d_has0_sub = null;
	
	Double[] d_out = null;
	Double[][] d_out_sub = null;
	
	ArrayList<String> factoryList = new ArrayList<String>();
		
	
	private double out_2d1_1d1 = 0.0;
	private double out_2d1_3d1 = 0.0;
	private double out_2d1_4d1 = 0.0;
	
	private double out_2d1_1d1_T_[] = {0.0, 0.0, 0.0, 0.0};
	
	
	
	private double out_2d1_3d1_T_[] = {0.0, 0.0, 0.0, 0.0}; 
	
	
	
	private double out_2d1_4d1_T_[] = {0.0, 0.0, 0.0, 0.0};
	
	
	
	int _reverse  = 0;
	int _t_reverse  =0;
	
	private boolean _log_verify_print = false;

	
	
	
	private HashMap<String, Integer> vehicleMasterHashMap = null;
	private HashMap<String, ArrayList<Vehicle>> factoryVehicleHashMap = null;
	
	private HashMap<String, Double> validItemMasterHashmap = new HashMap<String, Double>();
	
	private HashMap<String, List<ItemShipmentByRoute>> searchAllShipmentQtyHashmap = new HashMap<String, List<ItemShipmentByRoute>>();
	private HashMap<String, List<ItemTransport>> searchOutGoingTransportItemsHashmap = new HashMap<String, List<ItemTransport>>();
	
	private ILgistTransService svc;
	private long modelID;
	
	ArrayList<String> not_assign_vehicle = null;

	
	public Model(String originID) {
		graph = new Graph();
		generateGraph(graph, originID);
		
		
		
		d_has1 = new Double[4];
		d_has0 = new Double[4];
		d_out = new Double[4];
		for (int i=0; i < 4; i++) {
			d_has1[i] = new Double(0.0);
			d_has0[i] = new Double(0.0);
			d_out[i] = new Double(0.0);
		}
		
		d_has1_sub = new Double[4][4];
		d_has0_sub = new Double[4][4];
		d_out_sub = new Double[4][4];
		for (int i=0; i < 4; i++) {
			for (int j=0; j < 4; j++) {
				d_has1_sub[i][j] = new Double(0.0);
				d_has0_sub[i][j] = new Double(0.0);
				d_out_sub[i][j] = new Double(0.0);
			}
		}
		factoryList.add("1D1");
		factoryList.add("2D1");
		factoryList.add("3D1");
		factoryList.add("4D1");
		factoryList.add("5D1");
		
		
		not_assign_vehicle = new ArrayList<String>();
		not_assign_vehicle.add("4D1|1D1");
	}
	
	public Model(ILgistTransService svc, String originID, long modelID) {
		this.svc = svc;
		this.modelID = modelID;
		
		graph = new Graph();
		generateGraph(graph, originID);
		
		
		d_has1 = new Double[4];
		d_has0 = new Double[4];
		d_out = new Double[4];
		for (int i=0; i < 4; i++) {
			d_has1[i] = new Double(0.0);
			d_has0[i] = new Double(0.0);
			d_out[i] = new Double(0.0);
		}
		
		d_has1_sub = new Double[4][4];
		d_has0_sub = new Double[4][4];
		d_out_sub = new Double[4][4];
		for (int i=0; i < 4; i++) {
			for (int j=0; j < 4; j++) {
				d_has1_sub[i][j] = new Double(0.0);
				d_has0_sub[i][j] = new Double(0.0);
				d_out_sub[i][j] = new Double(0.0);
			}
		}
		factoryList.add("1D1");
		factoryList.add("2D1");
		factoryList.add("3D1");
		factoryList.add("4D1");
		factoryList.add("5D1");
		
		
		not_assign_vehicle = new ArrayList<String>();
		not_assign_vehicle.add("4D1|1D1");
	}
	
	private void generateGraph(Graph graph, String originVID) {
		
		
		
		graph.addVertex("1D1", "1D1");
		graph.addVertex("2D1", "2D1");
		graph.addVertex("3D1", "3D1");
		graph.addVertex("4D1", "4D1");
		graph.addVertex("5D1", "5D1");
		
		if (graph.getVertexbyID(originVID) == null) {
			graph.addVertex(originVID, originVID);
		}
		
		graph.setOriginVertex(originVID);
		
		for (int i=0; i < graph.getNumOfVertex(); i++) {
			for (int j=0; j < graph.getNumOfVertex(); j++) {
				if (i == j) continue;
				
				graph.addEdge(graph.getVertexbyIndex(i).getID(), graph.getVertexbyIndex(j).getID());
			}
		}
	}
	
	
	public void _load_alldata(String startDate, String endDate)  {	
		
		List<ItemShipmentByRoute> itemList =  this.svc.searchAllShipmentQty(this.modelID, startDate, endDate);
		for(ItemShipmentByRoute its : itemList) {
			
			if (its.getFctryCd().equals(graph.getOriginVertex().getID()) != true) {
				continue;
			}
			
			if (searchAllShipmentQtyHashmap.get(its.getDlvyDe()) == null) {
				List<ItemShipmentByRoute> _ItemLIst = new ArrayList<ItemShipmentByRoute>();
				_ItemLIst.add(its);
				searchAllShipmentQtyHashmap.put(its.getDlvyDe(), _ItemLIst);
			}
			else {
				List<ItemShipmentByRoute> _ItemLIst = searchAllShipmentQtyHashmap.get(its.getDlvyDe());
				_ItemLIst.add(its);
			}	
		}	
		System.out.println("searchAllShipmentQtyHashmap:" + searchAllShipmentQtyHashmap.size());
		
		
		List<ItemTransport> TransferList2 = this.svc.searchOutGoingTransportItems(0L, graph.getOriginVertex().getID(), startDate, endDate);
		for(ItemTransport itt : TransferList2) {
			if (searchOutGoingTransportItemsHashmap.get(itt.getDlvyDe()) == null) {
				List<ItemTransport> _ItemLIst = new ArrayList<ItemTransport>();
				_ItemLIst.add(itt);
				searchOutGoingTransportItemsHashmap.put(itt.getDlvyDe(), _ItemLIst);
			}
			else {
				List<ItemTransport> _ItemLIst = searchOutGoingTransportItemsHashmap.get(itt.getDlvyDe());
				_ItemLIst.add(itt);
			}
		}
		System.out.println("_OutTHashMap:" + searchOutGoingTransportItemsHashmap.size());
	}
	
	
	public void clear_hashmap() {
		searchAllShipmentQtyHashmap.clear();
		
		
		
		searchOutGoingTransportItemsHashmap.clear();
	}
	
	public void summary() {
		if (this._log_verify_print == true) {
			
			
			for (int i=0; i < 4; i++) {
				if (this.d_has1[i] == 0.0) continue;
				System.out.println("has 1 Ship" + (i+1) + ":\t" + this.d_has1[i]);
				for (int j=0; j < 4; j++) {
					if (j==1) continue;
					System.out.println("has 1 T" + (j+1) + "->" + (i+1) + ":\t" + this.d_has1_sub[i][j]);
				}
			}
			System.out.println("has 0:\t" + this.ship_2d1_has_0);
			for (int i=0; i < 4; i++) {
				if (this.d_has0[i] == 0.0) continue;
				
				System.out.println("has 0 T" + (i+1) + ":\t" + this.d_has0[i]);
				for (int j=0; j < 4; j++) {
					if (j==1) continue;
					System.out.println("has 0 T" + (i+1) + "->" + (j+1) + ":\t" + this.d_has0_sub[i][j]);
				}
			}
			for (int i=0; i < 4; i++) {
				if (this.d_out[i] == 0.0) continue;
				System.out.println("out " + graph.getOriginVertex().getID() + "->" + (i+1) + ":\t" + this.d_out[i]);
				for (int j=0; j < 4; j++) {
					if (j==1) continue;
					System.out.println("out T" + (j+1) + "->" + (i+1) + ":\t" + this.d_out_sub[i][j]);
				}
			}
		}

	}

	
	public void nothasItemGGeneateNewRoute(String defaultTransfer, String curDate) {
		
		
		
		double total_qty = 0.0;
		double real_total_qty = 0.0;
		double not_qty1 = 0.0;
		double not_qty2 = 0.0;
		double no_transfer = 0.0;
		double ship_total = 0.0;
		double zero_pallet = 0.0;
		double newship[] = {0.0, 0.0, 0.0, 0.0};
		double dropqty[] = {0.0, 0.0, 0.0, 0.0};
		
		
		List<ItemShipmentByRoute> itemList = searchAllShipmentQtyHashmap.get(curDate);
		if (itemList == null) return;
		
		for(ItemShipmentByRoute its : itemList) {
			String itemCD = its.getItemCd();
			double shipqty = its.getShipQty();
			String newPointID = its.getNewPoint();
							
			
			total_qty += shipqty;
			
				
			
			if (its.getHas() == 1) {
				not_qty1 += shipqty;
				continue;
			}
			
			if (itemHashmap.get(itemCD) == null) {
				System.out.println("not found:\t" + itemCD);
				not_qty2 += shipqty;
				continue;
			}
			
			
			real_total_qty += shipqty;
			
					
			if (newPointID.equals("1D1") == true) newship[0] += shipqty;
			else if (newPointID.equals("2D1") == true) newship[1] += shipqty;
			else if (newPointID.equals("3D1") == true) newship[2] += shipqty;
			else if (newPointID.equals("4D1") == true) newship[3] += shipqty;	
			
			if (items[itemHashmap.get(itemCD)].getPerBox() != 0 
					&& items[itemHashmap.get(itemCD)].getPerPallet() != 0) 
			{		
				if (items_transfered[itemHashmap.get(itemCD)].size() == 0) {
					
	
					if (defaultTransfer == null) {
						
						no_transfer += shipqty;
						
						continue;
					}
					else {
						
						Transfer transfer = new Transfer(graph.getVertexbyID(defaultTransfer), 1.0);
						items_transfered[itemHashmap.get(itemCD)].add(transfer);
					}

				}
				
				
				for (int tidx=0; tidx < items_transfered[itemHashmap.get(itemCD)].size(); tidx++) {
					Transfer transfer1 = items_transfered[itemHashmap.get(itemCD)].get(tidx);
					
					if (transfer1.vertex.getID().equals(newPointID) == true) {
						
						if (transfer1.vertex.getID().equals("1D1") == true) dropqty[0] += shipqty;
						else if (transfer1.vertex.getID().equals("2D1") == true) dropqty[1] += shipqty;
						else if (transfer1.vertex.getID().equals("3D1") == true) dropqty[2] += shipqty;
						else if (transfer1.vertex.getID().equals("4D1") == true) dropqty[3] += shipqty;	
						
						continue;
					}
					
					
					Route newroute = null;
					newroute = createRoute(transfer1.vertex,  graph.getVertexbyID(newPointID), items[itemHashmap.get(itemCD)], shipqty * transfer1.ratio) ;
					addToNewRouteList(newRouteList, newRouteHashMap, newroute);
					
					
					
					
					ship_total += (shipqty * transfer1.ratio);
					
				}			
			}
			else {
				
				zero_pallet += shipqty;
				
			}
		}
		
		System.out.println("=====================");
		System.out.println("total0 qty:" + total_qty);
		System.out.println("not qty1:" + not_qty1);
		System.out.println("not qty2:" + not_qty2);
		System.out.println("total1 qty:" + real_total_qty);
		System.out.println("newship: 1D1:\t" + newship[0] + "\t2D1:\t" + newship[1] + "\t3D1:\t" + newship[2] + "\t4D1:\t" + newship[3]);
		System.out.println("no transfer:" + no_transfer);
		System.out.println("[pallet=0 box=0]:" + zero_pallet);	
		System.out.println("drop: 1D1:\t" + dropqty[0] + "\t2D1:\t" + dropqty[1] + "\t3D1:\t" + dropqty[2] + "\t4D1:\t" + dropqty[3]);
		System.out.println("ship qty:" + ship_total);
		System.out.println("=====================");
	}
	
	
	private void bubbleSort(ArrayList<Vehicle> vehicleFactory) {
		
		for (int i=0; i < vehicleFactory.size()-1; i++) {
			for (int j=0; j < vehicleFactory.size() -1 - i; j++) {
				Vehicle v1 = vehicleFactory.get(j);
				Vehicle v2 = vehicleFactory.get(j+1);
				
				if (v1.getLimitPallet() < v2.getLimitPallet()) {
					vehicleFactory.remove(j+1);
					vehicleFactory.add(j, v2);
				}
			}
		}
	}
	
	
	public void load_vehicleMaster() {

		vehicleMasterHashMap = new HashMap<String, Integer>();
		factoryVehicleHashMap = new HashMap<String, ArrayList<Vehicle>>();
		
		vehicleMasterHashMap.put("14T", 	16);
		vehicleMasterHashMap.put("4.5T", 	14);
		vehicleMasterHashMap.put("8T", 		10);
		vehicleMasterHashMap.put("5T", 		8);
		vehicleMasterHashMap.put("3.5T", 	4);
		vehicleMasterHashMap.put("2.5T", 	3);
		
		ArrayList<Vehicle> factoryVehicle = new ArrayList<Vehicle>();
		factoryVehicle.add(new Vehicle("14T", vehicleMasterHashMap));
		factoryVehicle.add(new Vehicle("8T", vehicleMasterHashMap));
		factoryVehicle.add(new Vehicle("5T", vehicleMasterHashMap));
		factoryVehicle.add(new Vehicle("3.5T",vehicleMasterHashMap));	
		factoryVehicle.add(new Vehicle("2.5T", vehicleMasterHashMap));	
		bubbleSort(factoryVehicle);
		factoryVehicleHashMap.put("1D1", factoryVehicle);
		
		factoryVehicle = new ArrayList<Vehicle>();
		factoryVehicle.add(new Vehicle("14T", vehicleMasterHashMap));
		factoryVehicle.add(new Vehicle("4.5T", vehicleMasterHashMap));	
		factoryVehicle.add(new Vehicle("5T", vehicleMasterHashMap));
		factoryVehicle.add(new Vehicle("3.5T",vehicleMasterHashMap));	
		factoryVehicle.add(new Vehicle("2.5T", vehicleMasterHashMap));	
		bubbleSort(factoryVehicle);
		factoryVehicleHashMap.put("2D1", factoryVehicle);
		
		factoryVehicle = new ArrayList<Vehicle>();
		factoryVehicle.add(new Vehicle("14T", vehicleMasterHashMap));
		factoryVehicle.add(new Vehicle("4.5T", vehicleMasterHashMap));	
		factoryVehicle.add(new Vehicle("8T", vehicleMasterHashMap));
		factoryVehicle.add(new Vehicle("5T", vehicleMasterHashMap));
		factoryVehicle.add(new Vehicle("3.5T",vehicleMasterHashMap));	
		factoryVehicle.add(new Vehicle("2.5T", vehicleMasterHashMap));	
		bubbleSort(factoryVehicle);
		factoryVehicleHashMap.put("3D1", factoryVehicle);
		
		factoryVehicle = new ArrayList<Vehicle>();
		factoryVehicle.add(new Vehicle("14T", vehicleMasterHashMap));
		factoryVehicle.add(new Vehicle("4.5T", vehicleMasterHashMap));	
		factoryVehicle.add(new Vehicle("5T", vehicleMasterHashMap));
		factoryVehicle.add(new Vehicle("3.5T",vehicleMasterHashMap));	
		factoryVehicle.add(new Vehicle("2.5T", vehicleMasterHashMap));	
		bubbleSort(factoryVehicle);
		factoryVehicleHashMap.put("4D1", factoryVehicle);
		
		
	}
	
	
	
	public void load_itemMaster() {
		HashMap<String, String> isContainHashmap = new HashMap<String, String>();
		
		List<Item> itemList = svc.findAll();
		
		ArrayList<Item> _itemList = new ArrayList<Item>();
		for(Item it : itemList) {
			if (isContainHashmap.get(it.getItemCd()) == null) {
				_itemList.add(it);
				isContainHashmap.put(it.getItemCd(), it.getItemCd());
			}
		}
		
		System.out.println("[ItemCount]:\t" + itemList.size() +":"+ _itemList.size());
		isContainHashmap.clear();
		
		totalNumOfItem = _itemList.size();
		items = new IItem[totalNumOfItem];
		itemHashmap = new HashMap<String, Integer>();
			
		int i = 0;
		for(Item it : _itemList) {
		
			
			items[i] = new IItem(it.getItemNm(), it.getItemCd(), it.getBoxCnt(), it.getPaletteCnt());
			itemHashmap.put(it.getItemCd(), i);		
			
			if (items[i].getPerBox() != null && items[i].getPerPallet() != null && items[i].getPerBox() == 1 && items[i].getPerPallet() == 1) {
				System.out.println("[getPerBox = 1 or getPerPallet == 1] ========>" + it.getItemCd());
			}
			i++;
		}
		
		System.out.println("[totalNumOfItem]\t:" + totalNumOfItem);
		System.out.println("[itemMaster size]:\t" + itemHashmap.size());
	}
	
	@SuppressWarnings("unchecked")
	public void load_itemTransferMaster() {
		
		
		items_transfered = (ArrayList<Transfer>[])(new ArrayList[totalNumOfItem]);
		for (int i=0; i < totalNumOfItem; i++) {
			items_transfered[i] = new ArrayList<Transfer>();
		}
		
		
		List<TransferPlan> itemTransferList  = svc.getProductPlanItems(this.modelID);
		System.out.println("[itemTransferList size]:\t" + itemTransferList.size());

		
		for(TransferPlan itp : itemTransferList) {
			
			if (itemHashmap.get(itp.getItemCd()) == null) {
				System.out.println("[not found]:\t" + itp.getItemCd());	
			}
			else {	
				if (items[itemHashmap.get(itp.getItemCd())].getPerBox() != 0 
						&& items[itemHashmap.get(itp.getItemCd())].getPerPallet() != 0) 
				{
					if (itp.getRatioBy1D1() > 0.0) {
						Transfer transfer = new Transfer(graph.getVertexbyID("1D1"), itp.getRatioBy1D1());	
						items_transfered[itemHashmap.get(itp.getItemCd())].add(transfer);
					}
					else if (itp.getRatioBy2D1()> 0.0) {
						Transfer transfer = new Transfer(graph.getVertexbyID("2D1"), itp.getRatioBy2D1());
						items_transfered[itemHashmap.get(itp.getItemCd())].add(transfer);
						System.out.println("--------------------------");
						System.out.println(itp.getItemCd());
						System.out.println(itp.getRatioBy1D1());
						System.out.println(itp.getRatioBy2D1());
						System.out.println(itp.getRatioBy3D1());
						System.out.println(itp.getRatioBy4D1());
						System.out.println("--------------------------");
					}	
					else if (itp.getRatioBy3D1()> 0.0) {
						Transfer transfer = new Transfer(graph.getVertexbyID("3D1"), itp.getRatioBy3D1());
						items_transfered[itemHashmap.get(itp.getItemCd())].add(transfer);
					}
					else if (itp.getRatioBy4D1()> 0.0) {
						Transfer transfer = new Transfer(graph.getVertexbyID("4D1"), itp.getRatioBy4D1());
						items_transfered[itemHashmap.get(itp.getItemCd())].add(transfer);
					}
					
				}
				else {
					System.out.println("[getPerBox = 0 or getPerPallet == 0]:\t" + itp.getItemCd());
					
					
					
					
					
					
					
				}
			}  
		}	
		

				
	}
	
	
	public void load_validItemMaster(String curDate) {
		double output0 = 0.0;
		double output1 = 0.0;
		double output2 = 0.0;
		
		
		List<ItemShipmentByRoute> itemList = searchAllShipmentQtyHashmap.get(curDate);
		
		if (itemList == null) return;
		
		for(ItemShipmentByRoute its : itemList) {
			output1 += its.getShipQty();
						
			if (its.getFctryCd().equals(graph.getOriginVertex().getID()) != true || its.getHas() != 1) {
				continue;
			}
			
			
			if (itemHashmap.get(its.getItemCd()) == null) {
				System.out.println(its.getItemCd());
				System.out.println(its.getShipQty());
				output2 += its.getShipQty();
			}
			else {
				if (items[itemHashmap.get(its.getItemCd())].getPerBox() != 0 
						&& items[itemHashmap.get(its.getItemCd())].getPerPallet() != 0) 
				{
					validItemMasterHashmap.put(its.getItemCd(), its.getShipQty());	
					
					output0 += its.getShipQty();
				}
				else {
					System.out.println("[getPerBox = 0 or getPerPallet == 0] ========>" + its.getItemCd() + "\t" + its.getShipQty());
					output2 += its.getShipQty();
				}
			}
		}
		
		System.out.println("[has=1 Item qty]:\t" + output0 );
		System.out.println("[not found Item qty]:\t" + output2 );
		System.out.println("[all Item qty]:\t" + output1);		
	}
	
	private void printTotalOutput() {
		
		double totalqty = 0.0;
		for (int i=0; i < this.totalNumOfItem; i++) {
			if (this.items[i].getOutput(graph.getOriginVertex()) != null)
				totalqty += this.items[i].getOutput(graph.getOriginVertex());
		}
		System.out.println("totalqty : " + totalqty);
		
	}
	
	
	public void load_itemOutputQty(String curDate) {
		double output1 = 0.0;
		double output2 = 0.0;
		double output3 = 0.0;
		
		Vertex originVertex = graph.getOriginVertex();

		
		List<ItemShipmentByRoute> itemList = searchAllShipmentQtyHashmap.get(curDate);
		if (itemList == null) return;
		
		for(ItemShipmentByRoute its : itemList) {
			output1 += its.getShipQty();
			
			if (its.getFctryCd().equals(originVertex.getID()) != true || its.getHas() != 1) continue;
			
			if (itemHashmap.get(its.getItemCd()) == null) {
				System.out.println(its.getItemCd());
				System.out.println(its.getShipQty());
				output2 += its.getShipQty();
			}
			else {	
				double shipqty = 0.0;
				shipqty = its.getShipQty();

				
				
				
		
				
				if (items[itemHashmap.get(its.getItemCd())].getPerBox() != 0 
						&& items[itemHashmap.get(its.getItemCd())].getPerPallet() != 0) 
				{
					originVertex.addItem(items[itemHashmap.get(its.getItemCd())]);
					items[itemHashmap.get(its.getItemCd())].setOutput(originVertex, shipqty);
					output3 += shipqty;
				}
				else {
					System.out.println("[getPerBox = 0 or getPerPallet == 0] ========>" + its.getItemCd() + "\t" + its.getShipQty());
					output2 += its.getShipQty();
				}
		
				
				
				
				
				
			}
		}	
		
		System.out.println("=====================");
		System.out.println("[All] : " + output1  + "\t[NOT_F P=0] : " + output2 + "\t[has=1] : " + output3 );
		printTotalOutput();
		System.out.println("=====================");
		
	}
	
	private void _plus_shipqty(ArrayList<String> fList, String transCD, String shipCD, double _shipqty, double ratio, Double[] d_number, Double[][] d_number_sub) {
		if (transCD == null || shipCD == null) return;
		
		for (int i=0; i < 4; i++) {
			if (transCD.equals(fList.get(i)) == true) {
				d_number[i] += (_shipqty*ratio);
				
				for (int j=0; j < 4; j++) {
					if (shipCD.equals(fList.get(j)) == true) {
						d_number_sub[i][j] += (_shipqty*ratio);
						break;
					}
				}
				break;
			}
		}
	}
	
	private void _plus_transqty(ArrayList<String> fList, String transCD, String shipCD, double _shipqty, double ratio, Double[] d_number, Double[][] d_number_sub) {
		
		if (transCD == null || shipCD == null) return;
		
		for (int i=0; i < 4; i++) {
			if (shipCD.equals(fList.get(i)) == true) {
				d_number[i] += (_shipqty*ratio);
				
				for (int j=0; j < 4; j++) {
					if (transCD.equals(fList.get(j)) == true) {
						d_number_sub[i][j] += (_shipqty*ratio);
						break;
					}
				}
				break;
			}
		}
	}
	
	
	public void report_shipqty(String defaultTransCD, String curDate) {

		if (this._log_verify_print == false) return;
		
		List<ItemShipmentByRoute> itemList = searchAllShipmentQtyHashmap.get(curDate);
		if (itemList == null) return;

		for(ItemShipmentByRoute its : itemList) {
			if (itemHashmap.get(its.getItemCd()) == null) continue;
			
			if (items[itemHashmap.get(its.getItemCd())].getPerBox() == 0 
					|| items[itemHashmap.get(its.getItemCd())].getPerPallet() == 0) 
			{
				continue;
			}
			double _shipqty = its.getShipQty();
			
			if (its.getHas() == 1) {	
				this.ship_2d1_has_1 += _shipqty;	
				
				if (items_transfered[itemHashmap.get(its.getItemCd())].size() == 0) {
					 _plus_transqty(factoryList, defaultTransCD, its.getFctryCd(), _shipqty, 1.0, d_has1, d_has1_sub);
				}
				else {
					for (int tidx=0; tidx < items_transfered[itemHashmap.get(its.getItemCd())].size(); tidx++) {
						Transfer transfer = items_transfered[itemHashmap.get(its.getItemCd())].get(tidx);
						_plus_transqty(factoryList, transfer.vertex.getID(), its.getFctryCd(), _shipqty, transfer.ratio, d_has1, d_has1_sub);
					}	
				}				
			}
			else {
				this.ship_2d1_has_0 += _shipqty;	
				
				if (items_transfered[itemHashmap.get(its.getItemCd())].size() == 0) {
					 _plus_shipqty(factoryList, defaultTransCD, its.getNewPoint(), _shipqty, 1.0, d_has0, d_has0_sub);
				}
				else {
					for (int tidx=0; tidx < items_transfered[itemHashmap.get(its.getItemCd())].size(); tidx++) {
						Transfer transfer = items_transfered[itemHashmap.get(its.getItemCd())].get(tidx);
						_plus_shipqty(factoryList, transfer.vertex.getID(), its.getNewPoint(), _shipqty, transfer.ratio, d_has0, d_has0_sub);
					}
				}
			}
		}
	}
	
	
	public void report_transferqty(String defaultTransCD, String curDate) {
		if (this._log_verify_print == false) return;
		
		List<ItemTransport> TransferList2 = searchOutGoingTransportItemsHashmap.get(curDate);
		if (TransferList2 == null) return;
		
		
		for(ItemTransport itt : TransferList2) {	
			if (itemHashmap.get(itt.getItemCd()) == null) continue;
			
			Vertex fromvertex = graph.getVertexbyID(itt.getFrom());
			Vertex tovertex = graph.getVertexbyID(itt.getTo());
				
			if (fromvertex == null || tovertex == null) continue;
			if (fromvertex == tovertex) {
				continue;
			}
			if (tovertex.getID().equals(graph.getOriginVertex().getID()) == true) continue;
				
			if (items[itemHashmap.get(itt.getItemCd())].getPerBox() == 0 || items[itemHashmap.get(itt.getItemCd())].getPerPallet() == 0) {
				continue;
			}
			
			double _tqty = itt.getShipmentQty();
			
			if (items_transfered[itemHashmap.get(itt.getItemCd())].size() == 0) {
				_plus_transqty(factoryList, defaultTransCD, itt.getTo(), _tqty, 1.0, d_out, d_out_sub);
			}
			else {
				for (int tidx=0; tidx < items_transfered[itemHashmap.get(itt.getItemCd())].size(); tidx++) {
					Transfer transfer = items_transfered[itemHashmap.get(itt.getItemCd())].get(tidx);
					_plus_transqty(factoryList, transfer.vertex.getID(), itt.getTo(), _tqty, transfer.ratio, d_out, d_out_sub);
				}
			}
		
		}

	}
	
	
	
	
	
	
	public void load_itemRoutes(String curDate) {
		
		oldRouteList = new ArrayList<Route>();
		oldRouteHashMap = new HashMap<String, Route>();
		newRouteList = new ArrayList<Route>();
		newRouteHashMap = new HashMap<String, Route>();
		
		
		
		
		
		
		
		
		
		
		double _log_output = 0.0;
		double dropqty[] = {0.0, 0.0, 0.0, 0.0};
		
		List<ItemTransport> TransferList2 = searchOutGoingTransportItemsHashmap.get(curDate);
		if (TransferList2 == null) return;
		
		for(ItemTransport itt : TransferList2) {	
			

			if (itemHashmap.get(itt.getItemCd()) == null) {
				System.out.println(itt.getItemCd());
				System.out.println(itt.getShipmentQty());
			}
			else {
				
				
				
				
				double itemqty = 0.0;		
				itemqty = itt.getShipmentQty();
				Vertex fromvertex = graph.getVertexbyID(itt.getFrom());
				Vertex tovertex = graph.getVertexbyID(itt.getTo());
				
				if (fromvertex == null || tovertex == null) continue;
				if (fromvertex == tovertex) {
					continue;
				}
				if (tovertex.getID().equals(graph.getOriginVertex().getID()) == true) continue;
				if (items[itemHashmap.get(itt.getItemCd())].getPerBox() == 0 || items[itemHashmap.get(itt.getItemCd())].getPerPallet() == 0) continue;
										
				
				if (items[itemHashmap.get(itt.getItemCd())].getOutput(graph.getOriginVertex()) == null) {
					graph.getOriginVertex().addItem(items[itemHashmap.get(itt.getItemCd())]);
					items[itemHashmap.get(itt.getItemCd())].setOutput(graph.getOriginVertex(), itemqty);
					
				}
				else {
					items[itemHashmap.get(itt.getItemCd())].updateNewOutput(
							graph.getOriginVertex(), 
							items[itemHashmap.get(itt.getItemCd())].getOutput(graph.getOriginVertex()) + itemqty);	
				}
					
				_log_output += itemqty;
				
			
				Route route = new Route();
				route.addVertex(fromvertex);
				route.addVertex(tovertex);
				route.addItem(items[itemHashmap.get(itt.getItemCd())], itt.getShipmentQty());
				addToNewRouteList(oldRouteList, oldRouteHashMap, route) ;
				
				
				
				
				
			}
		}
		
		System.out.println("=====================");
		System.out.println("OUT qty:" + _log_output);
		System.out.println("will be drop: 1D1:\t" + dropqty[0] + "\t2D1:\t" + dropqty[1] + "\t3D1:\t" + dropqty[2] + "\t4D1:\t" + dropqty[3]);
		printTotalOutput();
		System.out.println("=====================");
	}
	
	
	public void clear_data() {
		
		
		
		
		
		if (oldRouteList != null) {
			for (int i=0; i < oldRouteList.size(); i++) {
				Route r = oldRouteList.get(i);
				r.clear();
			}		
			oldRouteList.clear();
			oldRouteList = null;
		}
		if (oldRouteHashMap != null) {
			oldRouteHashMap.clear();
			oldRouteHashMap = null;	
		}
	

		if (newRouteList != null) {
			for (int i=0; i < newRouteList.size(); i++) {
				Route r = newRouteList.get(i);
				r.clear();
			}
			newRouteList.clear();
			newRouteList = null;
		}
		if (newRouteHashMap != null) {
			newRouteHashMap.clear();	
			newRouteHashMap = null;	
		}

		
		
		
		
		
		validItemMasterHashmap.clear();
		
		
		
		graph.getOriginVertex().clearItem();
		graph.clearItemofAllVertex();
		for (int i=0; i < this.totalNumOfItem; i++) {
			this.items[i].clearOutput();
		}
	}
	
	
	
	private Route createRoute(Vertex startVertex, Route oldroute, IItem item, double volume) {
		Route newroute = new Route();
		newroute.addVertex(startVertex);	
		for (int i=1; i < oldroute.getTotalNumofVertex(); i++) {
			newroute.addVertex(oldroute.getVertex(i));
		}
		newroute.addItem(item, volume);	
		return newroute;
	}
	
	
	private Route cloneRouteAndSetItem(Route oldroute, IItem item, double volume) {		
		Route newroute = new Route();
		
		for (int i=0; i < oldroute.getTotalNumofVertex(); i++) {
			newroute.addVertex(oldroute.getVertex(i));
		}
		newroute.addItem(item, volume);	
		return newroute;
	}
	
	
	private Route createRoute(Vertex startVertex, Vertex finalVertex, IItem item, double volume) {	
		Route newroute = new Route();
		newroute.addVertex(startVertex);	
		newroute.addVertex(finalVertex);
		newroute.addItem(item, volume);
		return newroute;
	}
	
	
	private void addToNewRouteList(ArrayList<Route> routeList, HashMap<String, Route> routeHashMap , Route route) {
		
		if (routeHashMap.get(route.getStrVlist()) != null) {
			Route existroute = routeHashMap.get(route.getStrVlist());							

			
			existroute.addItem(route.getItem(0), route.getShipVolume(0));
			
			route.clear();
			route = null;
		}
		else {
			
			routeList.add(route);
			routeHashMap.put(route.getStrVlist(), route);
		}
	}
	
	
	
	public void GGenerateNewRoute(String defaultTransCD) {
		double _log_case21_AB = 0.0;
		double _log_totalqty_A = 0.0;
		double _log_case2_AB_AC = 0.0;
		double _log_case3_BA = 0.0;
		double _log_newqty = 0.0;
		
		boolean _log_print = false;
		
		
		Vertex origin_vertex = graph.getOriginVertex();
			
		for (int iidx=0; iidx < totalNumOfItem; iidx++) {
			
			int size = items_transfered[iidx].size();
			
			if (size == 0) {
				if (defaultTransCD == null) {
					continue;
				}
				
				Transfer transfer = new Transfer(graph.getVertexbyID(defaultTransCD), 1.0);
				items_transfered[itemHashmap.get(items[iidx].getID())].add(transfer);
			}
			
			
			for (int tidx=0; tidx < items_transfered[iidx].size(); tidx++) {
			
				if (items[iidx].getOutput(origin_vertex) == null) {
					continue;
				}
				
				
				Transfer transfer1 = items_transfered[iidx].get(tidx);
				
				if (_log_print) {
					System.out.println(graph.getOriginVertex().getID() + " (" + items[iidx].getID() + ") =(T)=> "  + transfer1.vertex.getID() + ":" + transfer1.ratio);
					System.out.println(graph.getOriginVertex().getID() + " (" + items[iidx].getID() + ") : "  + items[iidx].getOutput(origin_vertex));
				}
				
				_log_totalqty_A += items[iidx].getOutput(origin_vertex);
								
				double case_2_AB = 0.0;
				double case_2_AC = 0.0;
				double case_3_BA = 0.0;
				
				
				
				double case_1_A_totalOutput = (items[iidx].getOutput(origin_vertex) * transfer1.ratio);
				
				
				for (int ridx=0; ridx < oldRouteList.size(); ridx ++) {
					Route oldr = oldRouteList.get(ridx);
										
					if (oldr.getItemIdx(items[iidx].getID()) != null) {
						int itemIdx = oldr.getItemIdx(items[iidx].getID());

						
						if (transfer1.vertex == oldr.getLastVertex()) {
							
							
							
							
							case_2_AB += (oldr.getShipVolume(itemIdx));
							
							_log_case21_AB += case_2_AB;
							
							
							
						}
						
						else if (transfer1.vertex == oldr.getFirstVertex()) {
							
							
							
							
							case_3_BA += oldr.getShipVolume(itemIdx);	
							_log_case3_BA += case_3_BA;
						}
						
						else if (graph.getOriginVertex() == oldr.getLastVertex()) {
							
							
							
							
						
							Route newroute = cloneRouteAndSetItem(oldr,  items[iidx], oldr.getShipVolume(itemIdx)) ;
							if (_log_print) {
								System.out.println(items[iidx].getID());
								System.out.println("new route case4==:" + (oldr.getShipVolume(itemIdx) ));			
								
								newroute.printRoute();
							}
							addToNewRouteList(newRouteList, newRouteHashMap, newroute) ;
						}
						
						else if (graph.getOriginVertex() == oldr.getFirstVertex()) {	
							
							
							
							
							
							
							
							
							boolean route_lastvertex_is_transfer_factory = false;
							for (int tidx2=0; tidx2 < items_transfered[iidx].size(); tidx2++) {
								Transfer transfer2 = items_transfered[iidx].get(tidx2);
								if (transfer1 != transfer2 && oldr.getLastVertex() == transfer2.vertex) {
									route_lastvertex_is_transfer_factory = true;
									break;
								}
							}
							
							if (route_lastvertex_is_transfer_factory == false) {
								if ((oldr.getShipVolume(itemIdx) * transfer1.ratio) > 0) {
									case_2_AC += (oldr.getShipVolume(itemIdx) * transfer1.ratio);
																		
									
									Route newroute = createRoute(transfer1.vertex,  oldr,  items[iidx], (oldr.getShipVolume(itemIdx) * transfer1.ratio) ) ;
									if (_log_print) {
										System.out.println("new route case22==:" + ( oldr.getShipVolume(itemIdx) * transfer1.ratio ));
										newroute.printRoute();
									}
									addToNewRouteList(newRouteList, newRouteHashMap, newroute) ;
									
								}
								else {
									
								}
							}
							
							else {

							}

						}
						else {
							System.out.println("------------ not considering ------------");
						}
					}
				} 
							
				if (_log_print) {
					System.out.println("------------------------------------");
					System.out.print("case1:" + case_1_A_totalOutput);
					System.out.print("\tcase21:" + case_2_AB);
					System.out.print("\tcase22:" + case_2_AC);
					System.out.print("\tcase3:" + case_3_BA);
					System.out.print("\tnewqty:");
					
					System.out.println(case_1_A_totalOutput-case_2_AB-case_2_AC + case_3_BA);
				}


				Route newroute = null;				
				newroute = createRoute(transfer1.vertex,  origin_vertex, items[iidx], case_1_A_totalOutput-case_2_AB-case_2_AC + case_3_BA) ;
				if (_log_print) {
					System.out.println("new route:" + "(" + (case_1_A_totalOutput-case_2_AB-case_2_AC + case_3_BA) + ")");
					newroute.printRoute();
				}
				addToNewRouteList(newRouteList, newRouteHashMap, newroute) ;
				_log_newqty += (case_1_A_totalOutput-case_2_AB-case_2_AC + case_3_BA);					
				
				if (_log_print) {
					System.out.println("------------------------------------");	
				}
				
				_log_case2_AB_AC += (case_2_AB+case_2_AC);
			} 
	
		} 
		
		
		System.out.println("=================================");
		System.out.println(_log_totalqty_A +"\t"+_log_case2_AB_AC + "\t" + _log_case3_BA + "\t" + _log_newqty + "\t" + _log_case21_AB);
		System.out.println("=================================");

	}

	
	public void optRoute2() {
		double output = 0.0;
		
		
		for (int ii1=0; ii1 < newRouteList.size(); ii1++) {
			Route r_target1 = newRouteList.get(ii1);
			
			
			for (int jj1=0; jj1 < r_target1.getNumOfItem(); jj1++) {
				
				
				
				if (r_target1.getShipVolume(jj1) < 0) {
					
					
					
					
					int iidx1 = itemHashmap.get(r_target1.getItem(jj1).getID());
					
					
					for (int ii2=0; ii2 < newRouteList.size(); ii2++) {
						Route r_target2 = newRouteList.get(ii2);
												
						if (r_target1 != r_target2) {
							
							
							for (int jj2=0; jj2 < r_target2.getNumOfItem(); jj2++) {
								int iidx2 = itemHashmap.get(r_target2.getItem(jj2).getID());
								
								
								
								if (iidx1 == iidx2 && r_target2.getShipVolume(jj2) + r_target1.getShipVolume(jj1) > 0) {
									System.out.println(items[iidx1].getID() + ":" + r_target2.getShipVolume(jj2) + ":==" + r_target1.getShipVolume(jj1));
									
									
									output +=  (r_target1.getShipVolume(jj1) * -1.0);
									Route newroute = createRoute(r_target2.getFirstVertex(),  r_target1.getFirstVertex(),  
											items[iidx1], r_target1.getShipVolume(jj1) * -1.0 ) ;
									addToNewRouteList(newRouteList, newRouteHashMap, newroute) ;
																		
									r_target2.updateVolume(jj2, r_target2.getShipVolume(jj2) + r_target1.getShipVolume(jj1));
									r_target1.updateVolume(jj1, 0);
									
									
									break;
								}
							} 
						}
						
						if (r_target1.getShipVolume(jj1) == 0.0) break;
					} 
					
				} 
			}  
		}
		
		System.out.println("=================================");
		System.out.println("OPT Result:\t" + output);
		System.out.println("=================================");

	}

	
	public void changeNewTo(ArrayList<Route> routeList, String oldPointCD, String newPointCD) {
		
		if (graph.getVertexbyID(newPointCD) == null) {
			graph.addVertex(newPointCD, newPointCD);
		}
		Vertex newVertex = graph.getVertexbyID(newPointCD);
		
		for (int i=0; i < routeList.size(); i++) {
			Route r = routeList.get(i);
			
			if (r.getLastVertex().getID().equals(oldPointCD) == true) {	
				r.removeVertex(r.getTotalNumofVertex()-1);
				r.addVertex(newVertex);
			}
		}	
	}
	
	public void printModel(ArrayList<Route> routeList, int type) {
		System.out.println("=================================");
		
		for (int i=0; i < routeList.size(); i++) {
			Route r = routeList.get(i);
			r.printRoute();	
			r.printItem(type);
		}
		System.out.println("=================================");	
	}
	
	
	public void removeZeroShipItem(ArrayList<Route> routeList) {
		if (routeList == null) return;
		
		for (int i=0; i < routeList.size(); i++) {
			Route r = routeList.get(i);
			int j = 0;
			while (j < r.getNumOfItem()) {
				if (r.getShipVolume(j) <= 0) 
					r.removeItem(j);
				else 
					j++;
			}
		}
		
		for (int i=0; i < routeList.size(); i++) {
			Route r = routeList.get(i);
			int j = 0;
			while (j < r.getNumOfItem()) {
				if (r.getFirstVertex() == r.getLastVertex()) 
					r.removeItem(j);
				else 
					j++;
			}
		}
	}
			
	private void _calculate_vehicle(double totalPT, ArrayList<Vehicle> vehicleFactory) {		
		if (vehicleFactory == null) {
			return;
		}
		
		int totalnum = vehicleFactory.size();
		int resultNum[] = new int[totalnum];		
		
		
		for (int vhidx=0; vhidx < totalnum; vhidx++) {
			
			
			if (vhidx > 0 && totalPT > 0) {
				if ((int) Math.ceil(totalPT % vehicleFactory.get(vhidx-1).getLimitPallet())  >  vehicleFactory.get(vhidx).getLimitPallet() ) {
					resultNum[vhidx-1] ++;
					totalPT -= (vehicleFactory.get(vhidx-1).getLimitPallet());	
				}				
			}	
			if (totalPT <= 0.0) break;
			resultNum[vhidx] = (int)(totalPT / vehicleFactory.get(vhidx).getLimitPallet());
			totalPT -= (resultNum[vhidx] * vehicleFactory.get(vhidx).getLimitPallet());
			
			
		}
		
		if (totalPT > 0) {
			resultNum[totalnum-1]++;
		}
		
		for (int vhidx=0; vhidx < totalnum; vhidx++) {
			vehicleFactory.get(vhidx).setAllocNum(resultNum[vhidx]);
		}
	}
	
	private void _setResult(ItemTransferAllocate itemTransferAllocate, ArrayList<Vehicle> vehicleFactory) {
		if (vehicleFactory == null) return;
		
		for (int vhidx=0; vhidx < vehicleFactory.size();vhidx++) {
			System.out.println(vehicleFactory.get(vhidx).getName() + ": " + vehicleFactory.get(vhidx).getAllocNum() + " vehicles");
			if (vehicleFactory.get(vhidx).getName().equals("14T")) {
				
				itemTransferAllocate.setTon_14(vehicleFactory.get(vhidx).getAllocNum());
			}
			else if (vehicleFactory.get(vhidx).getName().equals("4.5T")) {
				itemTransferAllocate.setTon_7_5(vehicleFactory.get(vhidx).getAllocNum());
			}
			else if (vehicleFactory.get(vhidx).getName().equals("8T")) {
				itemTransferAllocate.setTon_8(vehicleFactory.get(vhidx).getAllocNum());
			}
			else if (vehicleFactory.get(vhidx).getName().equals("5T")) {
				itemTransferAllocate.setTon_5(vehicleFactory.get(vhidx).getAllocNum());
			}
			else if (vehicleFactory.get(vhidx).getName().equals("3.5T")) {
				itemTransferAllocate.setTon_3_5(vehicleFactory.get(vhidx).getAllocNum());
			}
			else if (vehicleFactory.get(vhidx).getName().equals("2.5T")) {
				itemTransferAllocate.setTon_2_5(vehicleFactory.get(vhidx).getAllocNum());
			}
			vehicleFactory.get(vhidx).setAllocNum(0);
		}
	}
	
	private void check_reverse_pallet(ArrayList<Route> routeList, HashMap<String, Route> routeHashmap) {

		if (this._log_verify_print == false) return;
		
		
		for(int i=0; i < routeList.size(); i++) {
			Route route = routeList.get(i);
			
			if (route.getFirstVertex().getID().equals("1D1") == true && route.getLastVertex().getID().equals("4D1") == true) {
				_t_reverse += 1;
				String opp_str = route.getLastVertex().getID() + "|" + route.getFirstVertex().getID();
				Route opp_r = routeHashmap.get(opp_str);
				if (opp_r != null) {
					if (route.getTotalPallet() > opp_r.getTotalPallet() && route.getTotalVolume() < opp_r.getTotalVolume()) {	
						_reverse += 1;
					}
					else if (opp_r.getTotalPallet() > route.getTotalPallet() && opp_r.getTotalVolume() < route.getTotalVolume()) {	
						_reverse += 1;
					}
				}					
			}
		}
		
	}
	
	public void how_many_vehicles(ArrayList<Route> routeList, String curDate, int roundtrip) {	

		if (routeList == null) return;
		
		HashMap<String, Route> routeHashmap = null;
		if (roundtrip == 1) {
			routeHashmap = new HashMap<String, Route>();
			finalizeRoute(routeList, routeHashmap);
			
		}
		
		System.out.println("--------------------------");
		for(int i=0; i < routeList.size(); i++) {
			Route route = routeList.get(i);
								
			if (roundtrip == 1) {
				String opp_str = route.getLastVertex().getID() + "|" + route.getFirstVertex().getID();
				Route opp_r = routeHashmap.get(opp_str);
				if (opp_r != null && opp_r.getTotalPallet() > route.getTotalPallet()) {	
					route.setTotalPallet(0.0);
					route.setTotalVolume(0.0);
				}
				if (opp_r != null && opp_r.getTotalPallet() == route.getTotalPallet()) {
					opp_r.setTotalPallet(0.0);
					opp_r.setTotalVolume(0.0);
				}			
			}			
			
			ItemTransferAllocate itemTransferAllocate = new ItemTransferAllocate();
			itemTransferAllocate.setDlvyDe(curDate);
			itemTransferAllocate.setFrom(route.getFirstVertex().getID());
			itemTransferAllocate.setTo(route.getLastVertex().getID());
			
			
			ItemShipment [] itemShipments = new ItemShipment[route.getNumOfItem()];
			double totalPT = 	0.0;
			double qty = 0.0;
			
			for(int j=0; j < route.getNumOfItem(); j++) {	
				itemShipments[j] = new ItemShipment();
				itemShipments[j].setItemCd(route.getItem(j).getID());
				itemShipments[j].setShipmentQty(route.getShipVolume(j));			
				itemShipments[j].setBoxCnt(route.getItem(j).getPerBox());
				itemShipments[j].setPaletteCnt(route.getItem(j).getPerPallet());
				itemShipments[j].setPaletteQty(route.getItem(j).refinePallet(route.getShipVolume(j)));
				
				totalPT += route.getItem(j).refinePallet(route.getShipVolume(j));	
				qty += route.getShipVolume(j);
			}
			if (totalPT == 0) continue;
			
			itemTransferAllocate.setPaletteQty(totalPT);
			itemTransferAllocate.setItemShipMents(itemShipments);
			
			

			route.printRoute();
			System.out.println("=> total Pallet :: " + totalPT);
			System.out.println("=> total qty :: " + qty);
			
			if (roundtrip == 1) {
				_calculate_vehicle(route.getTotalPallet(), this.factoryVehicleHashMap.get(route.getFirstVertex().getID()));
			}
			else {
				_calculate_vehicle(totalPT, this.factoryVehicleHashMap.get(route.getFirstVertex().getID()));
			}
			
			_setResult(itemTransferAllocate, this.factoryVehicleHashMap.get(route.getFirstVertex().getID()));
			System.out.println("----");
			
			svc.insertNewRoute(this.modelID, itemTransferAllocate);
			
		} 
		System.out.println("--------------------------");
		System.out.println();
			
	}
	
	
	public void finalizeRoute(ArrayList<Route> routeList, HashMap<String, Route> routeHashmap) {	
		if (routeList == null) return;
		
		for(int i=0; i < routeList.size(); i++) {
			Route route = routeList.get(i);
			routeHashmap.put(route.getStrVlist(), route);
			
			double totalPT = 0.0;
			double totalqty = 0.0;
			
			for(int j=0; j < route.getNumOfItem(); j++) {		
				totalPT += route.getItem(j).refinePallet(route.getShipVolume(j));	
				totalqty += route.getShipVolume(j);
			}
			
			route.setTotalPallet(totalPT);
			route.setTotalVolume(totalqty);
			
			
			
			String str = route.getFirstVertex().getID() + "|" + route.getLastVertex().getID();
			for (int j=0; j < this.not_assign_vehicle.size(); j++) {			
				if (str.equals(not_assign_vehicle.get(j)) == true) {
					route.setTotalPallet(0.0);
					route.setTotalVolume(0.0);
				}
			}
			
			
			
			
		} 

	}
	
	
	
	
	public void report_shipqty2(String curDate) {

		if (this._log_verify_print == false) return;
		
		List<ItemShipmentByRoute> itemList = searchAllShipmentQtyHashmap.get(curDate);
		if (itemList == null) return;

		
		for(ItemShipmentByRoute its : itemList) {
			if (itemHashmap.get(its.getItemCd()) == null) continue;
			
			if (items[itemHashmap.get(its.getItemCd())].getPerBox() == 0 
					|| items[itemHashmap.get(its.getItemCd())].getPerPallet() == 0) 
			{
				continue;
			}
			
			
			
			double _shipqty = its.getShipQty();
			
			
			if (its.getHas() == 1) {	
				this.ship_2d1_has_1 += _shipqty;
				
				
				
				if (items_transfered[itemHashmap.get(its.getItemCd())].size() == 0) {
					this.ship_2d1_has_1_[0] += (_shipqty*1.0);
				}
				else {
					for (int tidx=0; tidx < items_transfered[itemHashmap.get(its.getItemCd())].size(); tidx++) {
						Transfer transfer = items_transfered[itemHashmap.get(its.getItemCd())].get(tidx);
						if (transfer.vertex.getID().equals("1D1") == true) this.ship_2d1_has_1_[0] += (_shipqty*transfer.ratio);
						else if (transfer.vertex.getID().equals("2D1") == true) this.ship_2d1_has_1_[1] += (_shipqty*transfer.ratio);
						else if (transfer.vertex.getID().equals("3D1") == true) this.ship_2d1_has_1_[2] += (_shipqty*transfer.ratio);
						else if (transfer.vertex.getID().equals("4D1") == true) this.ship_2d1_has_1_[3] += (_shipqty*transfer.ratio);
						
					}	
				}

				
			}
			else {				
				this.ship_2d1_has_0 += _shipqty;	
				
				
				
				
				if (items_transfered[itemHashmap.get(its.getItemCd())].size() == 0) {
					this.ship_2d1_has_0_T1 += (_shipqty*1.0);
					
					if (its.getNewPoint().equals("1D1") == true) this.ship_2d1_has_0_T1_[0]+= (_shipqty*1.0);
					else if (its.getNewPoint().equals("2D1") == true) this.ship_2d1_has_0_T1_[1] += (_shipqty*1.0);
					else if (its.getNewPoint().equals("3D1") == true) this.ship_2d1_has_0_T1_[2] += (_shipqty*1.0);
					else if (its.getNewPoint().equals("4D1") == true) this.ship_2d1_has_0_T1_[3] += (_shipqty*1.0);
					
				}
				else {
					for (int tidx=0; tidx < items_transfered[itemHashmap.get(its.getItemCd())].size(); tidx++) {
						Transfer transfer = items_transfered[itemHashmap.get(its.getItemCd())].get(tidx);
						
						if (transfer.vertex.getID().equals("1D1") == true) {
							this.ship_2d1_has_0_T1 += (_shipqty*transfer.ratio);
							if (its.getNewPoint().equals("1D1") == true) this.ship_2d1_has_0_T1_[0] += (_shipqty*transfer.ratio);
							else if (its.getNewPoint().equals("2D1") == true) this.ship_2d1_has_0_T1_[1] += (_shipqty*transfer.ratio);
							else if (its.getNewPoint().equals("3D1") == true) this.ship_2d1_has_0_T1_[2] += (_shipqty*transfer.ratio);
							else if (its.getNewPoint().equals("4D1") == true) this.ship_2d1_has_0_T1_[3] += (_shipqty*transfer.ratio);
						}
						else if (transfer.vertex.getID().equals("3D1") == true) {
							this.ship_2d1_has_0_T3 += (_shipqty*transfer.ratio);
							if (its.getNewPoint().equals("1D1") == true) this.ship_2d1_has_0_T3_[0] += (_shipqty*transfer.ratio);
							else if (its.getNewPoint().equals("2D1") == true) this.ship_2d1_has_0_T3_[1] += (_shipqty*transfer.ratio);
							else if (its.getNewPoint().equals("3D1") == true) this.ship_2d1_has_0_T3_[2] += (_shipqty*transfer.ratio);
							else if (its.getNewPoint().equals("4D1") == true) this.ship_2d1_has_0_T3_[3] += (_shipqty*transfer.ratio);
						}
						else if (transfer.vertex.getID().equals("4D1") == true) {
							this.ship_2d1_has_0_T4 += (_shipqty*transfer.ratio);
							if (its.getNewPoint().equals("1D1") == true) this.ship_2d1_has_0_T4_[0] += (_shipqty*transfer.ratio);
							else if (its.getNewPoint().equals("2D1") == true) this.ship_2d1_has_0_T4_[1] += (_shipqty*transfer.ratio);
							else if (its.getNewPoint().equals("3D1") == true) this.ship_2d1_has_0_T4_[2] += (_shipqty*transfer.ratio);
							else if (its.getNewPoint().equals("4D1") == true) this.ship_2d1_has_0_T4_[3] += (_shipqty*transfer.ratio);
						}
						
						
					}
				}
			} 
			
		}
		
		
	}
	
	
	
	public void report_transferqty2(String curDate) {
		if (this._log_verify_print == false) return;
		
		List<ItemTransport> TransferList2 = searchOutGoingTransportItemsHashmap.get(curDate);
		if (TransferList2 == null) return;
		
		
		for(ItemTransport itt : TransferList2) {	
			if (itemHashmap.get(itt.getItemCd()) == null) {
			}
			else {
				Vertex fromvertex = graph.getVertexbyID(itt.getFrom());
				Vertex tovertex = graph.getVertexbyID(itt.getTo());
					
				if (fromvertex == null || tovertex == null) continue;
				if (fromvertex == tovertex) {
					continue;
				}
				if (tovertex.getID().equals(graph.getOriginVertex().getID()) == true) continue;
					
				if (items[itemHashmap.get(itt.getItemCd())].getPerBox() == 0 || items[itemHashmap.get(itt.getItemCd())].getPerPallet() == 0) {
					continue;
				}
			
				
				
					
				
				double _tqty = itt.getShipmentQty();
					
				if (itt.getTo().equals("1D1") == true) {
					this.out_2d1_1d1 += _tqty;
						
					if (items_transfered[itemHashmap.get(itt.getItemCd())].size() == 0) {
						this.out_2d1_1d1_T_[0] += (_tqty*1.0);
					}
					else {
						for (int tidx=0; tidx < items_transfered[itemHashmap.get(itt.getItemCd())].size(); tidx++) {
							Transfer transfer = items_transfered[itemHashmap.get(itt.getItemCd())].get(tidx);
								
							if (transfer.vertex.getID().equals("1D1") == true) {
								this.out_2d1_1d1_T_[0] += (_tqty*transfer.ratio);
							}
							else if (transfer.vertex.getID().equals("2D1") == true) {
								this.out_2d1_1d1_T_[1] += (_tqty*transfer.ratio);
							}
							else if (transfer.vertex.getID().equals("3D1") == true) {
								this.out_2d1_1d1_T_[2] += (_tqty*transfer.ratio);
							}
							else if (transfer.vertex.getID().equals("4D1") == true) {
								this.out_2d1_1d1_T_[3] += (_tqty*transfer.ratio);	
							}
							
						}
					}
				}
				else if (itt.getTo().equals("3D1") == true) {
					this.out_2d1_3d1 += _tqty;
					if (items_transfered[itemHashmap.get(itt.getItemCd())].size() == 0) {
						this.out_2d1_3d1_T_[0] += (_tqty*1.0);
					}
					else {
						for (int tidx=0; tidx < items_transfered[itemHashmap.get(itt.getItemCd())].size(); tidx++) {
							Transfer transfer = items_transfered[itemHashmap.get(itt.getItemCd())].get(tidx);
								
							if (transfer.vertex.getID().equals("1D1") == true) {
								this.out_2d1_3d1_T_[0] += (_tqty*transfer.ratio);
							}
							else if (transfer.vertex.getID().equals("2D1") == true) {
								this.out_2d1_3d1_T_[1] += (_tqty*transfer.ratio);
							}
							else if (transfer.vertex.getID().equals("3D1") == true) {
								this.out_2d1_3d1_T_[2] += (_tqty*transfer.ratio);
							}
							else if (transfer.vertex.getID().equals("4D1") == true) {
								this.out_2d1_3d1_T_[3] += (_tqty*transfer.ratio);
							}
							
						}
					}
				}
				else if (itt.getTo().equals("4D1") == true) {
					this.out_2d1_4d1 += _tqty;
					if (items_transfered[itemHashmap.get(itt.getItemCd())].size() == 0) {
						this.out_2d1_4d1_T_[0] += (_tqty*1.0);
					}
					else {
							for (int tidx=0; tidx < items_transfered[itemHashmap.get(itt.getItemCd())].size(); tidx++) {
								Transfer transfer = items_transfered[itemHashmap.get(itt.getItemCd())].get(tidx);
								
								if (transfer.vertex.getID().equals("1D1") == true) {
									this.out_2d1_4d1_T_[0] += (_tqty*transfer.ratio);	
								}
								else if (transfer.vertex.getID().equals("2D1") == true) {
									this.out_2d1_4d1_T_[1] += (_tqty*transfer.ratio);	
								}
								else if (transfer.vertex.getID().equals("3D1") == true) {
									this.out_2d1_4d1_T_[2] += (_tqty*transfer.ratio);	
								}
								else if (transfer.vertex.getID().equals("4D1") == true) {
									this.out_2d1_4d1_T_[3] += (_tqty*transfer.ratio);	
								}
								
							}
					}
				}
				
			}
		}
	}
	
}
