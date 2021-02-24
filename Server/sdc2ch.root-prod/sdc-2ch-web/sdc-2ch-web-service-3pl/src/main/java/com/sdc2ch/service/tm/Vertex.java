package com.sdc2ch.service.tm;

import java.util.ArrayList;

public class Vertex {

	private String name;
	private String vID;
	private double x;
	private double y;
	
	private ArrayList<IItem> itemList = new ArrayList<IItem>();
	
	public Vertex(String id, String name) {
		this.vID = id;
		this.name = name;
		this.x = 0.0;
		this.y = 0.0;
	}	
	
	public String getID() {
		return this.vID;
	}
	
	
	public int NumOfItem() {
		return itemList.size();
	}
	
	public void addItem(IItem item) {
		itemList.add(item);
		
	}
	
	
	
	
	
	public IItem getItem(int idx) {
		return itemList.get(idx);
	}
	
	public void clearItem() {
		itemList.clear();
	}
		
}
