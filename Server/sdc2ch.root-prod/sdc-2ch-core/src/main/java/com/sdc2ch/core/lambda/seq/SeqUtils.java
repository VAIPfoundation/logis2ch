package com.sdc2ch.core.lambda.seq;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class SeqUtils {

    static <T, U> Seq<U> transform(Stream<? extends T> stream, DelegatingSpliterator<T, U> delegating) {
        Spliterator<? extends T> delegate = stream.spliterator();

        return Seq.seq(new Spliterator<U>() {
            @Override
            public boolean tryAdvance(Consumer<? super U> action) {
                return delegating.tryAdvance(delegate, action);
            }

            @Override
            public Spliterator<U> trySplit() {
                return null;
            }

            @Override
            public long estimateSize() {
                return Long.MAX_VALUE;
            }

            @Override
            public int characteristics() {
                return delegate.characteristics() & Spliterator.ORDERED;
            }

            @Override
            @SuppressWarnings("unchecked")
            public Comparator<? super U> getComparator() {

                
                
                
                
                
                return (Comparator<U>) delegate.getComparator();
            }
        }).onClose(() -> stream.close());
    }
    
    @FunctionalInterface
    interface DelegatingSpliterator<T, U> {
        boolean tryAdvance(Spliterator<? extends T> delegate, Consumer<? super U> action);
    }
}
