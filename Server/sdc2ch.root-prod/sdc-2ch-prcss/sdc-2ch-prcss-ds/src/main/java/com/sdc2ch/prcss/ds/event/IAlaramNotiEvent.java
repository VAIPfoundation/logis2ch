package com.sdc2ch.prcss.ds.event;

import com.sdc2ch.require.event.I2ChInternalEvent;

public interface IAlaramNotiEvent extends I2ChInternalEvent<IAlaramNotiEvent> {
	
	public enum AlaramType {
		
		NOT_YET_ALLOCATED,
		
		NOT_YET_FRONT,
		
		
	}
	
	AlaramType getAlaramTy();

}
