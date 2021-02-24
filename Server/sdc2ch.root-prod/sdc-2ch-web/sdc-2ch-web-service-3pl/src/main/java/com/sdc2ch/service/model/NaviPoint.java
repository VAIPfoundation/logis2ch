package com.sdc2ch.service.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NaviPoint {
	private double lat;
	private double lng;
	private String name;
	
	public double plus() {
		return lat + lng;
	}

	
	public double plusThousand(int pathIndex) {
		return (lat + lng) + (1000+pathIndex);
	}
}
