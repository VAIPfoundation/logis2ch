package com.sdc2ch.core.pubsub.impl;

import com.sdc2ch.core.observer.impl.I2ChObserverble;
import com.sdc2ch.require.domain.IUser;
import com.sdc2ch.require.event.I2ChEvent;
import com.sdc2ch.require.pubsub.I2ChEventConsumer;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class I2ChEventConsumerImpl<T extends I2ChEvent<T>> implements I2ChEventConsumer<T> {

	private Action act = new Action() {

		@Override
		public void run() throws Exception {
			log.info("onComplete");
		}
	};
	private I2ChObserverble<T> pub;

	public I2ChEventConsumerImpl(I2ChObserverble<T> pub) {
		this.pub = pub;
		pub.subscribe(e -> {

		}, e -> {
			log.info("onError {}", e);
		}, act, e -> {


		});
	}

	@Override
	public void accept(T t) throws Exception {
		log.warn("I2ChEventConsumerImpl :> root consumer called !! {}", t);
	}

	@Override
	public Disposable filter(Consumer<? super I2ChEvent<T>> consumer, IUser user) {
		return filter(consumer, e -> e.user() != null && e.user().getUsername().hashCode() == user.getUsername().hashCode());
	}

	@Override
	public Disposable filter(Consumer<? super I2ChEvent<T>> consumer, Predicate<T> predicate) {
		return pub.filter(predicate).subscribe(consumer);

	}

	@Override
	public Disposable filter(Consumer<? super I2ChEvent<T>> consumer) {
		return pub.subscribe(consumer);
	}

}
