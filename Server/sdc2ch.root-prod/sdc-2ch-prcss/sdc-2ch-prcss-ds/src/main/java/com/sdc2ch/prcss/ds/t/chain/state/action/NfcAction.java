package com.sdc2ch.prcss.ds.t.chain.state.action;


import java.util.Collections;
import java.util.List;

import com.sdc2ch.prcss.ds.core.IShipping;
import com.sdc2ch.prcss.ds.event.ActionEventType;
import com.sdc2ch.prcss.ds.event.INfcTagEvent;
import com.sdc2ch.prcss.ds.event.IProcessEvent;
import com.sdc2ch.prcss.ds.t.chain.state.ShippingStatus;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class NfcAction extends ActionEvent {
	private ActionEventType eventTy;
	public NfcAction(ShippingStatus parent, ActionEventType eventTy) {
		super(parent);
		this.eventTy = eventTy;
	}
	
	protected Class<? extends INfcTagEvent> _evtClass;
	
	@SuppressWarnings("unchecked")
	@Override
	public void setEventClass(Class<? extends IProcessEvent> class1) {
		this._evtClass = (Class<? extends INfcTagEvent>) class1;
	}
	
	@Override
	public IShipping onCreate(IShipping context) {
		disposable = super.subscribeNFC(this, e -> predicate(e))
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
		return e.getActionEventTy() == eventTy;
	}

	protected void onReactive(IProcessEvent e1) {

		super.onReceive(e1);
	}
	
	@Override
	public boolean supported(IProcessEvent event) {
		return event instanceof INfcTagEvent && predicate(event);
	}
	@Override
	public void hit() {
		onComplete();
		super.setCurrent(this);
	}
	
}
