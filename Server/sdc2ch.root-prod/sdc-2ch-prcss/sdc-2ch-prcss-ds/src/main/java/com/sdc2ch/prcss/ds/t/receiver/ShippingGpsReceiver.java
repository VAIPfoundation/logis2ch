package com.sdc2ch.prcss.ds.t.receiver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.sdc2ch.aiv.event.IGpsEvent;
import com.sdc2ch.prcss.ds.core.IShipping;
import com.sdc2ch.prcss.ds.core.ShippingPlanContext;
import com.sdc2ch.prcss.ds.event.IGeoFenceEvent;
import com.sdc2ch.prcss.ds.event.IGeoFenceEvent.GeoFenceEvent;
import com.sdc2ch.prcss.ds.impl.GeoFenceEventImpl;
import com.sdc2ch.prcss.ds.io.ShippingPlanIO;
import com.sdc2ch.prcss.ds.util.ProcessUtils;
import com.sdc2ch.require.event.I2ChEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShippingGpsReceiver extends AbstractShippingReceiver {
	
	private List<ShippingPlanIO> shippings = new ArrayList<>();
	private List<IGeoFenceEvent> alliveds = new ArrayList<>();
	private List<IGeoFenceEvent> enters = new ArrayList<>();
	private AtomicLong inc = new AtomicLong();
	
	public ShippingGpsReceiver(ShippingPlanContext context) {
		super(context, IGpsEvent.class);
	}

	public void add(IShipping shipping) {
		shippings.add(shipping.getShippingPlanIO());
		shippings = shippings.stream().filter(ProcessUtils.distinctByKey(s -> s.getDlvyLcId())).collect(Collectors.toList());
	}

	@Override
	protected void onReceive(I2ChEvent<?> e) {
		
		if(e instanceof IGpsEvent) {
			

			
			try {
				IGpsEvent event = (IGpsEvent) e;
				

				if(event.getLat() == 0 || event.getLng() == 0)
					return;
				
				long _inc = inc.incrementAndGet();
				shippings.stream().map(t -> {
					double distance = ProcessUtils.distance(t.getLat(), event.getLat(), t.getLng(), event.getLng(), 0, 0);
					





					
					return GeoFenceEventImpl.of(event.user(), distance, t.getUniqueSequence(), event.getTimeStamp(), t.getRadius());
				}).map(geo -> {
					GeoFenceEvent ty = geo.getDistance() <= geo.getRadius() ? GeoFenceEvent.GEO_ARRIVE
							: geo.getDistance() <= 1000 ? GeoFenceEvent.GEO_ENTER : GeoFenceEvent.GEO_EXIT;
					geo.setGeoEvent(ty);
					geo.setIncrement(_inc);
					return geo.build();
				}).forEach(geo -> {

					switch(geo.getGeoEvent()) {
					case GEO_ARRIVE: 
						enters.removeIf( s -> s.getUniqueSequence() == geo.getUniqueSequence());
						if(isContains(alliveds,geo)) {
							alliveds.add(geo);
							super.fireEvent(geo);
						}
						break;
					case GEO_ENTER:
						if(isContains(enters,geo)) {
							enters.add(geo);
							super.fireEvent(geo);
						}
						break;
					case GEO_EXIT:
						alliveds.removeIf( s -> {
							boolean isDepart = s.getUniqueSequence() == geo.getUniqueSequence() && geo.getDistance() >= 1000;
							if(isDepart)
								super.fireEvent(geo);
							return isDepart;
						});
						enters.removeIf( s -> s.getUniqueSequence() == geo.getUniqueSequence());
						break;
					default:
						break;
					
					}
				});
			}catch (Exception ex) {
				log.error("{}", ex);
			}
			
		}
	}
	
	private boolean isContains(List<IGeoFenceEvent> list, IGeoFenceEvent geo) {
		return list.isEmpty() || !list.stream().anyMatch(s -> (s.getUniqueSequence() == geo.getUniqueSequence()));
	}
}
