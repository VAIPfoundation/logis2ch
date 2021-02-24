package com.sdc2ch.core.observer.impl;

import com.sdc2ch.require.event.I2ChEvent;

import io.reactivex.ObservableSource;
import io.reactivex.internal.fuseable.HasUpstreamObservableSource;

public abstract class I2ChObservableWithUpstream <T extends I2ChEvent<T>, U> extends I2ChObserverble<T> implements HasUpstreamObservableSource<T> {

    
    protected final ObservableSource<T> source;

    
    I2ChObservableWithUpstream(ObservableSource<T> source) {
        this.source = source;
    }

    @Override
    public final ObservableSource<T> source() {
        return source;
    }
}
