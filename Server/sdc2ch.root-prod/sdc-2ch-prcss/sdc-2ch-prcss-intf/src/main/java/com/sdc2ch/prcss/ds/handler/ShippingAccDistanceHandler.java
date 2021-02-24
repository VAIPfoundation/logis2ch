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
public class ShippingAccDistanceHandler extends AbstractShippingHandler {
	
	private IGpsEvent old;
	private double accDistance;
	
	public ShippingAccDistanceHandler(IShippingContext context) {
		super(context, IGpsEvent.class);
	}


	@Override
	protected void onReceive(I2ChEvent<?> e) {
		
		if(e instanceof IGpsEvent) {
			IGpsEvent event = (IGpsEvent) e;
			handle(event);
		}
	}


	@Override
	public void handle(IGpsEvent event) {
		if(event == null)
			return;
		try {

			if(event.getLat() == 0 || event.getLng() == 0)
				return;
			
			if(old == null) {
				old = event;
			}
			
			double lat = StringUtils.isEmpty(old.getLat()) ? 0 : new BigDecimal(old.getLat()).doubleValue();
			double lng = StringUtils.isEmpty(old.getLng()) ? 0 : new BigDecimal(old.getLng()).doubleValue();
			double distance = super.distance(lat, event.getLat(), lng, event.getLng(), 0, 0);
			
			outer : if(distance < 0 || distance > 1800) {
				long diff = event.getTimeStamp() - old.getTimeStamp();

				long timeDiff = diff/1000;
				if(timeDiff * 50 > distance && distance > 0) {
					break outer;
				}
				return;
			}
			accDistance += distance; 
			super.getContext().fireEvent(ShippingAnalsEvent.builder().analsTy(AnalsTy.CONTI_DRV).accDistance(accDistance).build());
		}catch (Exception ex) {
			log.error("{}", ex);
		}finally {
			if(event != null) {
				if(event.getLat() != 0 && event.getLng() != 0)
					old = event;
			}
		}
		
	}
}
