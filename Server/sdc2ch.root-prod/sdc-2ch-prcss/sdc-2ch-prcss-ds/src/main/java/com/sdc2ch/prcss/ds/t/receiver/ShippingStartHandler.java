package com.sdc2ch.prcss.ds.t.receiver;

import com.sdc2ch.prcss.ds.core.ShippingPlanContext;
import com.sdc2ch.prcss.ds.event.IMobileActionEvent.MobileEventActionType;
import com.sdc2ch.prcss.ds.impl.MobileActionEventImpl;
import com.sdc2ch.require.event.I2ChEvent;
import com.sdc2ch.service.event.IDriverStartJobEvent;
import com.sdc2ch.service.event.IMobileEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShippingStartHandler extends AbstractShippingReceiver {
	
	public ShippingStartHandler(ShippingPlanContext context) {
		super(context, IDriverStartJobEvent.class);
	}

	@Override
	protected void onReceive(I2ChEvent<?> e) {
		
		if(e instanceof IMobileEvent) {
			
			try {
				IMobileEvent<?> event = (IMobileEvent<?>) e;
				
				MobileActionEventImpl impl = MobileActionEventImpl.of(e.user(), System.currentTimeMillis(), null);
				switch (event.getMobileEventType()) {
				case ALLOCATE_CONFIRM:
					impl.setMobileEventActionType(MobileEventActionType.ALLOCATE_CONFIRM);
					break;
				case FINISH_JOB:
					impl.setMobileEventActionType(MobileEventActionType.FINISH_JOB);
					break;
				case START_JOB:
					impl.setMobileEventActionType(MobileEventActionType.START_JOB);
					break;






				default:
					break;
				}
				
				super.fireEvent(impl);

			}catch (Exception ex) {
				log.error("{}", ex);
			}
		}
	}

}
