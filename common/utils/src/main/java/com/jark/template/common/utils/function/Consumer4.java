package com.jark.template.common.utils.function;

import java.util.Objects;

/**
 * @author Ponder
 * @Type TriConsumer.java
 * @Desc 三个参数处理
 * @date 2023/4/11 18:20
 */
@FunctionalInterface
public interface Consumer4<T1, T2, T3, T4> {

    /**
     *
     */
    void accept(T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * andThen
     */
    default Consumer4<T1, T2, T3, T4> andThen(final Consumer4<? super T1, ? super T2, ? super T3, ? super T4> after) {
        Objects.requireNonNull(after);
        return (t1, t2, t3, t4) -> {
            accept(t1, t2, t3, t4);
            after.accept(t1, t2, t3, t4);
        };
    }

}



