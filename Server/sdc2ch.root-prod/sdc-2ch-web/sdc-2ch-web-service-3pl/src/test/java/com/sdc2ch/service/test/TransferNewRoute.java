package com.sdc2ch.service.test;

import java.util.List;

import com.sdc2ch.service.test.Vehicles.Vehicle;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TransferNewRoute {
	private int id;
	public TransferNewRoute (int id){
		this.id= id;
	}
	private String dlvyDe;
	private ProductFactory from;
	private ProductFactory to;
	private String routeName;
	private List<Vehicle> numOfVehicles;
}
