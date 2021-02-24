package com.sdc2ch.prcss.ds.t.receiver;

import com.sdc2ch.prcss.ds.core.ShippingPlanContext;
import com.sdc2ch.prcss.ds.impl.EmptyBoxEventImpl;
import com.sdc2ch.prcss.eb.event.IEmptyboxEvent;
import com.sdc2ch.require.event.I2ChEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShippingEmptyboxHandler extends AbstractShippingReceiver {
	
	
	public ShippingEmptyboxHandler(ShippingPlanContext context) {
		super(context, IEmptyboxEvent.class);
	}

	@Override
	protected void onReceive(I2ChEvent<?> e) {
		
		if(e instanceof IEmptyboxEvent) {
			
			try {
				
				IEmptyboxEvent event = (IEmptyboxEvent) e;
				EmptyBoxEventImpl emptybox = EmptyBoxEventImpl.of(e.user());
				emptybox.setTimeStamp(event.getTimeStamp());
				emptybox.setUniqueSequence(findSequence(event.getDlvyLcId()));
				
				super.fireEvent(emptybox);
			}catch (Exception ex) {
				log.error("{}", ex);
			}

		}
		
	}

}
