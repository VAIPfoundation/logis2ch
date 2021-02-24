package com.sdc2ch.service.tm;

import java.util.HashMap;

public class Vehicle {
	private String name;
	private int limitPallet;
	private int allocNum;
	
	public Vehicle(String name, int limitPallet) {
		this.name = name;
		this.limitPallet = limitPallet;
		this.allocNum = 0;
	}
	
	public Vehicle(String name, HashMap<String, Integer> palletHashmap) {
		this.name = name;
		this.limitPallet = palletHashmap.get(name);
		this.allocNum = 0;
	}
	
	public void setAllocNum(int num) {
		this.allocNum = num;
	}
	
	public int getAllocNum() {
		return this.allocNum;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getLimitPallet() {
		return this.limitPallet;
	}
	
	public void howmany(float pallet) {

	}
}