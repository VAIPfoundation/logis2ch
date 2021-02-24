package com.sdc2ch.service.model.cost;

public enum CarTonType {
	TON_14(14, 16),
	TON_11(11, 16),
	TON_8(8, 10),
	TON_7_5(7.5, 10),
	TON_5(5, 8),
	TON_4_5(4.5, 14),
	TON_3_5(3.5, 4),
	TON_2_5(2.5, 3)
	;
	public double ton;
	public double parretVolume;
	CarTonType(double ton, double volume){
		this.ton = ton;
		this.parretVolume = volume;
	}
	public double getParretVolume() {
		return parretVolume;
	}
	
}
