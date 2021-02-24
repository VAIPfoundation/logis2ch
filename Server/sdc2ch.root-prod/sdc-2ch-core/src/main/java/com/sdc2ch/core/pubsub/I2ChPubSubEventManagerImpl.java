package com.sdc2ch.core.pubsub;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.sdc2ch.core.observer.impl.I2ChObserverble;
import com.sdc2ch.core.observer.impl.I2ChObserverbleImpl;
import com.sdc2ch.core.pubsub.impl.I2ChEventConsumerImpl;
import com.sdc2ch.core.pubsub.impl.I2ChEventPublisherImpl;
import com.sdc2ch.require.event.I2ChEvent;
import com.sdc2ch.require.pubsub.I2ChEventConsumer;
import com.sdc2ch.require.pubsub.I2ChEventManager;
import com.sdc2ch.require.pubsub.I2ChEventPublisher;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public final class I2ChPubSubEventManagerImpl implements I2ChEventManager {

	private static final I2ChObserverble<? extends I2ChEvent<?>> _pub = new I2ChObserverbleImpl<>();
	private Map<Class<?>, I2ChEventPublisherImpl<?>> mapped = new HashMap<>();







	@SuppressWarnings("unchecked")
	@Override
	public <T extends I2ChEvent<T>> I2ChEventPublisher<T> regist(Class<T> cls) {
		return (I2ChEventPublisher<T>) getPublisher(cls);
	}

	private <T> I2ChEventPublisherImpl<?> getPublisher(Class<T> cls) {
		mapped.putIfAbsent(cls, new I2ChEventPublisherImpl<>(_pub.filtering(e -> {
			log.info(">>>", e);
			return e.getClass() == cls;
		})));
		return mapped.get(cls);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends I2ChEvent<T>> I2ChEventConsumer<T> subscribe(Class<?> cls) {
		I2ChEventConsumer<?> consumer = new I2ChEventConsumerImpl<>(getPublisher(cls).observerbles());
		return (I2ChEventConsumer<T>) consumer;
	}





























































}
