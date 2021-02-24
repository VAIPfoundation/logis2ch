package com.sdc2ch.require.pubsub;

import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.event.I2ChEvent;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

 
public interface I2ChEventConsumer<T extends I2ChEvent<T>> extends Consumer<T> {
	
	Disposable filter(Consumer<? super I2ChEvent<T>> consumer);
	
	
	Disposable filter(Consumer<? super I2ChEvent<T>> consumer, IUser user);
	
	Disposable filter(Consumer<? super I2ChEvent<T>> consumer, Predicate<T> predicate);
}
