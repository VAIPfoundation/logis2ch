package com.sdc2ch.core.observer.impl;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import com.sdc2ch.require.event.I2ChEvent;
import com.sdc2ch.require.pubsub.I2ChEventObserver;

import io.reactivex.Observer;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.annotations.Nullable;
import io.reactivex.annotations.SchedulerSupport;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.subjects.Subject;

 
public abstract class I2ChObserverble<T extends I2ChEvent<T>> extends Subject<T> implements I2ChEventObserver<T> {

    
    @SuppressWarnings("rawtypes")
    static final PublishDisposable[] TERMINATED = new PublishDisposable[0];
    
    @SuppressWarnings("rawtypes")
    static final PublishDisposable[] EMPTY = new PublishDisposable[0];

    
    final AtomicReference<PublishDisposable<T>[]> subscribers;

    
    Throwable error;

    
    @SuppressWarnings("unchecked")
    protected I2ChObserverble() {
        subscribers = new AtomicReference<PublishDisposable<T>[]>(EMPTY);
    }

    
    boolean add(PublishDisposable<T> ps) {
        for (;;) {
            PublishDisposable<T>[] a = subscribers.get();
            if (a == TERMINATED) {
                return false;
            }

            int n = a.length;
            @SuppressWarnings("unchecked")
            PublishDisposable<T>[] b = new PublishDisposable[n + 1];
            System.arraycopy(a, 0, b, 0, n);
            b[n] = ps;

            if (subscribers.compareAndSet(a, b)) {
                return true;
            }
        }
    }

    
    @SuppressWarnings("unchecked")
    void remove(PublishDisposable<?> ps) {
        for (;;) {
            PublishDisposable<T>[] a = subscribers.get();
            if (a == TERMINATED || a == EMPTY) {
                return;
            }

            int n = a.length;
            int j = -1;
            for (int i = 0; i < n; i++) {
                if (a[i] == ps) {
                    j = i;
                    break;
                }
            }

            if (j < 0) {
                return;
            }

            PublishDisposable<T>[] b;

            if (n == 1) {
                b = EMPTY;
            } else {
                b = new PublishDisposable[n - 1];
                System.arraycopy(a, 0, b, 0, j);
                System.arraycopy(a, j + 1, b, j, n - j - 1);
            }
            if (subscribers.compareAndSet(a, b)) {
                return;
            }
        }
    }

    @Override
    public void onSubscribe(Disposable s) {
        if (subscribers.get() == TERMINATED) {
            s.dispose();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onError(Throwable t) {
        ObjectHelper.requireNonNull(t, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
        if (subscribers.get() == TERMINATED) {
            RxJavaPlugins.onError(t);
            return;
        }
        error = t;

        for (PublishDisposable<I2ChEvent<T>> s : subscribers.getAndSet(TERMINATED)) {
            s.onError(t);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onComplete() {
        if (subscribers.get() == TERMINATED) {
            return;
        }
        for (PublishDisposable<I2ChEvent<T>> s : subscribers.getAndSet(TERMINATED)) {
            s.onComplete();
        }
    }

    @Override
    public boolean hasObservers() {
        return subscribers.get().length != 0;
    }

    @Override
    @Nullable
    public Throwable getThrowable() {
        if (subscribers.get() == TERMINATED) {
            return error;
        }
        return null;
    }

    @Override
    public boolean hasThrowable() {
        return subscribers.get() == TERMINATED && error != null;
    }

    @Override
    public boolean hasComplete() {
        return subscribers.get() == TERMINATED && error == null;
    }

	@SuppressWarnings("unchecked")
	@Override
	protected void subscribeActual(Observer<? super T> t) {
		
		PublishDisposable<T> ps = new PublishDisposable<T>((Observer<? super I2ChEvent<T>>) t, this);
        t.onSubscribe(ps);
        if (add(ps)) {
            
            
            if (ps.isDisposed()) {
                remove(ps);
            }
        } else {
            Throwable ex = error;
            if (ex != null) {
                t.onError(ex);
            } else {
                t.onComplete();
            }
        }
		
	}

    @Override
    public void onNext(T t) {
        ObjectHelper.requireNonNull(t, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
        for (PublishDisposable<T> s : subscribers.get()) {
            s.onNext(t);
        }
    }
    
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    @Override
    public final I2ChObserverble<T> filtering(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return (I2ChObserverble<T>) RxJavaPlugins.onAssembly(new I2ChObservableFilter<T>(this, predicate));
    }

    
    static final class PublishDisposable<T> extends AtomicBoolean implements Disposable {

        private static final long serialVersionUID = 3562861878281475070L;
        
        final Observer<? super I2ChEvent<T>> actual;
        
        final I2ChObserverble<? extends I2ChEvent<T>> parent;

        
        PublishDisposable(Observer<? super I2ChEvent<T>> t, I2ChObserverble<? extends I2ChEvent<T>> parent) {
            this.actual = t;
            this.parent = parent;
        }


		public void onNext(I2ChEvent<T> t) {
            if (!get()) {
                actual.onNext(t);
            }
        }

        public void onError(Throwable t) {
            if (get()) {
                RxJavaPlugins.onError(t);
            } else {
                actual.onError(t);
            }
        }

        public void onComplete() {
            if (!get()) {
                actual.onComplete();
            }
        }

        @Override
        public void dispose() {
            if (compareAndSet(false, true)) {
                parent.remove(this);
            }
        }

        @Override
        public boolean isDisposed() {
            return get();
        }
    }

}
