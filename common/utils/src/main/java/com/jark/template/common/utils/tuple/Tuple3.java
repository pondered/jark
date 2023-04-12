package com.jark.template.common.utils.tuple;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import com.jark.template.common.utils.function.Function3;

/**
 * 两个参数的Tuple
 */
public final class Tuple3<T1, T2, T3> implements Tuple, Comparable<Tuple3<T1, T2, T3>>, Serializable {

    public final T1 _1;

    public final T2 _2;

    public final T3 _3;

    public Tuple3(final T1 _1, final T2 _2, final T3 _3) {
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
    }

    public static <T1, T2, T3> Comparator<Tuple3<T1, T2, T3>> comparator(Comparator<? super T1> t1Comp, Comparator<? super T2> t2Comp,
                                                                         Comparator<? super T3> t3Comp) {
        return (Comparator<Tuple3<T1, T2, T3>> & Serializable) (t1, t2) -> {
            final int check1 = t1Comp.compare(t1._1, t2._1);
            if (check1 != 0) {
                return check1;
            }

            final int check2 = t2Comp.compare(t1._2, t2._2);
            if (check2 != 0) {
                return check2;
            }

            return t3Comp.compare(t1._3, t2._3);
        };
    }

    private static <U1 extends Comparable<? super U1>, U2 extends Comparable<? super U2>, U3 extends Comparable<? super U3>> int compareTo(
        Tuple3<?, ?, ?> o1, Tuple3<?, ?, ?> o2) {
        final Tuple3<U1, U2, U3> t1 = (Tuple3<U1, U2, U3>) o1;
        final Tuple3<U1, U2, U3> t2 = (Tuple3<U1, U2, U3>) o2;

        final int check1 = t1._1.compareTo(t2._1);
        if (check1 != 0) {
            return check1;
        }

        final int check2 = t1._2.compareTo(t2._2);
        if (check2 != 0) {
            return check2;
        }
        return t1._3.compareTo(t2._3);
    }

    public T1 _1() {
        return _1;
    }

    public T2 _2() {
        return _2;
    }

    public T3 _3() {
        return _3;
    }

    public Tuple3<T1, T2, T3> update1(T1 value) {
        return new Tuple3<>(value, _2, _3);
    }

    public Tuple3<T1, T2, T3> update2(T2 value) {
        return new Tuple3<>(_1, value, _3);
    }

    public Tuple3<T1, T2, T3> update3(T3 value) {
        return new Tuple3<>(_1, _2, value);
    }

    public <U1, U2, U3> Tuple3<U1, U2, U3> map(Function3<? super T1, ? super T2, ? super T3, Tuple3<U1, U2, U3>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return mapper.apply(_1, _2, _3);
    }

    public <U1, U2, U3> Tuple3<U1, U2, U3> map(Function<? super T1, ? extends U1> f1, Function<? super T2, ? extends U2> f2,
                                               Function<? super T3, ? extends U3> f3) {
        Objects.requireNonNull(f1, "f1 is null");
        Objects.requireNonNull(f2, "f2 is null");
        Objects.requireNonNull(f3, "f3 is null");
        return Tuple.of(f1.apply(_1), f2.apply(_2), f3.apply(_3));
    }

    public <U> Tuple3<U, T2, T3> map1(Function<? super T1, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_1);
        return Tuple.of(u, _2, _3);
    }

    public <U> Tuple3<T1, U, T3> map2(Function<? super T2, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_2);
        return Tuple.of(_1, u, _3);
    }

    public <U> Tuple3<T1, T2, U> map3(Function<? super T3, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_3);
        return Tuple.of(_1, _2, u);
    }

    public <U> U apply(Function3<? super T1, ? super T2, ? super T3, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(_1, _2, _3);
    }

    public <T4> Tuple4<T1, T2, T3, T4> append(T4 t4) {
        return Tuple.of(_1, _2, _3, t4);
    }

    public <T4> Tuple4<T1, T2, T3, T4> concat(Tuple1<T4> tuple) {
        Objects.requireNonNull(tuple, "tuple is null");
        return Tuple.of(_1, _2, _3, tuple._1);
    }

    @Override
    public int arity() {
        return 3;
    }

    @Override
    public List<?> toList() {
        return List.of(_1, _2, _3);
    }

    @Override
    public int compareTo(final Tuple3<T1, T2, T3> that) {
        return Tuple3.compareTo(this, that);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Tuple3)) {
            return false;
        } else {
            final Tuple3<?, ?, ?> that = (Tuple3<?, ?, ?>) o;
            return Objects.equals(this._1, that._1)
                && Objects.equals(this._2, that._2)
                && Objects.equals(this._3, that._3);
        }
    }

    @Override
    public int hashCode() {
        return Tuple.hash(_1, _2, _3);
    }

    @Override
    public String toString() {
        return "(" + _1 + ", " + _2 + ", " + _3 + ")";
    }

}


