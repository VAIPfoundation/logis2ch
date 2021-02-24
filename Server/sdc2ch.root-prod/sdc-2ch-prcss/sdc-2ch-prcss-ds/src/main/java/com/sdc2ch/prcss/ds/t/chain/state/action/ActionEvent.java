package com.sdc2ch.prcss.ds.t.chain.state.action;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.sdc2ch.prcss.ds.core.AbstractShippingState;
import com.sdc2ch.prcss.ds.core.IShipping;
import com.sdc2ch.prcss.ds.event.IProcessEvent;
import com.sdc2ch.prcss.ds.t.chain.state.ShippingStatus;

import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public abstract class ActionEvent extends AbstractShippingState {
	
	public ActionEvent(AbstractShippingState parent) {
		super(parent);
		this.parentState = (ShippingStatus) parent;
	}
	
	protected ShippingStatus parentState;
	
	protected Disposable disposable;
	protected Class<? extends IProcessEvent> evtClass;
	
	public abstract void hit();
	
	private IProcessEvent last;
	
	@Override
	public IShipping onCreate(IShipping context) {
		return this;
	}
	
	public void setEventClass(Class<? extends IProcessEvent> class1) {
		this.evtClass = class1;
	}

	@Override
	public List<IShipping> getChilds() {
		return Collections.emptyList();
	}
	protected abstract void onReactive(IProcessEvent e1);
	public abstract boolean supported(IProcessEvent event);
	
	@Override
	public void onCancel() {
		log.debug("onCancel() call {}", this);
		onComplete();
	}
	public void onComplete() {

		
		if(!super.isFactory()){
			Optional.of(disposable).ifPresent(d -> d.dispose());
		}
		completed = true;
	}
	
	@Override
	public void onReceive(IProcessEvent e1) {

		this.last = e1;
		setEvent(e1);
		parentState.onReceive(e1);
	}
	
	@Override
	public void setCurrent(AbstractShippingState current) {
		getParent().setCurrent(this);
	}
	
	public boolean supported(IProcessEvent event, IShipping shipping) {
		return false;
	}
	

	@Override
	public IProcessEvent getCurrentEvent() {
		return super.getCurrentEvent();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(getClass().getSimpleName()).append("]");
		return sb.toString();
	}

	public void removeEvent() {
		Optional.of(disposable).ifPresent(d -> d.dispose());
	}
	
	public IProcessEvent getEvent() {
		return last;
	}
}
