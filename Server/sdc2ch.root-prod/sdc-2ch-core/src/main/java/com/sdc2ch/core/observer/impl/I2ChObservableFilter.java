package com.sdc2ch.core.observer.impl;

import com.sdc2ch.require.event.I2ChEvent;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.Nullable;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.observers.BasicFuseableObserver;

public class I2ChObservableFilter<T extends I2ChEvent<T>> extends I2ChObservableWithUpstream<T, T> {

	final Predicate<? super T> predicate;

	public I2ChObservableFilter(ObservableSource<T> source, Predicate<? super T> predicate) {
	        super(source);
	        this.predicate = predicate;
	    }

	static final class FilterObserver<T> extends BasicFuseableObserver<T, T> {
		final Predicate<? super T> filter;

		FilterObserver(Observer<? super T> actual, Predicate<? super T> filter) {
			super(actual);
			this.filter = filter;
		}

		@Override
		public void onNext(T t) {
			if (sourceMode == NONE) {
				boolean b;
				try {
					b = filter.test(t);
				} catch (Throwable e) {
					fail(e);
					return;
				}
				if (b) {
					actual.onNext(t);
				}
			} else {
				actual.onNext(null);
			}
		}

		@Override
		public int requestFusion(int mode) {
			return transitiveBoundaryFusion(mode);
		}

		@Nullable
		@Override
		public T poll() throws Exception {
			for (;;) {
				T v = qs.poll();
				if (v == null || filter.test(v)) {
					return v;
				}
			}
		}
	}
}
