package com.jark.template.common.utils.tuple;


import java.util.List;
import java.util.Objects;

/**
 * 思路来自 var Tuple
 */
public sealed interface Tuple permits Tuple0, Tuple1, Tuple2, Tuple3, Tuple4 {

    static <T1> Tuple1<T1> of(T1 t1) {
        return new Tuple1<>(t1);
    }

    static <T1, T2> Tuple2<T1, T2> of(T1 t1, T2 t2) {
        return new Tuple2<>(t1, t2);
    }

    static <T1, T2, T3> Tuple3<T1, T2, T3> of(T1 t1, T2 t2, T3 t3) {
        return new Tuple3<>(t1, t2, t3);
    }

    static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> of(T1 t1, T2 t2, T3 t3, T4 t4) {
        return new Tuple4<>(t1, t2, t3, t4);
    }

    static int hash(Object o1) {
        return Objects.hash(o1);
    }

    static int hash(Object o1, Object o2) {
        return Objects.hash(o1, o2);
    }

    static int hash(Object o1, Object o2, Object o3) {
        return Objects.hash(o1, o2, o3);
    }

    static int hash(Object o1, Object o2, Object o3, Object o4) {
        return Objects.hash(o1, o2, o3, o4);
    }

    int arity();

    List<?> toList();

}


