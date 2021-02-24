package com.sdc2ch.require.pubsub;

import com.sdc2ch.require.event.I2ChEvent;


public interface I2ChEventPublisher<T extends I2ChEvent<T>> {
	void fireEvent(T event);








}
