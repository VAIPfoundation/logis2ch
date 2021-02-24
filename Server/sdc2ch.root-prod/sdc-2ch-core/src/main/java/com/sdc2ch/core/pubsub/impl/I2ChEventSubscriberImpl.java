package com.sdc2ch.core.pubsub.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sdc2ch.core.observer.impl.I2ChObserverble;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.event.I2ChEvent;
import com.sdc2ch.require.pubsub.I2ChEventConsumer;
import com.sdc2ch.require.pubsub.I2ChEventManager;
import com.sdc2ch.require.pubsub.I2ChEventSubscriber;

 
@Component
public abstract class I2ChEventSubscriberImpl<T extends I2ChEvent<T>> extends I2ChObserverble<T> implements I2ChEventSubscriber<T> {
	@Autowired
	public void setup(I2ChEventManager manager) {
		init(manager);
	}
	protected abstract void init(I2ChEventManager manager);
	public abstract void filter(IUser user, I2ChEventConsumer<T> consumer);
}
