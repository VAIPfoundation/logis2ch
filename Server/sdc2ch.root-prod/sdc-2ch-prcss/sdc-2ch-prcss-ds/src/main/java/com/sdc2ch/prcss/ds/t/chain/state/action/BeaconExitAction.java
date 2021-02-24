package com.sdc2ch.prcss.ds.t.chain.state.action;


import com.sdc2ch.prcss.ds.event.IBeconEvent.BeconEventType;
import com.sdc2ch.prcss.ds.event.IBeconExitedEvent;
import com.sdc2ch.prcss.ds.event.IProcessEvent;
import com.sdc2ch.prcss.ds.t.chain.state.ShippingStatus;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class BeaconExitAction extends BeaconAction {
	
	public BeaconExitAction(ShippingStatus parent, BeconEventType type) {
		super(parent, type);
	}
	
	protected void onReactive(IProcessEvent e1) {
		super.onReceive(e1);
	}
	
	@Override
	public boolean supported(IProcessEvent event) {
		return event instanceof IBeconExitedEvent && predicate(event);
	}
	@Override
	public void hit() {
		onComplete();
		super.setCurrent(this);
	}
	
}
