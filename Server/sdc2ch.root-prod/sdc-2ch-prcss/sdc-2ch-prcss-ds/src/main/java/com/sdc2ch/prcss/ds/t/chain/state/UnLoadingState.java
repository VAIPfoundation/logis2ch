package com.sdc2ch.prcss.ds.t.chain.state;


import com.sdc2ch.prcss.ds.core.IShipping;
import com.sdc2ch.prcss.ds.event.IProcessEvent;
import com.sdc2ch.prcss.ds.io.ShippingState;
import com.sdc2ch.prcss.ds.t.chain.ShippingChain;
import com.sdc2ch.prcss.ds.t.chain.ShippingChain.ChainNm;

import lombok.extern.slf4j.Slf4j;

 
@Slf4j
public class UnLoadingState extends ShippingStatus {

	private int state;
	public UnLoadingState(ShippingChain chain) {
		super(chain, StateNm.UNLOAD);
	}
	
	@Override
	public IShipping onCreate(IShipping context) {
		return super.onCreate(context);
	}








	@Override
	public void onReceive(IProcessEvent event) {
		if(state == 0 || isEven()) {
			
			onComplete();
			log.info("정상처리 이벤트를 보낸다. {}" ,this);
		}
		state++;
	}
	




	
	private boolean isEven() {
		return state != 0 && state % 2 == 0;
	}
	
	public void hit() {
		super.hit();
	}
	@Override
	public ShippingState getShippingState() {
		ShippingState st = null;
		ChainNm nm = getParent().chainName();
		switch (nm) {
		case CUSTOMER_CENTER:
			st = ShippingState.TAKEOVER;
			break;
		case FACTORY:
			st = ShippingState.LOADING;
			break;
		case WAREHOUSE:
			st = ShippingState.LOADING;
			break;
		default:
			break;
		}
		return st;
	}
}
