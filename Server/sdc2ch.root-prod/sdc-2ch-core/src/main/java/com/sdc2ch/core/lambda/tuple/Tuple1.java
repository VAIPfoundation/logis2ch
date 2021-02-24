package com.sdc2ch.core.lambda.tuple;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.sdc2ch.core.lambda.function.Function1;


public class Tuple1<T1> implements Tuple, Comparable<Tuple1<T1>>, Serializable, Cloneable {

	private static final long serialVersionUID = 3894508847693407129L;
	public final T1 v1;
    
    public T1 v1() {
        return v1;
    }
    
    public Tuple1(Tuple1<T1> tuple) {
        this.v1 = tuple.v1;
    }

    public Tuple1(T1 v1) {
        this.v1 = v1;
    }
    
    
    public final <R> R map(Function1<T1, R> function) {
        return function.apply(this);
    }
    
    
    public final <U1> Tuple1<U1> map1(Function1<T1, U1> function) {
        return Tuple.tuple(function.apply(v1));
    }
    
    @Override
    public final Object[] array() {
        return new Object[] { v1 };
    }

    @Override
    public final List<?> list() {
        return Arrays.asList(array());
    }

    
    @Override
    public final int degree() {
        return 1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final Iterator<Object> iterator() {
        return (Iterator<Object>) list().iterator();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public int compareTo(Tuple1<T1> other) {
        int result = 0;
        
        result = ((Comparable) v1).compareTo((Comparable) other.v1); if (result != 0) return result;

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Tuple1))
            return false;

        @SuppressWarnings({ "unchecked", "rawtypes" })
        final Tuple1<T1> that = (Tuple1) o;
        
        if (v1 != that.v1) {
            if (v1 == null ^ that.v1 == null)
                return false;

            if (!v1.equals(that.v1))
                return false;
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        
        result = prime * result + ((v1 == null) ? 0 : v1.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return "("
             +        v1
             + ")";
    }

    @Override
    public Tuple1<T1> clone() {
        return new Tuple1<>(this);
    }
}

