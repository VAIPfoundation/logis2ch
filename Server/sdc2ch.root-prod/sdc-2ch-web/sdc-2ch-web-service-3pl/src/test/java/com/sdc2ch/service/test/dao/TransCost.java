package com.sdc2ch.service.test.dao;

import java.math.BigDecimal;

import com.sdc2ch.service.test.Vehicles.Vehicle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransCost {
	
	private BigDecimal carTon; 
	private BigDecimal totTunRate; 
	private int contractCost; 
	private int totDistance; 
	private int costOfOil; 
	private BigDecimal totOilAmt; 
	private BigDecimal transitCost; 
	private BigDecimal totOilCost;
	private BigDecimal totTollCost;
	private BigDecimal totFullCost;
	private Vehicle vehicle;

}
