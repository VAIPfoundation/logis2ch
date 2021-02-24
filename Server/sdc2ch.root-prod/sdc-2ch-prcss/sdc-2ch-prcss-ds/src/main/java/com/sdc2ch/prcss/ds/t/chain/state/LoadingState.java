package com.sdc2ch.prcss.ds.t.chain.state;



import com.sdc2ch.prcss.ds.core.IShipping;
import com.sdc2ch.prcss.ds.core.ShippingPlan;
import com.sdc2ch.prcss.ds.event.IProcessEvent;
import com.sdc2ch.prcss.ds.io.ShippingState;
import com.sdc2ch.prcss.ds.t.chain.ShippingChain;

import lombok.extern.slf4j.Slf4j;

 
@Slf4j
public class LoadingState extends ShippingStatus {
	
	public LoadingState(ShippingChain chain) {
		super(chain, StateNm.LOAD);
	}
	
	@Override
	public IShipping onCreate(IShipping context) {
		ShippingPlan plan = chain.shipConfig;
		return super.onCreate(context);
	}

	@Override
	public void onReceive(IProcessEvent event) {
		log.info("정상처리 이벤트를 보낸다. {} -> {}" ,this , event);
		super.setEvent(event);
		super.onChange(this);
	}
	
	@Override
	public com.sdc2ch.prcss.ds.io.ShippingState getShippingState() {
		return ShippingState.LOADING;
	}
}
