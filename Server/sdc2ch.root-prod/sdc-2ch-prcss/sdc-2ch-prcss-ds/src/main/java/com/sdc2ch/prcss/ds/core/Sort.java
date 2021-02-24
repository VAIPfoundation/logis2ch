package com.sdc2ch.prcss.ds.core;

import java.util.Comparator;
import java.util.List;

import com.google.common.collect.ComparisonChain;
import com.sdc2ch.prcss.ds.t.chain.ShippingChain;




public class Sort implements Comparator<ShippingChain> {
	
	public static enum SortedType {
		ROUTE_NO,
		DLVY_LC_SEQ,
		DLVY_LC_RE_INDEX,
		DLVY_LC_TIME,
		ROUTE_RE_IDX;
	}
	private List<SortedType> sortList;
	
	Sort(List<SortedType> sortList){
		this.sortList = sortList;
	}
	
	
	@Override
	public int compare(ShippingChain o1, ShippingChain o2) {
		
		ComparisonChain chain = ComparisonChain.start();
		
		for(SortedType s : sortList) {
			switch(s) {
			case DLVY_LC_SEQ:
				chain = chain.compare(o1.shipConfig.dlvyLcSeq, o2.shipConfig.dlvyLcSeq);
				break;
			case DLVY_LC_TIME:
				chain = chain.compare(o1.shipConfig.plannedATime, o2.shipConfig.plannedATime);
				break;
			case ROUTE_NO:
				chain = chain.compare(o1.shipConfig.routeNo, o2.shipConfig.routeNo);
				break;
			case DLVY_LC_RE_INDEX:
				chain = chain.compare(o1.shipConfig.getDlvyLoReIdx(), o2.shipConfig.getDlvyLoReIdx());
				break;
			case ROUTE_RE_IDX:
				chain = chain.compare(o1.shipConfig.getRouteReIdx(), o2.shipConfig.getRouteReIdx());
				break;
			}
		}
		return chain.result();
	}
}
