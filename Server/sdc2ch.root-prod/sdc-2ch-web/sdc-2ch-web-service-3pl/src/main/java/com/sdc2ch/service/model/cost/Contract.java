package com.sdc2ch.service.model.cost;

public enum Contract {

	TON_14(PaymentFuelQty.TON_14, 227000),
	TON_11(PaymentFuelQty.TON_11, 227000),
	TON_8(PaymentFuelQty.TON_8, 146967),
	TON_7_5(PaymentFuelQty.TON_7_5, 146967),
	TON_5(PaymentFuelQty.TON_5, 135200),
	TON_4_5(PaymentFuelQty.TON_4_5, 207000),
	TON_3_5(PaymentFuelQty.TON_3_5, 120300),
	TON_2_5(PaymentFuelQty.TON_2_5, 107000);
	public PaymentFuelQty ctt;
	public int contractCost;
	Contract(PaymentFuelQty ctt, int cost){
		this.ctt = ctt;
		this.contractCost = cost;
	}
	
	public PaymentFuelQty getPaymentFuelQty() {
		return ctt;
	}

	public Contract convert(CarTonType ton) {
		
		switch (ton) {
		case TON_11:
			return TON_11;
		case TON_14:
			return TON_14;
		case TON_2_5:
			return TON_2_5;
		case TON_3_5:
			return TON_3_5;
		case TON_4_5:
			return TON_4_5;
		case TON_5:
			return TON_5;
		case TON_7_5:
			return TON_7_5;
		case TON_8:
			return TON_8;
		default:
			break;
		}
		
		return null;
	}
}
