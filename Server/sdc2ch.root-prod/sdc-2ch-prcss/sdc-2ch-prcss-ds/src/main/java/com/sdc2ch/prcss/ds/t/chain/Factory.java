package com.sdc2ch.prcss.ds.t.chain;

import com.sdc2ch.prcss.ds.core.IShipping;
import com.sdc2ch.prcss.ds.core.ShippingPlan;
import com.sdc2ch.prcss.ds.core.ShippingPlanContext;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Factory extends ShippingChain {
	
	public Factory(ShippingPlanContext chainContext, ShippingPlan config) {
		super(chainContext, config);
		setChainName(ChainNm.FACTORY);
	}
	
	@Override
	public IShipping onCreate(IShipping context) {
		super.onCreate(context);
		return this;
	}
}
