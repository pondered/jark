package com.jark.template.common.utils.tuple;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 两个参数的Tuple
 */
public final class Tuple2<T1, T2> implements Tuple, Comparable<Tuple2<T1, T2>>, Serializable {

    public final T1 _1;

    public final T2 _2;

    public Tuple2(final T1 _1, final T2 _2) {
        this._1 = _1;
        this._2 = _2;
    }

    public static <T1, T2> Comparator<Tuple2<T1, T2>> comparator(Comparator<? super T1> t1Comp, Comparator<? super T2> t2Comp) {
        return (Comparator<Tuple2<T1, T2>> & Serializable) (t1, t2) -> {
            final int check1 = t1Comp.compare(t1._1, t2._1);
            if (check1 != 0) {
                return check1;
            }
            return t2Comp.compare(t1._2, t2._2);
        };
    }

    private static <U1 extends Comparable<? super U1>, U2 extends Comparable<? super U2>> int compareTo(Tuple2<?, ?> o1, Tuple2<?, ?> o2) {
        final Tuple2<U1, U2> t1 = (Tuple2<U1, U2>) o1;
        final Tuple2<U1, U2> t2 = (Tuple2<U1, U2>) o2;

        final int check1 = t1._1.compareTo(t2._1);
        if (check1 != 0) {
            return check1;
        }
        return t1._2.compareTo(t2._2);
    }

    public T1 _1() {
        return _1;
    }

    public T2 _2() {
        return _2;
    }

    public Tuple2<T1, T2> update(T1 t1, T2 t2) {
        return Tuple.of(t1, t2);
    }

    public Tuple2<T1, T2> update1(T1 value) {
        return Tuple.of(value, _2);
    }

    public Tuple2<T1, T2> update2(T2 value) {
        return Tuple.of(_1, value);
    }

    public Tuple2<T2, T1> swap() {
        return Tuple.of(_2, _1);
    }

    public Map.Entry<T1, T2> toEntry() {
        return Map.entry(_1, _2);
    }

    public <U1, U2> Tuple2<U1, U2> map(BiFunction<? super T1, ? super T2, Tuple2<U1, U2>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return mapper.apply(_1, _2);
    }

    public <U1, U2> Tuple2<U1, U2> map(Function<? super T1, ? extends U1> f1, Function<? super T2, ? extends U2> f2) {
        Objects.requireNonNull(f1, "f1 is null");
        Objects.requireNonNull(f2, "f2 is null");
        return Tuple.of(f1.apply(_1), f2.apply(_2));
    }

    public <U> Tuple2<U, T2> map1(Function<? super T1, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_1);
        return Tuple.of(u, _2);
    }

    public <U> Tuple2<T1, U> map2(Function<? super T2, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_2);
        return Tuple.of(_1, u);
    }

    public <U> U apply(BiFunction<? super T1, ? super T2, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(_1, _2);
    }

    public <T3> Tuple3<T1, T2, T3> append(T3 t3) {
        return Tuple.of(_1, _2, t3);
    }

    public <T3> Tuple3<T1, T2, T3> concat(Tuple1<T3> tuple) {
        Objects.requireNonNull(tuple, "tuple is null");
        return Tuple.of(_1, _2, tuple._1);
    }

    public <T3, T4> Tuple4<T1, T2, T3, T4> concat(Tuple2<T3, T4> tuple) {
        Objects.requireNonNull(tuple, "tuple is null");
        return Tuple.of(_1, _2, tuple._1, tuple._2);
    }


    @Override
    public int arity() {
        return 2;
    }

    @Override
    public List<?> toList() {
        return List.of(_1, _2);
    }

    @Override
    public int compareTo(final Tuple2<T1, T2> that) {
        return Tuple2.compareTo(this, that);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Tuple2)) {
            return false;
        } else {
            final Tuple2<?, ?> that = (Tuple2<?, ?>) o;
            return Objects.equals(this._1, that._1)
                && Objects.equals(this._2, that._2);
        }
    }

    @Override
    public int hashCode() {
        return Tuple.hash(_1, _2);
    }

    @Override
    public String toString() {
        return "(" + _1 + ", " + _2 + ")";
    }
}


