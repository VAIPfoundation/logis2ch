package com.sdc2ch.prcss.ds.t.receiver;

import com.sdc2ch.aiv.event.IBeaconFireEvent;
import com.sdc2ch.aiv.event.IBeaconFireEvent.InoutType;
import com.sdc2ch.prcss.ds.core.ShippingPlanContext;
import com.sdc2ch.prcss.ds.event.IBeconEvent.BeconEventType;
import com.sdc2ch.prcss.ds.impl.BecoEventImpl;
import com.sdc2ch.require.enums.SetupLcType;
import com.sdc2ch.require.event.I2ChEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShippingBconHandler extends AbstractShippingReceiver {
	
	private IBeaconFireEvent current;
	
	private long delay = 1000 * 60 * 30;
	
	public ShippingBconHandler(ShippingPlanContext context) {
		super(context, IBeaconFireEvent.class);
	}
	
	@Override
	protected void onReceive(I2ChEvent<?> e) {
		
		if(e instanceof IBeaconFireEvent) {
			
			try {
				IBeaconFireEvent event = (IBeaconFireEvent) e;
				InoutType type = event.getInoutTy();
				BecoEventImpl bcon = BecoEventImpl.of(e.user());
				switch (type) {
				case IN:
					bcon.setBconEvent(BeconEventType.BCON_ENTER);
					break;
				case OUT:
					bcon.setBconEvent(BeconEventType.BCON_EXITD);
					break;
				default:
					break;
				}
				bcon.setTimeStamp(event.getTimestamp());
				bcon.setFactoryType(event.getFactoryTy());
				bcon.setSetupLcTy(event.getSetupLcTy());
				
				if(SetupLcType.FRONT == event.getSetupLcTy()) {
					if(current == null) {
						current = event;
						super.fireEvent(bcon);
					}
					
					if(event.getInoutTy() != current.getInoutTy()) {
						
						
						if(delay < event.getTimestamp() - current.getTimestamp()) {
							current = event;
							super.fireEvent(bcon);
						}
					}else {
						
						if(event.getFactoryTy() != current.getFactoryTy()) {
							current = event;
							super.fireEvent(bcon);
						}
					}
					
				}
			}catch (Exception ex) {
				log.error("{}" , e);
			}

		}
		
	}

}
