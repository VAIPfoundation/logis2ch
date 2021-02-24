package com.sdc2ch.core.lambda.tuple;

import java.util.List;



public interface Tuple extends Iterable<Object> {

    
    static <T1> Tuple1<T1> tuple(T1 v1) {
        return new Tuple1<>(v1);
    }

    
    static <T1, T2> Tuple2<T1, T2> tuple(T1 v1, T2 v2) {
        return new Tuple2<>(v1, v2);
    }

    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	static <T extends Comparable<T>> Range<T> range(T t1, T t2) {
        return new Range(t1, t2);
    }

    
    Object[] array();

    
    List<?> list();

    
    int degree();
}
