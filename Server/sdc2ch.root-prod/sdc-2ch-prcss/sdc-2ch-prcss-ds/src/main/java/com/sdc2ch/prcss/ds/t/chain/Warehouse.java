package com.sdc2ch.prcss.ds.t.chain;

import com.sdc2ch.prcss.ds.core.AbstractShippingState;
import com.sdc2ch.prcss.ds.core.ShippingPlan;
import com.sdc2ch.prcss.ds.core.ShippingPlanContext;
import com.sdc2ch.prcss.ds.t.chain.ShippingChain.ChainNm;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Warehouse extends ShippingChain {

	public Warehouse(ShippingPlanContext chainContext, ShippingPlan config) {
		super(chainContext, config);
		setChainName(ChainNm.WAREHOUSE);
	}
	
	@Override
	protected void onChange(AbstractShippingState current) {


		if(current != this.current) {
			
			if (chainName() != ChainNm.FACTORY && current == states.stream().reduce((f, l) -> l).get()) {
				states.forEach(s -> s.forceComplete());
				completed = true;
			}

			this.setCurrent(current);
			super.onChange(this);
		}

		
	}
}
