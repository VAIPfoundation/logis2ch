package com.sdc2ch.prcss.ds.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import com.sdc2ch.aiv.event.IGpsEvent;
import com.sdc2ch.core.geocalc.DegreeCoordinate;
import com.sdc2ch.core.geocalc.EarthCalc;
import com.sdc2ch.core.geocalc.Point;
import com.sdc2ch.prcss.ds.IShippingContext;
import com.sdc2ch.prcss.ds.IShippingHandler;
import com.sdc2ch.prcss.ds.event.IGeoFenceEvent;
import com.sdc2ch.prcss.ds.event.IProcessEvent;
import com.sdc2ch.prcss.ds.event.IShippingContextEvent.EventAction;
import com.sdc2ch.prcss.ds.event.IShippingContextEvent.EventBy;
import com.sdc2ch.prcss.ds.event.ShippingStateEvent;
import com.sdc2ch.prcss.ds.io.ShippingState2;
import com.sdc2ch.require.event.I2ChEvent;
import com.sdc2ch.tms.enums.FactoryType;
import com.sdc2ch.tms.io.TmsPlanIO;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractShippingHandler implements IShippingHandler {
	
	private Disposable mainDisposable;
	private IShippingContext context;
	private Class<?> receive;
	
	private List<BehaviorSubject<ShippingStateEvent>> observers = new ArrayList<>();
	
	protected AbstractShippingHandler(IShippingContext context, Class<?> class1) {
		this.context = context;
		this.receive = class1;
	}
	
	protected abstract void onReceive(I2ChEvent<?> e);
	
	@Override
	public void onReady() {
		this.mainDisposable = context.subscribe(receive)
				.filter(e -> onReceive(e), context.getUser());
	}
	
	@Override
	public void onClose() {
		Optional.of(mainDisposable).ifPresent(d -> d.dispose());
	}

	@Override
	public void add(TmsPlanIO tmsplan) {
		
	}
	
	public void addObserver(BehaviorSubject<ShippingStateEvent> observer) {
		this.observers.add(observer);
	}
	









	public void fireEvent(IProcessEvent event) {
		
		ShippingStateEvent sEvent = new ShippingStateEvent();
		
		if(event instanceof IGeoFenceEvent ) {
			
			IGeoFenceEvent geo = (IGeoFenceEvent) event;
			
			FactoryType ft = FactoryType.convert(geo.getStopCd());
			sEvent.setEventDt(new Date(event.getTimeStamp()));
			sEvent.setState(ft == FactoryType.FFFF ? ShippingState2.ENTER2 : ShippingState2.ENTER);
			sEvent.setEventBy(EventBy.GPS);
			sEvent.setStopCd(geo.getStopCd());
			sEvent.setRouteNo(geo.getRouteNo());
			
			switch (geo.getGeoEvent()) {
			case GEO_ARRIVE:
				sEvent.setEventAct(EventAction.GEO_ARRIVED);
				sEvent.setState(ft == FactoryType.FFFF ? ShippingState2.ARRIVE : ShippingState2.ENTER);
				break;
			case GEO_ENTER:
				sEvent.setEventAct(EventAction.GEO_ENTER);
				sEvent.setRouteNo(null);
				break;
			case GEO_EXIT:
				sEvent.setState(ft == FactoryType.FFFF ? ShippingState2.DEPART : ShippingState2.EXIT);
				sEvent.setEventAct(EventAction.GEO_EXITED);
				break;
			default:
				break;
			}
		}
		context.fireEvent(sEvent);
	}

	public IShippingContext getContext() {
		return context;
	}
	
	public <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Set<Object> seen = ConcurrentHashMap.newKeySet();
	    return t -> seen.add(keyExtractor.apply(t));
	}
	
	public static boolean isNumeric(String s) {  
	    return s != null && s.matches("[-+]?\\d*\\.?\\d+");  
	}
	
	public double distance(double lat1, double lat2, double lon1,
	        double lon2, double el1, double el2) {
		
		DegreeCoordinate currentlat = new DegreeCoordinate(lat1);
		DegreeCoordinate currentlng = new DegreeCoordinate(lon1);
		Point current = new Point(currentlat, currentlng);
		
		DegreeCoordinate lastlat = new DegreeCoordinate(lat2);
		DegreeCoordinate lastlng = new DegreeCoordinate(lon2);
		Point last = new Point(lastlat, lastlng);
		
		return EarthCalc.getDistance(current, last);

	}

	public abstract void handle(IGpsEvent event);
}
