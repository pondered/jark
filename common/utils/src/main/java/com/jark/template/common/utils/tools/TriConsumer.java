package com.jark.template.common.utils.tools;

import java.util.Objects;

/**
 * @author Ponder
 * @Type TriConsumer.java
 * @Desc 三个参数处理
 * @date 2023/4/11 18:20
 */
@FunctionalInterface
public interface TriConsumer<T, U, V> {

    /**
     */
    void accept(T first, U second, V third);

    /**
     * andThen
     */
    default TriConsumer<T, U, V> andThen(final TriConsumer<? super T, ? super U, ? super V> after) {
        Objects.requireNonNull(after);
        return (t, u, v) -> {
            accept(t, u, v);
            after.accept(t, u, v);
        };
    }

}



