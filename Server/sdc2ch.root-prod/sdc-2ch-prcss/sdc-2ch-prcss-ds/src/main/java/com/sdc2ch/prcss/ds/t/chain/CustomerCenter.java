package com.sdc2ch.prcss.ds.t.chain;

import com.sdc2ch.prcss.ds.core.AbstractShippingState;
import com.sdc2ch.prcss.ds.core.ShippingPlan;
import com.sdc2ch.prcss.ds.core.ShippingPlanContext;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class CustomerCenter extends ShippingChain {

	public CustomerCenter(ShippingPlanContext chainContext, ShippingPlan config) {
		super(chainContext, config);
		setChainName(ChainNm.CUSTOMER_CENTER);
	}
	
	@Override
	protected void onChange(AbstractShippingState current) {
		
		
		
		String lo = current.getShippingPlanIO().getBundledDlvyLc();
		
		if(lo != null) {
			super.getChains().stream().filter(c -> current.getParent() != c && lo.equals(c.getShippingPlanIO().getBundledDlvyLc())).forEach(bc -> {
				log.info("묶음착지 발견 처리방안은 ???? {}", bc);
				bc.flattened().forEach(c1 -> c1.onCancel());
			});
		}
		
		









		super.onChange(current);
		
	}
		

}
