package com.sdc2ch.service.model.cost;

public enum PaymentFuelQty {
	TON_14(CarTonType.TON_14, 0.36, 1654),
	TON_11(CarTonType.TON_11, 0.36, 1654),
	TON_8(CarTonType.TON_8, 0.24, 1654),
	TON_7_5(CarTonType.TON_7_5, 0.24, 1654),
	TON_5(CarTonType.TON_5, 0.21, 1654),
	TON_4_5(CarTonType.TON_4_5, 0.34, 1654),
	TON_3_5(CarTonType.TON_3_5, 0.17, 1654),
	TON_2_5(CarTonType.TON_2_5, 0.17, 1654);
	public CarTonType ctt;
	public double liter;
	public int costOfOil;
	PaymentFuelQty(CarTonType ctt, double liter, int cost){
		this.ctt = ctt;
		this.liter = liter;
		this.costOfOil = cost;
	}
}
