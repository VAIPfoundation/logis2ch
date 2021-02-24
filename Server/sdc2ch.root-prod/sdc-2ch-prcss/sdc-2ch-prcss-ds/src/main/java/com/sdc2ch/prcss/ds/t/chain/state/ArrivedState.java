package com.sdc2ch.prcss.ds.t.chain.state;

import com.sdc2ch.prcss.ds.core.IShipping;
import com.sdc2ch.prcss.ds.event.IProcessEvent;
import com.sdc2ch.prcss.ds.io.ShippingState;
import com.sdc2ch.prcss.ds.t.chain.ShippingChain;
import com.sdc2ch.prcss.ds.t.chain.ShippingChain.ChainNm;

import lombok.extern.slf4j.Slf4j;

 
@Slf4j
public class ArrivedState extends ShippingStatus {

	public ArrivedState(ShippingChain chain) {
		super(chain, StateNm.ARRIVED);
	}

	@Override
	public IShipping onCreate(IShipping context) {



		return super.onCreate(context);
	}








	@Override
	public void onReceive(IProcessEvent event) {
		
		super.onReceive(event);

	}

	@Override
	public ShippingState getShippingState() {
		ShippingState st = null;
		ChainNm nm = getParent().chainName();
		switch (nm) {
		case CUSTOMER_CENTER:
			st = ShippingState.ARRIVE;
			break;
		case FACTORY:
			st = ShippingState.ENTER;
			break;
		case WAREHOUSE:
			st = ShippingState.ARRIVE;
			break;
		default:
			break;
		}
		return st;
	}







}
