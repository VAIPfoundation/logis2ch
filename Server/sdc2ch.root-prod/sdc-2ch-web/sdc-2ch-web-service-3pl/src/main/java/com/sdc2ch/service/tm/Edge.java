package com.sdc2ch.service.tm;


public class Edge {
	
	private Vertex v1;
	private Vertex v2;
	
	public Edge(Vertex v1, Vertex v2) {
		this.v1 = v1;
		this.v2 = v2;
	}
	
	public void printEdge() {
		System.out.println(v1.getID() + "--->" + v2.getID()  );
	}
}
