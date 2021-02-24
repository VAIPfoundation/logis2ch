package com.sdc2ch.core.lambda.function;

import java.util.function.Function;

import com.sdc2ch.core.lambda.tuple.Tuple1;


@FunctionalInterface
public interface Function1<T1, R> extends Function<T1, R> {

    
    default R apply(Tuple1<T1> args) {
        return apply(args.v1);
    }

    
    @Override
    R apply(T1 v1);
    
    
    default Function<T1, R> toFunction() {
        return t -> apply(t);
    }

    
    static <T1, R> Function1<T1, R> from(Function<T1, R> function) {
        return v1 -> function.apply(v1);
    }
    
}
