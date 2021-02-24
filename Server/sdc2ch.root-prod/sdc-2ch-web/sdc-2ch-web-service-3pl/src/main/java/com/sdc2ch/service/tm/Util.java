package com.sdc2ch.service.tm;

import java.util.ArrayList;
import java.util.HashMap;

public class Util {
	static public void calculate_vehicle(double totalPT, String startID) {
		
		HashMap<String, Integer> vehicleMasterHashMap = new HashMap<String, Integer>();
		HashMap<String, ArrayList<Vehicle>> factoryVehicleHashMap = new HashMap<String, ArrayList<Vehicle>>();
		
		vehicleMasterHashMap.put("14T", 	16);
		vehicleMasterHashMap.put("4.5T", 	14);
		vehicleMasterHashMap.put("8T", 		10);
		vehicleMasterHashMap.put("5T", 		8);
		vehicleMasterHashMap.put("3.5T", 	4);
		vehicleMasterHashMap.put("2.5T", 	3);
		
		ArrayList<Vehicle> factoryVehicle = new ArrayList<Vehicle>();
		factoryVehicle.add(new Vehicle("14T", vehicleMasterHashMap));
		factoryVehicle.add(new Vehicle("8T", vehicleMasterHashMap));
		factoryVehicle.add(new Vehicle("5T", vehicleMasterHashMap));
		factoryVehicle.add(new Vehicle("3.5T",vehicleMasterHashMap));	
		factoryVehicle.add(new Vehicle("2.5T", vehicleMasterHashMap));	
		factoryVehicleHashMap.put("1D1", factoryVehicle);
		
		factoryVehicle = new ArrayList<Vehicle>();
		factoryVehicle.add(new Vehicle("14T", vehicleMasterHashMap));
		factoryVehicle.add(new Vehicle("4.5T", vehicleMasterHashMap));	
		factoryVehicle.add(new Vehicle("5T", vehicleMasterHashMap));
		factoryVehicle.add(new Vehicle("3.5T",vehicleMasterHashMap));	
		factoryVehicle.add(new Vehicle("2.5T", vehicleMasterHashMap));	
		factoryVehicleHashMap.put("2D1", factoryVehicle);
		
		factoryVehicle = new ArrayList<Vehicle>();
		factoryVehicle.add(new Vehicle("14T", vehicleMasterHashMap));
		factoryVehicle.add(new Vehicle("4.5T", vehicleMasterHashMap));	
		factoryVehicle.add(new Vehicle("8T", vehicleMasterHashMap));
		factoryVehicle.add(new Vehicle("5T", vehicleMasterHashMap));
		factoryVehicle.add(new Vehicle("3.5T",vehicleMasterHashMap));	
		factoryVehicle.add(new Vehicle("2.5T", vehicleMasterHashMap));	
		factoryVehicleHashMap.put("3D1", factoryVehicle);
		
		factoryVehicle = new ArrayList<Vehicle>();
		factoryVehicle.add(new Vehicle("14T", vehicleMasterHashMap));
		factoryVehicle.add(new Vehicle("4.5T", vehicleMasterHashMap));	
		factoryVehicle.add(new Vehicle("5T", vehicleMasterHashMap));
		factoryVehicle.add(new Vehicle("3.5T",vehicleMasterHashMap));	
		factoryVehicle.add(new Vehicle("2.5T", vehicleMasterHashMap));	
		factoryVehicleHashMap.put("4D1", factoryVehicle);
		
		
		
		ArrayList<Vehicle> vehicleFactory = factoryVehicleHashMap.get(startID);
		int totalnum = vehicleFactory.size();
		int results[] = new int[totalnum];		
		
		
		for (int vhidx=0; vhidx < totalnum; vhidx++) {
			
			if (vhidx > 0 && totalPT > 0) {
				if ((int) Math.ceil(totalPT % vehicleFactory.get(vhidx-1).getLimitPallet())  >  vehicleFactory.get(vhidx).getLimitPallet() ) {
					results[vhidx-1] ++;
					totalPT -= (vehicleFactory.get(vhidx-1).getLimitPallet());	
				}				
			}	
			if (totalPT <= 0.0) break;
			results[vhidx] = (int)(totalPT / vehicleFactory.get(vhidx).getLimitPallet());
			totalPT -= (results[vhidx] * vehicleFactory.get(vhidx).getLimitPallet());
		}
		
		if (totalPT > 0) {
			results[totalnum-1]++;
		}
		
		
		for (int vhidx=0; vhidx < totalnum; vhidx++) {
			System.out.println(results[vhidx]);
		}
		
		vehicleFactory.clear();
		results = null;
	}
}
