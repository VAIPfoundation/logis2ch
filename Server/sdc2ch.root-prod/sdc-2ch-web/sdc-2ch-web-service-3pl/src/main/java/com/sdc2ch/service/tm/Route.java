package com.sdc2ch.service.tm;

import java.util.ArrayList;
import java.util.HashMap;

public class Route {
	private String str_vlist;
	
	private ArrayList<Vertex> vertexList;
	
	private double finalTotalVolume;
	private double finalTotalPallet;
	
	private ArrayList<IItem> itemList;
	private ArrayList<Double> shipVolumeList;
	
	HashMap<String, Integer> itemHashmap;
	
	public Route() {
		vertexList = new ArrayList<Vertex>();
		str_vlist = "";
		itemList = new ArrayList<IItem>();
		shipVolumeList = new ArrayList<Double>();
		
		itemHashmap = new HashMap<String, Integer>();
		
		
		this.finalTotalVolume = 0.0;
		this.finalTotalPallet = 0.0;
	}
	
	public void setTotalVolume(double qty) {
		this.finalTotalVolume = qty;
	}
	
	public void setTotalPallet(double pallet) {
		this.finalTotalPallet = pallet;
	}
	
	public double getTotalVolume() {
		return this.finalTotalVolume;
	}
	
	public double getTotalPallet() {
		return this.finalTotalPallet;
	}
	
	public void clear() {
		vertexList.clear();
		itemList.clear();
		shipVolumeList.clear();
		
		itemHashmap.clear();
	}
	
	public int getTotalNumofVertex() {
		return vertexList.size();
	}
	
	public void addVertex(Vertex vertex) {
		vertexList.add(vertex);
		
		if (str_vlist.length() > 0) {
			str_vlist = str_vlist.concat("|");
		}
 		str_vlist = str_vlist.concat(vertex.getID());
	}
	
	public void removeVertex(int idx) {
		vertexList.remove(idx);
		
		str_vlist = "";
		for (int i=0; i < this.getTotalNumofVertex(); i++) {
			str_vlist = str_vlist.concat(vertexList.get(i).getID());
			
			if (i < this.getTotalNumofVertex()-1) {
				str_vlist = str_vlist.concat("|");
			}
		}
	}
	
	public Vertex getVertex(int idx) {
		if (idx < 0 || idx >= vertexList.size()) return null;
		return vertexList.get(idx);
	}
	
	public Vertex getFirstVertex() {
		if (vertexList.size() == 0) return null;
		return vertexList.get(0);
	}
	
	public Vertex getLastVertex() {
		if (vertexList.size() == 0) return null;
		return vertexList.get(vertexList.size()-1);
	}

	
	
	

	
	public int getNumOfItem() {
		return itemList.size();
	}
	
	public void addItem(IItem item, double shipVol) {
		if (itemHashmap.get(item.getID()) != null) {
			
			int idx = itemHashmap.get(item.getID());
			double exist_vol = shipVolumeList.get(idx);
			
			updateVolume(idx,  exist_vol+shipVol);
		}
		else {
			itemList.add(item);
			shipVolumeList.add(shipVol);
			
			itemHashmap.put(item.getID(), itemList.size()-1);
		}
	}
	
	public void removeItem(int idx) {
		itemHashmap.remove(itemList.get(idx).getID());
		itemList.remove(idx);
		shipVolumeList.remove(idx);	
	}
	
	
	public IItem getItem(int idx) {
		return itemList.get(idx);
	}
	
	public Integer getItemIdx(String iID) {
		return itemHashmap.get(iID);
	}

	public double getShipVolume(int idx) {
		return shipVolumeList.get(idx);
	}
	
	public void updateVolume(int idx, double new_Volume) {
		shipVolumeList.remove(idx);
		shipVolumeList.add(idx, new_Volume);
	}
	
	public void printRoute() {
		for(int i=0; i < vertexList.size(); i++) {
			if (i == vertexList.size() - 1) {
				System.out.print(vertexList.get(i).getID());
			}
			else {
				System.out.print(vertexList.get(i).getID() + "->");
			}
		}
		System.out.println();	
	}
	
	public void printItem(int type) {
		
		double qty = 0.0;
		for(int i=0; i < itemList.size(); i++) {
			if (itemList.get(i).getPerBox() == 0 || itemList.get(i).getPerPallet() == 0) {
				
			}
			else {
				if (type == 1) {
					System.out.print(itemList.get(i).getID() + "\t" + shipVolumeList.get(i) + "\t" +  itemList.get(i).getPerBox() + "\t"  + itemList.get(i).getPerPallet() 
							+ "\t" + itemList.get(i).refinePallet(shipVolumeList.get(i)));
					System.out.println();
				}
			
				qty += shipVolumeList.get(i);
			}
		}
		System.out.println("Total\t" + qty);	
	}
	
	
	public String getStrVlist() {
		return this.str_vlist;
	}
	
	
	public boolean compareRoute(String vlist) {
		return this.str_vlist.equals(vlist);
	}
}
