package com.sdc2ch.require.pubsub;

import com.sdc2ch.require.event.I2ChEvent;

import io.reactivex.Observer;
import io.reactivex.functions.Predicate;

 
public interface I2ChEventObserver<T extends I2ChEvent<T>> extends Observer<T> {
	

	
	
	I2ChEventObserver<T> filtering(Predicate<? super T> predicate);
}
