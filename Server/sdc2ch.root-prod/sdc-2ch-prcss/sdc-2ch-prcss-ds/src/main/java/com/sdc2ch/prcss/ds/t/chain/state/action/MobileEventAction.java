package com.sdc2ch.prcss.ds.t.chain.state.action;


import java.util.Collections;
import java.util.List;

import com.sdc2ch.core.expression.InstanceOf;
import com.sdc2ch.prcss.ds.core.IShipping;
import com.sdc2ch.prcss.ds.event.IMobileActionEvent;
import com.sdc2ch.prcss.ds.event.IMobileActionEvent.MobileEventActionType;
import com.sdc2ch.prcss.ds.event.IProcessEvent;
import com.sdc2ch.prcss.ds.t.chain.state.ShippingStatus;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MobileEventAction extends ActionEvent {
	
	private MobileEventActionType type;
	
	public MobileEventAction(ShippingStatus parent, MobileEventActionType type) {
		super(parent);
		this.type = type;
	}
	
	protected Class<? extends IMobileActionEvent> _evtClass;
	
	@SuppressWarnings("unchecked")
	@Override
	public void setEventClass(Class<? extends IProcessEvent> class1) {
		this._evtClass = (Class<? extends IMobileActionEvent>) class1;
	}
	
	@Override
	public IShipping onCreate(IShipping context) {
		disposable = super.subscribeMBL(this, e -> {
			return predicate(e);
			
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
	
	
	public boolean predicate(IProcessEvent e) {
		boolean isCOmplete = InstanceOf.when(e).instanceOf(_evtClass)
				.then( evt1 -> evt1.getMobileEventActionType() == type).otherwise(false);
		return isCOmplete && e != getCurrentEvent();
	}
	
	
	
	protected void onReactive(IProcessEvent e1) {
		log.info("onReactive() :> {}", this);
		super.onReceive(e1);
	}
	
	@Override
	public boolean supported(IProcessEvent event) {
		return predicate(event);
	}
	@Override
	public void hit() {
		onComplete();
		super.setCurrent(this);
		parentState.hit();
		
		if(type == MobileEventActionType.FINISH_JOB) {
			super.onFinish();
		}
	}
	
	public MobileEventActionType getMobileEventType() {
		return type;
	}
}
