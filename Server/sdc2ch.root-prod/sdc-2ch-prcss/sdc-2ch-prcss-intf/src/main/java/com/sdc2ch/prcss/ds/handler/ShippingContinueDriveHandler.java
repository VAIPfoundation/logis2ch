package com.sdc2ch.prcss.ds.handler;

import java.math.BigDecimal;

import org.springframework.util.StringUtils;

import com.sdc2ch.aiv.event.IGpsEvent;
import com.sdc2ch.prcss.ds.IShippingContext;
import com.sdc2ch.prcss.ds.event.ShippingAnalsEvent;
import com.sdc2ch.prcss.ds.event.ShippingAnalsEvent.AnalsTy;
import com.sdc2ch.require.event.I2ChEvent;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ShippingContinueDriveHandler extends AbstractShippingHandler {
	
	private Record record;
	
	public ShippingContinueDriveHandler(IShippingContext context) {
		super(context, IGpsEvent.class);
	}

	@Override
	protected void onReceive(I2ChEvent<?> e) {
		
		if(e instanceof IGpsEvent) {
			IGpsEvent event = (IGpsEvent) e;
			handle(event);
		}
	}
	
	class Record {
		
		public Record(IGpsEvent event) {
			this.lastEvent = event;
			this.firstTime = event.getTimeStamp();
			this.lastTime = event.getTimeStamp();
		}
		private IGpsEvent lastEvent;
		private long firstTime;
		private long lastTime;
	}

	@Override
	public void handle(IGpsEvent event) {
		if(event != null) {

			try {
				
				if(event.getLat() == 0 || event.getLng() == 0)
					return;
				
				if(record == null) {
					record = new Record(event);
				}
				
				double lat = StringUtils.isEmpty(record.lastEvent.getLat()) ? 0 : new BigDecimal(record.lastEvent.getLat()).doubleValue();
				double lng = StringUtils.isEmpty(record.lastEvent.getLng()) ? 0 : new BigDecimal(record.lastEvent.getLng()).doubleValue();
				double distance = super.distance(lat, event.getLat(), lng, event.getLng(), 0, 0);
				
				
				long diffsec = (event.getTimeStamp() - record.lastEvent.getTimeStamp()) / 1000;
				
				if(diffsec > 0) {
					
					double kmph = (distance / diffsec) * 3.6;
					
					if(kmph > 10) {
						
						long diff = event.getTimeStamp() - record.lastTime;
						
						if(diff > 10 * 60 * 1000) {
							
							super.getContext().fireEvent(ShippingAnalsEvent.builder()
									.analsTy(AnalsTy.TRIP_DRV)
									.tripDrvStTime(record.firstTime)
									.tripDrvEdTime(record.lastTime)
									.build());
							record.firstTime = event.getTimeStamp();
						}
						
						record.lastTime = event.getTimeStamp();
					}
					
				}
				
			}catch (Exception ex) {
				log.error("{}", ex);
			}finally {
				if(record != null && event != null) {
					record.lastEvent = event;
				}
			}
		}
	}
}
