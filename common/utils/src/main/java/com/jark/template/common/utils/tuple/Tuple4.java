package com.jark.template.common.utils.tuple;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import com.jark.template.common.utils.function.Function4;

/**
 * 两个参数的Tuple
 */
public final class Tuple4<T1, T2, T3, T4> implements Tuple, Comparable<Tuple4<T1, T2, T3, T4>>, Serializable {

    public final T1 _1;

    public final T2 _2;

    public final T3 _3;

    public final T4 _4;

    public Tuple4(final T1 _1, final T2 _2, final T3 _3, final T4 _4) {
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
        this._4 = _4;
    }

    public static <T1, T2, T3, T4> Comparator<Tuple4<T1, T2, T3, T4>> comparator(Comparator<? super T1> t1Comp, Comparator<? super T2> t2Comp,
                                                                                 Comparator<? super T3> t3Comp, Comparator<? super T4> t4Comp) {
        return (Comparator<Tuple4<T1, T2, T3, T4>> & Serializable) (t1, t2) -> {
            final int check1 = t1Comp.compare(t1._1, t2._1);
            if (check1 != 0) {
                return check1;
            }

            final int check2 = t2Comp.compare(t1._2, t2._2);
            if (check2 != 0) {
                return check2;
            }

            final int check3 = t3Comp.compare(t1._3, t2._3);
            if (check3 != 0) {
                return check3;
            }
            return t4Comp.compare(t1._4, t2._4);
        };
    }

    private static <U1 extends Comparable<? super U1>,
        U2 extends Comparable<? super U2>,
        U3 extends Comparable<? super U3>,
        U4 extends Comparable<? super U4>> int compareTo(
        Tuple4<?, ?, ?, ?> o1, Tuple4<?, ?, ?, ?> o2) {
        final Tuple4<U1, U2, U3, U4> t1 = (Tuple4<U1, U2, U3, U4>) o1;
        final Tuple4<U1, U2, U3, U4> t2 = (Tuple4<U1, U2, U3, U4>) o2;

        final int check1 = t1._1.compareTo(t2._1);
        if (check1 != 0) {
            return check1;
        }

        final int check2 = t1._2.compareTo(t2._2);
        if (check2 != 0) {
            return check2;
        }

        final int check3 = t1._3.compareTo(t2._3);
        if (check3 != 0) {
            return check3;
        }
        return t1._4.compareTo(t2._4);
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

    public T4 _4() {
        return _4;
    }

    public Tuple4<T1, T2, T3, T4> update1(T1 value) {
        return new Tuple4<>(value, _2, _3, _4);
    }

    public Tuple4<T1, T2, T3, T4> update2(T2 value) {
        return new Tuple4<>(_1, value, _3, _4);
    }

    public Tuple4<T1, T2, T3, T4> update3(T3 value) {
        return new Tuple4<>(_1, _2, value, _4);
    }

    public Tuple4<T1, T2, T3, T4> update4(T4 value) {
        return new Tuple4<>(_1, _2, _3, value);
    }

    public <U1, U2, U3, U4> Tuple4<U1, U2, U3, U4> map(Function4<? super T1, ? super T2, ? super T3, ? super T4, Tuple4<U1, U2, U3, U4>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return mapper.apply(_1, _2, _3, _4);
    }

    public <U1, U2, U3, U4> Tuple4<U1, U2, U3, U4> map(Function<? super T1, ? extends U1> f1, Function<? super T2, ? extends U2> f2,
                                                       Function<? super T3, ? extends U3> f3, Function<? super T4, ? extends U4> f4) {
        Objects.requireNonNull(f1, "f1 is null");
        Objects.requireNonNull(f2, "f2 is null");
        Objects.requireNonNull(f3, "f3 is null");
        Objects.requireNonNull(f4, "f4 is null");
        return Tuple.of(f1.apply(_1), f2.apply(_2), f3.apply(_3), f4.apply(_4));
    }

    public <U> Tuple4<U, T2, T3, T4> map1(Function<? super T1, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_1);
        return Tuple.of(u, _2, _3, _4);
    }

    public <U> Tuple4<T1, U, T3, T4> map2(Function<? super T2, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_2);
        return Tuple.of(_1, u, _3, _4);
    }

    public <U> Tuple4<T1, T2, U, T4> map3(Function<? super T3, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_3);
        return Tuple.of(_1, _2, u, _4);
    }

    public <U> Tuple4<T1, T2, T3, U> map4(Function<? super T4, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_4);
        return Tuple.of(_1, _2, _3, u);
    }

    public <U> U apply(Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(_1, _2, _3, _4);
    }

    @Override
    public int arity() {
        return 4;
    }

    @Override
    public List<?> toList() {
        return List.of(_1, _2, _3, _4);
    }


    @Override
    public int compareTo(final Tuple4<T1, T2, T3, T4> that) {
        return Tuple4.compareTo(this, that);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Tuple4)) {
            return false;
        } else {
            final Tuple4<?, ?, ?, ?> that = (Tuple4<?, ?, ?, ?>) o;
            return Objects.equals(this._1, that._1)
                && Objects.equals(this._2, that._2)
                && Objects.equals(this._3, that._3)
                && Objects.equals(this._4, that._4);
        }
    }

    @Override
    public int hashCode() {
        return Tuple.hash(_1, _2, _3, _4);
    }

    @Override
    public String toString() {
        return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ")";
    }

}


