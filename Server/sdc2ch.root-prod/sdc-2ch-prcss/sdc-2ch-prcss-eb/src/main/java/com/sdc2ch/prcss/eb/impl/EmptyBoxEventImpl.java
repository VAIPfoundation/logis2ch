package com.sdc2ch.prcss.eb.impl;

import com.sdc2ch.prcss.eb.event.IEmptyboxEvent;
import com.sdc2ch.require.domain.IUser;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmptyBoxEventImpl implements IEmptyboxEvent {
	private String dlvyLcId;
	private String dlvyLcNm;
	private String dlvyDe;
	private long timeStamp;
	private String cause;
	private IUser user;
	
	private int squareBoxQty;
	
	
	private int triangleBoxQty;
	
	
	private int yodelryBoxQty;

	
	private int palletQty;
	private String routeNo;
	
	@Override
	public IUser user() {
		return user;
	}

}
