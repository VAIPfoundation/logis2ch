package com.sdc2ch.prcss.ds.t.chain;



import com.sdc2ch.prcss.ds.core.AbstractShippingState;
import com.sdc2ch.prcss.ds.core.IShipping;
import com.sdc2ch.prcss.ds.core.ShippingPlan;
import com.sdc2ch.prcss.ds.core.ShippingPlanContext;
import com.sdc2ch.prcss.ds.event.IProcessEvent;
import com.sdc2ch.prcss.ds.t.chain.ShippingChain.ChainNm;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Home extends ShippingChain {

	public Home(ShippingPlanContext chainContext, ShippingPlan config) {
		super(chainContext, config);
		setChainName(ChainNm.HOME);
	}
	
	@Override
	protected void onChange(AbstractShippingState current) {

		
		if (chainName() != ChainNm.FACTORY && current == states.stream().reduce((f, l) -> l).get()) {
			states.forEach(s -> s.forceComplete());
			completed = true;
		}
		this.setCurrent(current);
		super.onNext();
	}

	public void removeAlarm() {
		states.stream().forEach(s -> s.removeAlarm());
	}

}
