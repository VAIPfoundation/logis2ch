package com.sdc2ch.prcss.ds.vo;

import java.util.Date;
import java.util.Map;

import com.sdc2ch.prcss.ds.io.ShippingState;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShipStateVo {
	
	public ShipStateVo(Long id) {
		this.allocatedGroupId = id;
	}
	
	private String routeNo;
	private String vrn;
	private int squareBoxQty;
	private String dlvyDe;
	
	public final long allocatedGroupId;
	public Date allocatedDt;
	
	private long curDlvyLoId;
	
	private ShippingState state;
	
	private String curDlvyLoNm;
	
	private String curArrivedScheduledTime;
	
	
	private String nextDlvylcNm;
	
	private String nextArrivedScheduledTime;
	
	private int emptyboxTotal;
	private int emptyboxComplete;
	
	private Map<ShippingState, Long> stateTimeMap;

}
