package com.sdc2ch.prcss.ds.t.chain.state;


import com.sdc2ch.prcss.ds.core.IShipping;
import com.sdc2ch.prcss.ds.event.IProcessEvent;
import com.sdc2ch.prcss.ds.io.ShippingState;
import com.sdc2ch.prcss.ds.t.chain.ShippingChain;
import com.sdc2ch.prcss.ds.t.chain.ShippingChain.ChainNm;

import lombok.extern.slf4j.Slf4j;

 

@Slf4j
public class DepartState extends ShippingStatus {

	public DepartState(ShippingChain chain) {
		super(chain, StateNm.DEPART);
	}
	
	@Override
	public IShipping onCreate(IShipping context) {
		return super.onCreate(context);
	}
	
	@Override
	public void onReceive(IProcessEvent event) {
		log.info("정상처리 이벤트를 보낸다. {}" ,this);
		super.onReceive(event);
	}

	@Override
	public ShippingState getShippingState() {
		ShippingState st = null;
		ChainNm nm = getParent().chainName();
		switch (nm) {
		case CUSTOMER_CENTER:
			st = ShippingState.DELIVERY;
			break;
		case FACTORY:
			st = ShippingState.EXIT;
			break;
		case WAREHOUSE:
			st = ShippingState.DELIVERY;
			break;
		default:
			break;
		}
		return st;
	}
	
}
