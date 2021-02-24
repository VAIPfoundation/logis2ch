package com.sdc2ch.service.model.cost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

@Getter
public class RouteTonTollCostBuilder {
	private Map<RouteNo, List<RouteTonTollCost>> mapped;

	private RouteTonTollCostBuilder() {
		this.mapped = init();
	}
	
	private static RouteTonTollCostBuilder builder;
	public static RouteTonTollCostBuilder getInstance () {
		if(builder == null)
			builder = new RouteTonTollCostBuilder();
		return builder;
	}
	
	public RouteTonTollCost getTollCost(RouteNo routeNo, CarTonType ton) {
		List<RouteTonTollCost> list = mapped.get(routeNo);
		if(list != null) {
			return list.stream().filter(c -> c.getCarTon() == ton).findFirst().orElse(null);
		}
		return null;
	}
	@Getter
	public class RouteTonTollCost {
		private RouteNo routeNo;
		private CarTonType carTon;
		private int tollCost;
		
		public RouteTonTollCost(RouteNo routeNo, CarTonType ton, int cost) {
			this.routeNo = routeNo;
			this.carTon = ton;
			this.tollCost = cost;
		}
	}


	public Map<RouteNo, List<RouteTonTollCost>> init() {
		
		Map<RouteNo, List<RouteTonTollCost>> mapped = new HashMap<>();
		RouteNo rn = null;
		List<RouteTonTollCost> costs = new ArrayList<>();
		RouteTonTollCost tollCost = null;
		
		
		costs = new ArrayList<>();
		rn = RouteNo.ROUTE01;
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_14, 6800);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_11, 6800);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_8, 5600);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_7_5, 5600);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_5, 5600);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_4_5, 6800);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_3_5, 5600);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_2_5, 5600);
		costs.add(tollCost);
		mapped.put(rn, costs);
		
		
		costs = new ArrayList<>();
		rn = RouteNo.ROUTE02;
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_14, 13200);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_11, 13200);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_8, 10800);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_7_5, 10800);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_5, 10800);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_4_5, 13200);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_3_5, 10800);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_2_5, 10800);
		costs.add(tollCost);
		mapped.put(rn, costs);
		
		
		costs = new ArrayList<>();
		rn = RouteNo.ROUTE03;
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_14, 39400);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_11, 39400);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_8, 28600);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_7_5, 28600);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_5, 28000);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_4_5, 39400);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_3_5, 28000);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_2_5, 28000);
		costs.add(tollCost);
		mapped.put(rn, costs);
		
		
		costs = new ArrayList<>();
		rn = RouteNo.ROUTE04;
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_14, 6800);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_11, 6800);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_8, 5600);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_7_5, 5600);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_5, 5600);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_4_5, 6800);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_3_5, 5600);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_2_5, 5600);
		costs.add(tollCost);
		mapped.put(rn, costs);
		
		
		
		costs = new ArrayList<>();
		rn = RouteNo.ROUTE05;
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_14, 5400);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_11, 5400);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_8, 4600);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_7_5, 4600);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_5, 4600);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_4_5, 5400);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_3_5, 4600);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_2_5, 4600);
		costs.add(tollCost);
		mapped.put(rn, costs);
		
		
		
		costs = new ArrayList<>();
		rn = RouteNo.ROUTE06;
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_14, 31800);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_11, 31800);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_8, 24200);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_7_5, 24200);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_5, 23400);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_4_5, 31800);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_3_5, 23400);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_2_5, 23400);
		costs.add(tollCost);
		mapped.put(rn, costs);
		
		
		costs = new ArrayList<>();
		rn = RouteNo.ROUTE07;
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_14, 13200);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_11, 13200);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_8, 10800);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_7_5, 10800);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_5, 10800);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_4_5, 13200);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_3_5, 10800);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_2_5, 10800);
		costs.add(tollCost);
		mapped.put(rn, costs);
		
		
		
		costs = new ArrayList<>();
		rn = RouteNo.ROUTE08;
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_14, 5400);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_11, 5400);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_8, 4600);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_7_5, 4600);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_5, 4600);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_4_5, 5400);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_3_5, 4600);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_2_5, 4600);
		costs.add(tollCost);
		mapped.put(rn, costs);
		
		
		
		costs = new ArrayList<>();
		rn = RouteNo.ROUTE09;
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_14, 36600);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_11, 36600);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_8, 27800);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_7_5, 27800);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_5, 26800);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_4_5, 36600);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_3_5, 26800);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_2_5, 26800);
		costs.add(tollCost);
		mapped.put(rn, costs);
		
		
		
		
		costs = new ArrayList<>();
		rn = RouteNo.ROUTE10;
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_14, 39400);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_11, 39400);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_8, 28600);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_7_5, 28600);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_5, 28000);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_4_5, 39400);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_3_5, 28000);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_2_5, 28000);
		costs.add(tollCost);
		mapped.put(rn, costs);
		
		
		
		
		costs = new ArrayList<>();
		rn = RouteNo.ROUTE11;
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_14, 31800);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_11, 31800);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_8, 24200);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_7_5, 24200);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_5, 23400);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_4_5, 31800);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_3_5, 23400);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_2_5, 23400);
		costs.add(tollCost);
		mapped.put(rn, costs);
		
		
		
		
		costs = new ArrayList<>();
		rn = RouteNo.ROUTE12;
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_14, 36600);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_11, 36600);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_8, 27800);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_7_5, 27800);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_5, 26800);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_4_5, 36600);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_3_5, 26800);
		costs.add(tollCost);
		tollCost = new RouteTonTollCost(rn, CarTonType.TON_2_5, 26800);
		costs.add(tollCost);
		mapped.put(rn, costs);
		
		return mapped;
	}
}
