package com.sdc2ch.service.factory;

import java.math.BigDecimal;

public enum CarTon {
	
	largevan,
	largetruck,
	specialtruck,
	car
	
	;

	public static CarTon convert(String carWegit) {
		int carTon = new BigDecimal(carWegit).intValue();
		CarTon carType = null;
		if(carTon <= 8) {
			carType = largevan;
		}else if(carTon <= 11) {
			carType = largetruck;
		}else if(carTon <= 20) {
			carType = specialtruck;
		}else {
			carType = car;
		}
		return carType;
	}
}
