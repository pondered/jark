package com.jark.template.common.utils.function;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Ponder
 * @Type TriConsumer.java
 * @Desc 三个参数处理
 * @date 2023/4/11 18:20
 */
@FunctionalInterface
public interface Function3<T1, T2, T3, R> {
    static <T1, T2, T3, R> Function3<T1, T2, T3, R> constant(R value) {
        return (t1, t2, t3) -> value;
    }

    static <T1, T2, T3, R> Function3<T1, T2, T3, R> of(Function3<T1, T2, T3, R> methodReference) {
        return methodReference;
    }

    static <T1, T2, T3, R> Function3<T1, T2, T3, R> narrow(Function3<? super T1, ? super T2, ? super T3, ? extends R> f) {
        return (Function3<T1, T2, T3, R>) f;
    }

    default BiFunction<T2, T3, R> apply(T1 t1) {
        return (T2 t2, T3 t3) -> apply(t1, t2, t3);
    }

    default Function<T3, R> apply(T1 t1, T2 t2) {
        return (T3 t3) -> apply(t1, t2, t3);
    }

    /**
     * apply
     */
    R apply(T1 t1, T2 t2, T3 t3);

    default <V> Function3<T1, T2, T3, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after, "after is null");
        return (t1, t2, t3) -> after.apply(apply(t1, t2, t3));
    }

}



