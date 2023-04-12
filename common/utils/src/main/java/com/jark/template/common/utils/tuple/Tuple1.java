package com.jark.template.common.utils.tuple;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * 两个参数的Tuple
 */
public final class Tuple1<T1> implements Tuple, Comparable<Tuple1<T1>>, Serializable {
    public final T1 _1;

    public Tuple1(T1 t1) {
        this._1 = t1;
    }

    public static <T1> Comparator<Tuple1<T1>> comparator(Comparator<? super T1> t1Comp) {
        return (Comparator<Tuple1<T1>> & Serializable) (t1, t2) -> t1Comp.compare(t1._1, t2._1);
    }

    private static <U1 extends Comparable<? super U1>> int compareTo(Tuple1<?> o1, Tuple1<?> o2) {
        final Tuple1<U1> t1 = (Tuple1<U1>) o1;
        final Tuple1<U1> t2 = (Tuple1<U1>) o2;
        return t1._1.compareTo(t2._1);
    }


    public T1 _1() {
        return _1;
    }

    public Tuple1<T1> update1(T1 value) {
        return Tuple.of(value);
    }

    public <U1> Tuple1<U1> map(Function<? super T1, ? extends U1> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return Tuple.of(mapper.apply(_1));
    }

    public <U> U apply(Function<? super T1, ? extends U> function) {
        Objects.requireNonNull(function, "mapper is null");
        return function.apply(_1);
    }

    public <T2> Tuple2<T1, T2> append(T2 t2) {
        return Tuple.of(_1, t2);
    }

    public <T2, T3> Tuple3<T1, T2, T3> concat(T2 t2, T3 t3) {
        return Tuple.of(_1, t2, t3);
    }

    public <T2, T3, T4> Tuple4<T1, T2, T3, T4> concat(T2 t2, T3 t3, T4 t4) {
        return Tuple.of(_1, t2, t3, t4);
    }

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public List<?> toList() {
        return List.of(_1);
    }

    @Override
    public int compareTo(final Tuple1<T1> that) {
        return Tuple1.compareTo(this, that);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Tuple1)) {
            return false;
        } else {
            final Tuple1<?> that = (Tuple1<?>) o;
            return Objects.equals(this._1, that._1);
        }
    }

    @Override
    public int hashCode() {
        return Tuple.hash(_1);
    }

    @Override
    public String toString() {
        return "(" + _1 + ")";
    }
}


