package com.sdc2ch.service.model.cost;

public enum RouteNo {
	ROUTE01("1D1","2D1", 0.7, 146),
	ROUTE02("1D1","3D1", 0.8, 162),
	ROUTE03("1D1","4D1", 1.25, 638),
	ROUTE04("2D1","1D1", 0.7, 146),
	ROUTE05("2D1","3D1", 0.5, 78),
	ROUTE06("2D1","4D1", 1, 518),
	ROUTE07("3D1","1D1", 0.8, 162),
	ROUTE08("3D1","2D1", 0.5, 78),
	ROUTE09("3D1","4D1", 1.08, 578),
	ROUTE10("4D1","1D1", 1.25, 638),
	ROUTE11("4D1","2D1", 1, 518),
	ROUTE12("4D1","3D1", 1.08, 578),
	
	;
	public String from;
	public String to;
	public double trunRate;
	public int distance;
	RouteNo(String from, String to, double trunRate, int distance){
		this.from = from;
		this.to = to;
		this.trunRate = trunRate;
		this.distance = distance;
	}
	
	public String genarateKey() {
		return from + to;
	}
}
