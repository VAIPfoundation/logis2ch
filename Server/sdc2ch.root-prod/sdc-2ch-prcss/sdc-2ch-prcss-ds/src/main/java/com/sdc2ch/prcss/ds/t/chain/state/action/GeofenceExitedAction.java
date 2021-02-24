package com.sdc2ch.prcss.ds.t.chain.state.action;


import com.sdc2ch.prcss.ds.event.IGeoFenceEvent.GeoFenceEvent;
import com.sdc2ch.prcss.ds.event.IGeoFenceExitedEvent;
import com.sdc2ch.prcss.ds.event.IProcessEvent;
import com.sdc2ch.prcss.ds.t.chain.state.ShippingStatus;


public class GeofenceExitedAction extends GeofenceAction {
	
	public GeofenceExitedAction(ShippingStatus parent, GeoFenceEvent type) {
		super(parent, type);
	}
	
	@Override
	protected void onReactive(IProcessEvent e1) {
		ShippingStatus state = (ShippingStatus) super.findArriveState();
		

		if(state != null && state.isComplete()) {
			super.info(e1);

			
			super.onReceive(e1);
		}
	}
	@Override
	public boolean supported(IProcessEvent event) {
		



		return event instanceof IGeoFenceExitedEvent && 
				((IGeoFenceExitedEvent) event).getGeoEvent() == type  && 
						super.isEqualLoc(event);
	}
	@Override
	public void hit() {
		onComplete();
		super.setCurrent(this);
	}
}
