package com.sdc2ch.prcss.ds.t.chain.state;



import java.util.List;
import java.util.stream.Collectors;

import com.sdc2ch.prcss.ds.core.AbstractShippingState;
import com.sdc2ch.prcss.ds.core.IShipping;
import com.sdc2ch.prcss.ds.event.IProcessEvent;
import com.sdc2ch.prcss.ds.io.ShippingState;
import com.sdc2ch.prcss.ds.t.chain.ShippingChain;

import lombok.extern.slf4j.Slf4j;

 
@Slf4j
public class TurnningState extends ShippingStatus {


	public TurnningState(ShippingChain chain) {
		super(chain, StateNm.TURN);
	}
	
	@Override
	public IShipping onCreate(IShipping context) {



		return super.onCreate(context);
	}








	




	
	@Override
	public boolean supported(IProcessEvent event) {
		
		
		List<AbstractShippingState> list = this.flattened().filter(s -> s != this).collect(Collectors.toList());
		
		boolean supported = list.stream().anyMatch(s -> {
			boolean sss = s.supported(event);



			return sss;
		});
		return supported;
	}
	
	@Override
	public com.sdc2ch.prcss.ds.io.ShippingState getShippingState() {
		return ShippingState.RETURN;
	}

}
