package com.sdc2ch.service.model.cost;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RouteInfo {
	private RouteNo routeNo;
	private List<Contract> contracts;
	private RouteTonTollCostBuilder tollCost;
	private Map<RouteNo, List<RouteTonTollCostBuilder>> mapped;

}
