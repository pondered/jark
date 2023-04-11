package com.jark.template.common.utils.tools;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author Ponder
 * @Type TriConsumer.java
 * @Desc 三个参数处理
 * @date 2023/4/11 18:20
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R> {

    /**
     * apply
     */
    R apply(T first, U second, V third);

    default <Z> TriFunction<T, U, V, Z> andThen(final Function<? super R, ? extends Z> after) {
        Objects.requireNonNull(after);
        return (T a, U b, V c) -> after.apply(apply(a, b, c));
    }

}



