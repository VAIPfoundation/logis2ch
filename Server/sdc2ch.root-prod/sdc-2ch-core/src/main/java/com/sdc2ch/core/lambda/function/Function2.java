package com.sdc2ch.core.lambda.function;

import java.util.function.BiFunction;

import com.sdc2ch.core.lambda.tuple.Tuple2;


@FunctionalInterface
public interface Function2<T1, T2, R> extends BiFunction<T1, T2, R> {

    
    default R apply(Tuple2<T1, T2> args) {
        return apply(args.v1, args.v2);
    }

    
    @Override
    R apply(T1 v1, T2 v2);
    
    
    default BiFunction<T1, T2, R> toBiFunction() {
        return (t1, t2) -> apply(t1, t2);
    }

    
    static <T1, T2, R> Function2<T1, T2, R> from(BiFunction<T1, T2, R> function) {
        return (v1, v2) -> function.apply(v1, v2);
    }
    
}
