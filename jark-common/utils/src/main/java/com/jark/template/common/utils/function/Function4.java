package com.jark.template.common.utils.function;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Ponder
 * @Type TriConsumer.java
 * @Desc 四个参数处理
 * @date 2023/4/11 18:20
 */
@FunctionalInterface
public interface Function4<T1, T2, T3, T4, R> {

    static <T1, T2, T3, T4, R> Function4<T1, T2, T3, T4, R> constant(R value) {
        return (t1, t2, t3, t4) -> value;
    }

    static <T1, T2, T3, T4, R> Function4<T1, T2, T3, T4, R> of(Function4<T1, T2, T3, T4, R> methodReference) {
        return methodReference;
    }

    static <T1, T2, T3, T4, R> Function4<T1, T2, T3, T4, R> narrow(Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f) {
        return (Function4<T1, T2, T3, T4, R>) f;
    }

    default Function3<T2, T3, T4, R> apply(T1 t1) {
        return (T2 t2, T3 t3, T4 t4) -> apply(t1, t2, t3, t4);
    }

    default BiFunction<T3, T4, R> apply(T1 t1, T2 t2) {
        return (T3 t3, T4 t4) -> apply(t1, t2, t3, t4);
    }

    default Function<T4, R> apply(T1 t1, T2 t2, T3 t3) {
        return (T4 t4) -> apply(t1, t2, t3, t4);
    }

    /**
     * apply
     */
    R apply(T1 t1, T2 t2, T3 t3, T4 t4);

    default <V> Function4<T1, T2, T3, T4, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after, "after is null");
        return (t1, t2, t3, t4) -> after.apply(apply(t1, t2, t3, t4));
    }
}



