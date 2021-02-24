package com.sdc2ch.core.lambda.tuple;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.sdc2ch.core.lambda.function.Function1;
import com.sdc2ch.core.lambda.function.Function2;

public class Tuple2<T1, T2> implements Tuple, Comparable<Tuple2<T1, T2>>, Serializable, Cloneable {
    
	private static final long serialVersionUID = -3801037807787057041L;
	public final T1 v1;
    public final T2 v2;
    
    public T1 v1() {
        return v1;
    }
    
    public T2 v2() {
        return v2;
    }
    
    public Tuple2(Tuple2<T1, T2> tuple) {
        this.v1 = tuple.v1;
        this.v2 = tuple.v2;
    }

    public Tuple2(T1 v1, T2 v2) {
        this.v1 = v1;
        this.v2 = v2;
    }
    
    
    public final Tuple2<T2, T1> swap() {
        return new Tuple2<>(v2, v1);
    }

    
    public static final <T extends Comparable<T>> boolean overlaps(Tuple2<T, T> left, Tuple2<T, T> right) {
        return left.v1.compareTo(right.v2) <= 0
            && left.v2.compareTo(right.v1) >= 0;
    }

    
    public static final <T extends Comparable<T>> Optional<Tuple2<T, T>> intersect(Tuple2<T, T> left, Tuple2<T, T> right) {
        if (overlaps(left, right))
            return Optional.of(new Tuple2<>(
                left.v1.compareTo(right.v1) >= 0 ? left.v1 : right.v1,
                left.v2.compareTo(right.v2) <= 0 ? left.v2 : right.v2
            ));
        else
            return Optional.empty();
    }
    
    
    public final <R> R map(Function2<T1, T2, R> function) {
        return function.apply(this);
    }
    
    
    public final <U1> Tuple2<U1, T2> map1(Function1<T1, U1> function) {
        return Tuple.tuple(function.apply(v1), v2);
    }
    
    
    public final <U2> Tuple2<T1, U2> map2(Function1<T2, U2> function) {
        return Tuple.tuple(v1, function.apply(v2));
    }
    
    @Override
    public final Object[] array() {
        return new Object[] { v1, v2 };
    }

    @Override
    public final List<?> list() {
        return Arrays.asList(array());
    }

    
    @Override
    public final int degree() {
        return 2;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final Iterator<Object> iterator() {
        return (Iterator<Object>) list().iterator();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public int compareTo(Tuple2<T1, T2> other) {
        int result = 0;
        
        result = ((Comparable) v1).compareTo((Comparable) other.v1); if (result != 0) return result;
        result = ((Comparable) v2).compareTo((Comparable) other.v2); if (result != 0) return result;

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Tuple2))
            return false;

        @SuppressWarnings({ "unchecked", "rawtypes" })
        final Tuple2<T1, T2> that = (Tuple2) o;
        
        if (v1 != that.v1) {
            if (v1 == null ^ that.v1 == null)
                return false;

            if (!v1.equals(that.v1))
                return false;
        }
        
        if (v2 != that.v2) {
            if (v2 == null ^ that.v2 == null)
                return false;

            if (!v2.equals(that.v2))
                return false;
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        
        result = prime * result + ((v1 == null) ? 0 : v1.hashCode());
        result = prime * result + ((v2 == null) ? 0 : v2.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return "("
             +        v1
             + ", " + v2
             + ")";
    }

    @Override
    public Tuple2<T1, T2> clone() {
        return new Tuple2<>(this);
    }
}
