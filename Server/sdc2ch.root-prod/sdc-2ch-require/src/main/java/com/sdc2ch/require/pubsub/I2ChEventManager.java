package com.sdc2ch.require.pubsub;

import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.event.I2ChEvent;

 
public interface I2ChEventManager {
	
	
	<T extends I2ChEvent<T>> I2ChEventPublisher<T> regist(Class<T> cls);
	
	
	<T extends I2ChEvent<T>> I2ChEventConsumer<T> subscribe(Class<?> class1);
}
