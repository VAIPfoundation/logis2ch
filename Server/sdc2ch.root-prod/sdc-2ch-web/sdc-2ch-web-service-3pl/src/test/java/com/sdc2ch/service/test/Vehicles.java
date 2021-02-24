package com.sdc2ch.service.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.sdc2ch.service.model.cost.CarTonType;
import com.sdc2ch.service.model.item.ItemTransport;
import com.sdc2ch.service.transfer.FactoryType;
import com.sdc2ch.service.util.Utils;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
public class Vehicles {
	private ProductFactory factory;
	private CarTonType ton;
	private int numOfRetention; 
	private List<Vehicle> vehicles;
	private AtomicInteger inc = new AtomicInteger();
	
	public List<Vehicle> listVehicle() {
		
		if(vehicles == null) {
			vehicles = new ArrayList<>();
			for(int i = 0 ; i < numOfRetention ; i++) {
				String vrn = factory.getFctryTy().name.substring(0, 2);
				vrn = vrn + String.format("%03d", inc.incrementAndGet());
				vehicles.add(Vehicle.builder().factory(factory).ton(ton).vrn(vrn).shipInfos(new ArrayList<>()).build());
			}
		}
		return vehicles;
	}
	public void clear() {
		vehicles = null;
		inc.set(0);
	}
	@Override
	public String toString() {
		return Utils.toJsonString(this);
	}
	
	public Vehicle getTempCar() {
		String vrn = "용차";
		vrn = vrn + ton.ton + "T" + String.format("%03d", inc.incrementAndGet());
		Vehicle v = Vehicle.builder().factory(factory).ton(ton).vrn(vrn).shipInfos(new ArrayList<>()).build();
		vehicles.add(v);
		return v;
	}
	
	@Getter
	@Builder
	@Slf4j
	public static class Vehicle {
		private ProductFactory factory;
		private CarTonType ton; 
		private String vrn;
		@Setter
		private List<ItemTransport> shipInfos;
		
		private double parretQty;
		
		public boolean addVolume(ItemTransport item) {
			
			double parret = item.getPaletteQty();
			boolean isAdded = false;
			if(ton.parretVolume - parretQty >= parret) {
				parretQty += parret;
				isAdded = true;
				shipInfos.add(item);






			}
			return isAdded;
		}
		
		public void clear() {
			parretQty = 0;
			shipInfos = new ArrayList<>();
		}
		
		public double getVolumePercent() {
			return Double.valueOf(round(100.0 - (ton.parretVolume - parretQty) / ton.parretVolume * 100));
		}
		
		private String round(double parretQty) {
			return (Math.round(parretQty * 100) / 100.0) + "";
		}

		public void copy(Vehicle vehicle) {
			parretQty = vehicle.parretQty;
			shipInfos = vehicle.shipInfos;





		}
		
	}

	
	public static class VehiclesBuilder {
		private static List<VehicleTonMapper> mapper = createVehicleMap();
		private Vehicles vehicles = new Vehicles();
		private ProductFactory factory;
		private CarTonType ton;
		
		public static VehiclesBuilder builder() {
			return new VehiclesBuilder();
		}
		public VehiclesBuilder factory(ProductFactory factory) {
			this.factory = factory;
			return this;
		}
		public VehiclesBuilder ton(CarTonType ton) {
			this.ton = ton;
			return this;
		}
		
		public Vehicles build() {
			vehicles.factory = this.factory;
			vehicles.ton = this.ton;
			vehicles.numOfRetention = this.tonMapper();
			return vehicles;
			
		}
		private int tonMapper() {
			return mapper.stream().filter(m -> m.cty == this.ton && m.fty == this.factory.getFctryTy()).findFirst().get().nor;
		}
		private static List<VehicleTonMapper> createVehicleMap() {
			
			List<VehicleTonMapper> mapList = new ArrayList<>();
			





			int[] numsOf1D1 = {16, 1,  0,  0,  51,  0,   0, 0};
			int[] numsOf2D1 = { 5, 0,  0,  0,  31,  5,   0, 0};
			int[] numsOf3D1 = {17, 1,  0,  0,  61,  0,   0, 0};
			int[] numsOf4D1 = { 8, 0,  0,  0, 125,  8,   0, 0};
			int[] numsOfFFF = {999, 0, 0, 0, 0, 0, 0, 0};
			int[][] totNums = {numsOf1D1, numsOf2D1, numsOf3D1, numsOf4D1, numsOfFFF}; 
			
			int fIdx = 0;
			for(FactoryType f : FactoryType.values()) {
				int sIdx = 0;
				for(CarTonType t : CarTonType.values()) {
					mapList.add(new VehicleTonMapper(f, t, totNums[fIdx][sIdx++]));
				}
				fIdx++;
			}
			return mapList;
		}
		
		static class VehicleTonMapper {
			final FactoryType fty;
			final CarTonType cty;
			final int nor;
			VehicleTonMapper(FactoryType fty, CarTonType cty, int nor){
				this.fty= fty;
				this.cty= cty;
				this.nor= nor;
			}
		}
		
	}

}
