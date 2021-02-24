package com.sdc2ch.prcss.ds.t.chain.state.action;


import java.util.Collections;
import java.util.List;

import com.sdc2ch.prcss.ds.core.IShipping;
import com.sdc2ch.prcss.ds.event.IAutoDoorEvent;
import com.sdc2ch.prcss.ds.event.IProcessEvent;
import com.sdc2ch.prcss.ds.t.chain.state.ShippingStatus;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class AutoDoorAction extends ActionEvent {
	
	public AutoDoorAction(ShippingStatus parent) {
		super(parent);
	}
	
	@Override
	public IShipping onCreate(IShipping context) {
		disposable = super.subscribeGPS(this, e -> supported(e))
				.subscribe(e1 -> onReactive(e1));
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

	protected void onReactive(IProcessEvent e1) {
		log.info("onReactive() {}, {}", this.getParent(), this);
		super.onReceive(e1);
	}

	@Override
	public boolean supported(IProcessEvent event) {
		return event instanceof IAutoDoorEvent;
	}

	@Override
	public void hit() {
		onComplete();
		super.setCurrent(this);
	}
	
}
