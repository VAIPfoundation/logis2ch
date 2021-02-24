package com.sdc2ch.require.pubsub;

import com.sdc2ch.require.event.I2ChEvent;

import io.reactivex.Observable;


public interface I2ChEventSubscriber<T extends I2ChEvent<T>> extends I2ChEventObserver<T> {
	Observable<T> filter(T filter);
}
