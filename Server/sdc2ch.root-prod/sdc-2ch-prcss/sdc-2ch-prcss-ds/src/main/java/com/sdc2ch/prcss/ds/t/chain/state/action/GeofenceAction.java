package com.sdc2ch.prcss.ds.t.chain.state.action;


import java.util.Collections;
import java.util.List;

import com.sdc2ch.prcss.ds.core.IShipping;
import com.sdc2ch.prcss.ds.event.IGeoFenceEvent;
import com.sdc2ch.prcss.ds.event.IGeoFenceEvent.GeoFenceEvent;
import com.sdc2ch.prcss.ds.event.IProcessEvent;
import com.sdc2ch.prcss.ds.t.chain.ShippingChain;
import com.sdc2ch.prcss.ds.t.chain.state.ShippingStatus;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class GeofenceAction extends ActionEvent {
	
	protected GeoFenceEvent type;
	
	protected Class<? extends IGeoFenceEvent> _evtClass;
	
	@SuppressWarnings("unchecked")
	@Override
	public void setEventClass(Class<? extends IProcessEvent> class1) {
		this._evtClass = (Class<? extends IGeoFenceEvent>) class1;
	}
	
	public GeofenceAction(ShippingStatus parent, GeoFenceEvent type) {
		super(parent);
		this.type = type;
	}
	
	@Override
	public IShipping onCreate(IShipping context) {
		disposable = super.subscribeGPS(this, e -> {
			boolean iscomplete = predicate(e);
			boolean cur = true;
			if(iscomplete && super.getCurrentEvent() != null && super.getCurrentEvent() instanceof IGeoFenceEvent) {
				cur = ((IGeoFenceEvent) super.getCurrentEvent()).getIncrement() != ((IGeoFenceEvent) e).getIncrement();

			}
			
			
			return iscomplete;
		}).subscribe(e1 -> onReactive(e1));
		return this;
	}

	@Override
	public void onCancel() {
		super.onComplete();
	}

	@Override
	public List<IShipping> getChilds() {
		return Collections.emptyList();
	}
	
	public boolean predicate(IProcessEvent event) {










		return ((IGeoFenceEvent) event).getGeoEvent() == type && super.isEqualLoc(event);
	}

	protected void onReactive(IProcessEvent e1) {
		super.onReceive(e1);
	}
	protected void info(IProcessEvent e1) {
		log.info("onReactive() -> {} {}, {}", this.getParent().getParent(), this.getParent(), e1.getClass().getSimpleName());
	}
	
	@Override
	public boolean supported(IProcessEvent event) {
		return event instanceof IGeoFenceEvent && predicate(event);
	}
	@Override
	public void hit() {
		onComplete();
		super.setCurrent(this);
	}
	
}
