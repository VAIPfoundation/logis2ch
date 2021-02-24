package com.sdc2ch.service.tm;

import java.util.ArrayList;
import java.util.HashMap;

public class IItem {

	private String itemName;
	private String itemID;
	private int numOfproducer;
	private double dailyTotalVolume;
	

	private HashMap<Vertex, Double> outputVolume;
	
	private Integer perBox;
	private Integer perPallet;
	
	public IItem(String itemName, String itemID, Integer perBox, Integer perPallet) {
		this.itemName = itemName;
		this.itemID = itemID;
		this.numOfproducer = 0;
		this.dailyTotalVolume = 0.0;
		
		if (perBox == null) this.perBox = 0;
		else this.perBox = perBox;
		
		if (perPallet == null) this.perPallet = 0;
		else this.perPallet = perPallet;
		
		outputVolume = new HashMap<Vertex, Double>();
		
		
	}
	
	public void updatePerBox(Integer perBox) {
		if (this.perBox == 0 && perBox != null) this.perBox = perBox; 
	}
	
	public void updatePerPallet(Integer perPallet) {		
		if (this.perPallet == 0 && perPallet != null) this.perPallet = perPallet; 
	}
	
	public String getName() {
		return this.itemName;
	}
	
	public String getID() {
		return this.itemID;
	}
	
	public void setOutput(Vertex vertex, double volume) {
		this.dailyTotalVolume += volume;
		this.numOfproducer ++;
		outputVolume.put(vertex, volume);
	}
	
	public void updateNewOutput(Vertex vertex, double volume) {
		double prevVolume = outputVolume.get(vertex);
		this.dailyTotalVolume -= prevVolume;
		outputVolume.remove(vertex);
		
		outputVolume.put(vertex, volume);
		this.dailyTotalVolume += volume;
	}
	
	public Double getOutput(Vertex vertex) {
		return outputVolume.get(vertex);
	}
	
	public void clearOutput() {
		this.numOfproducer = 0;
		this.dailyTotalVolume = 0.0;
		outputVolume.clear();
	}
	
	public int getNumofProducer() {
		return this.numOfproducer;
	}
	
	public double getDailyTotalVolume() {
		return this.dailyTotalVolume;
	}

	public Integer getPerBox() {
		return this.perBox;
	}
	
	public Integer getPerPallet() {
		return this.perPallet;
	}
	
	public int refinePalletCeil(double volume) {
		if (this.perBox == 0 || this.perPallet == 0) {
			System.out.println("=====================" + this.itemID);
			return 0;
		}
		return (int) Math.ceil( volume / this.perBox / this.perPallet );
	}
	
	public double refinePallet(double volume) {
		if (this.perBox == 0 || this.perPallet == 0) return 0;
		return (volume / this.perBox / this.perPallet );
	}
}
