package com.sdc2ch.service.tm;

import java.util.ArrayList;
import java.util.HashMap;

public class Graph {
	
	private ArrayList<Vertex> vertexList;
	private HashMap<String, Vertex> vertexHash;
	private Vertex originVertex;
	
	private ArrayList<Edge> edgeList;
	
	
	public Graph() {
		vertexList = new ArrayList<Vertex>();
		vertexHash = new HashMap<String, Vertex>();
		
		edgeList = new ArrayList<Edge>();
	}
	
	
	
	
	public Vertex getVertexbyIndex(int idx) {
		Vertex vertex = vertexList.get(idx);
		return vertex;
	}
	
	public Vertex getVertexbyID(String vID) {
		Vertex vertex = vertexHash.get(vID);
		return vertex;
	}

	public void addVertex(String vID, String name) {
		Vertex vertex = new Vertex(vID, name);
		vertexList.add(vertex);
		vertexHash.put(vID, vertex);
	}
	
	public int getNumOfVertex() {
		return vertexList.size();
	}
	
	public void setOriginVertex(String vID) {
		originVertex = vertexHash.get(vID);
	}
	

	public Vertex getOriginVertex() {
		return originVertex;
	}
		
	
	public void clearItemofAllVertex() {
		for (int i=0; i < this.getNumOfVertex(); i++) {
			vertexList.get(i).clearItem();
		}
	}
	
	
	
	
	public void addEdge(String vID1, String vID2) {
		Vertex v1 = getVertexbyID(vID1);
		Vertex v2 = getVertexbyID(vID2);
		
		if (v1 == null || v2 == null) {
			System.out.println("Not found vertex!!");
			return;
		}
		
		Edge edge = new Edge(v1, v2);
		edgeList.add(edge);
	}
	
	public Edge getEdgebyIndex(int idx) {
		Edge edge = edgeList.get(idx);
		return edge;
	}
	
	public int getNumOfEdge() {
		return edgeList.size();
	}
	
	
	public void dfs(Vertex curV) {
		
	}
}