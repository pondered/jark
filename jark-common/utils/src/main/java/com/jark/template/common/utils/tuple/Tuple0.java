package com.jark.template.common.utils.tuple;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 两个参数的Tuple
 */
public final class Tuple0 implements Tuple, Comparable<Tuple0>, Serializable {
    private static final Tuple0 INSTANCE = new Tuple0();

    private static final Comparator<Tuple0> COMPARATOR = (Comparator<Tuple0> & Serializable) (t1, t2) -> 0;

    private Tuple0() {
    }

    public static Tuple0 instance() {
        return INSTANCE;
    }

    public static Comparator<Tuple0> comparator() {
        return COMPARATOR;
    }

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public List<?> toList() {
        return Collections.emptyList();
    }

    @Override
    public int compareTo(final Tuple0 o) {
        return 0;
    }

    public <T1> Tuple1<T1> append(T1 t1) {
        return Tuple.of(t1);
    }

}


