package com.sdc2ch.core.pubsub.impl;

import com.sdc2ch.core.observer.impl.I2ChObserverble;
import com.sdc2ch.require.event.I2ChEvent;
import com.sdc2ch.require.pubsub.I2ChEventPublisher;

 
public final class I2ChEventPublisherImpl<T extends I2ChEvent<T>> implements I2ChEventPublisher<T> {

	private I2ChObserverble<T> pub;
	
	public I2ChEventPublisherImpl(I2ChObserverble<T> pub) {
		this.pub = pub;
	}

	@Override
	public void fireEvent(T t) {
		pub.onNext(t);
	}
	
	public I2ChObserverble<T> observerbles(){
		return pub;
	}

}
