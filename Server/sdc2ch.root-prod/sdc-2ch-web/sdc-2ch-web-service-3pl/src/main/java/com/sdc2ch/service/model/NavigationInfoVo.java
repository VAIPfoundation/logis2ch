package com.sdc2ch.service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NavigationInfoVo {
	
	private int totalDistance;
	private int totalTollCost;
	private int totalTime;
	private int routePathCount;
	private String navigationJson;

}
