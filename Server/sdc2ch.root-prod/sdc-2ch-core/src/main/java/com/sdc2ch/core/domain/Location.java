package com.sdc2ch.core.domain;

import java.math.BigDecimal;


public class Location {
	
	private final int radius;
	private final BigDecimal lat;
	private final BigDecimal lng;
	
	public Location(int radius, BigDecimal lat, BigDecimal lng) {
		this.radius = radius;
		this.lat = lat;
		this.lng = lng;
	}
	
	public int getRadius() {return radius;}
	public BigDecimal getLat() {return lat;}
	public BigDecimal getLng() {return lng;}
}
