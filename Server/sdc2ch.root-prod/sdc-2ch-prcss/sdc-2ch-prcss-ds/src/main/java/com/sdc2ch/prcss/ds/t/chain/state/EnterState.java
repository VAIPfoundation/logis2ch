package com.sdc2ch.prcss.ds.t.chain.state;


import com.sdc2ch.prcss.ds.core.IShipping;
import com.sdc2ch.prcss.ds.event.IProcessEvent;
import com.sdc2ch.prcss.ds.io.ShippingState;
import com.sdc2ch.prcss.ds.t.chain.ShippingChain;

import lombok.extern.slf4j.Slf4j;

 
@Slf4j
public class EnterState extends ShippingStatus {
	
	enum EnterStateLevel {
		LEVEL01("진입중"),
		LEVEL02(StateNm.ENTER.stateNm)
		;
		String nm;
		EnterStateLevel(String nm){
			this.nm = nm;
		}
		
	}

	public EnterState(ShippingChain chain) {
		super(chain, StateNm.ENTER);
	}
	
	@Override
	public IShipping onCreate(IShipping context) {



		return super.onCreate(context);
	}




	@Override
	public void onReceive(IProcessEvent event) {
		
		ShippingStatus state =  (ShippingStatus) super.getCurrentState();
		
		if(state == null) {
			super.onReceive(event);
		}
	}
	@Override
	public com.sdc2ch.prcss.ds.io.ShippingState getShippingState() {
		return ShippingState.START;
	}

}
