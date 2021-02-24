package com.sdc2ch.prcss.ds.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.sdc2ch.aiv.event.IGpsEvent;
import com.sdc2ch.prcss.ds.IShippingContext;
import com.sdc2ch.prcss.ds.event.GeoFenceEventImpl;
import com.sdc2ch.prcss.ds.event.IGeoFenceEvent;
import com.sdc2ch.prcss.ds.event.IGeoFenceEvent.GeoFenceEvent;
import com.sdc2ch.require.event.I2ChEvent;
import com.sdc2ch.tms.enums.FactoryType;
import com.sdc2ch.tms.io.TmsPlanIO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShippingGpsHandler extends AbstractShippingHandler {
	
	private List<TmsPlanIO> shippings = new ArrayList<>();
	private List<IGeoFenceEvent> alliveds = new ArrayList<>();
	private List<IGeoFenceEvent> enters = new ArrayList<>();
	private AtomicLong inc = new AtomicLong();
	
	public ShippingGpsHandler(IShippingContext context) {
		super(context, IGpsEvent.class);
	}

	public void add(TmsPlanIO tmsplan) {
		shippings.add(tmsplan);
		shippings = shippings.stream().filter(super.distinctByKey(s -> s.getStopCd())).collect(Collectors.toList());
	}

	@Override
	protected void onReceive(I2ChEvent<?> e) {
		if(e instanceof IGpsEvent) {
			IGpsEvent event = (IGpsEvent) e;
			handle(event);
		}
	}
	
	private boolean isContains(List<IGeoFenceEvent> list, IGeoFenceEvent geo) {
		return list.isEmpty() || !list.stream().anyMatch(s -> (s.getUniqueSequence() == geo.getUniqueSequence()));
	}

	@Override
	public void handle(IGpsEvent event) {
		if(event != null) {
			

			
			try {

				if(event.getLat() == 0 || event.getLng() == 0)
					return;
				
				long _inc = inc.incrementAndGet();
				shippings.stream().map(t -> {
					
					double lat = StringUtils.isEmpty(t.getLat()) ? 0 : new BigDecimal(t.getLat()).doubleValue();
					double lng = StringUtils.isEmpty(t.getLng()) ? 0 : new BigDecimal(t.getLng()).doubleValue();
					double distance = super.distance(lat, event.getLat(), lng, event.getLng(), 0, 0);
					FactoryType ft = FactoryType.convert(t.getStopCd());





					
					GeoFenceEventImpl ent = GeoFenceEventImpl.of(event.user(), distance, t.getUniqueSequence(), event.getTimeStamp(), FactoryType.FFFF == ft ? 50 : 100);
					ent.setStopCd(t.getStopCd());
					ent.setRouteNo(FactoryType.FFFF == ft ? t.getRouteNo() : null);
					return ent;
				}).map(geo -> {
					GeoFenceEvent ty = geo.getDistance() <= geo.getRadius() ? GeoFenceEvent.GEO_ARRIVE
							: geo.getDistance() <= 1000 ? GeoFenceEvent.GEO_ENTER : GeoFenceEvent.GEO_EXIT;
					geo.setGeoEvent(ty);
					geo.setIncrement(_inc);
					return geo.build2();
				}).forEach(geo -> {

					switch(geo.getGeoEvent()) {
					case GEO_ARRIVE: 

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
}
