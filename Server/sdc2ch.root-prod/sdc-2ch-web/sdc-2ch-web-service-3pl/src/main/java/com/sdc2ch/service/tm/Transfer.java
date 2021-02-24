package com.sdc2ch.service.tm;


public class Transfer {
	public Vertex vertex;
	public double ratio;
	public double volume;
	
	public Transfer(Vertex vertex, double ratio) {
		this.vertex = vertex;
		this.ratio = ratio;
		this.volume = 0.0;
	}
}
