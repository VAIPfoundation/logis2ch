package com.sdc2ch.prcss.ds.t.chain.state.action;


import java.util.Collections;
import java.util.List;

import com.sdc2ch.core.expression.InstanceOf;
import com.sdc2ch.prcss.ds.core.IShipping;
import com.sdc2ch.prcss.ds.event.IBeconEnterEvent;
import com.sdc2ch.prcss.ds.event.IBeconEvent;
import com.sdc2ch.prcss.ds.event.IBeconEvent.BeconEventType;
import com.sdc2ch.prcss.ds.event.IProcessEvent;
import com.sdc2ch.prcss.ds.t.chain.state.ShippingStatus;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class BeaconAction extends ActionEvent {
	
	
	private BeconEventType type;
	
	protected Class<? extends IBeconEvent> _evtClass;
	
	@SuppressWarnings("unchecked")
	@Override
	public void setEventClass(Class<? extends IProcessEvent> class1) {
		this._evtClass = (Class<? extends IBeconEvent>) class1;
	}
	
	public BeaconAction(ShippingStatus parent, BeconEventType type) {
		super(parent);
		this.type = type;
	}
	@Override
	public IShipping onCreate(IShipping context) {
		disposable = super.subscribeBCN(this, e -> supported(e))
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
	
	public boolean predicate(IProcessEvent e) {
		return InstanceOf.when(e).instanceOf(_evtClass).then( evt1 ->{


			return evt1.getBconEvent() == type && e != super.getCurrentEvent() ;
		} ).otherwise(false);
	}

	protected void onReactive(IProcessEvent e1) {

		super.onReceive(e1);
	}
	
	@Override
	public boolean supported(IProcessEvent event) {
		return event instanceof IBeconEnterEvent && predicate(event);
	}

	@Override
	public void hit() {
		onComplete();
		super.setCurrent(this);
	}
	
}
