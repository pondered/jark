package com.jark.template.common.utils.function;

import java.util.Objects;

/**
 * @author Ponder
 * @Type TriConsumer.java
 * @Desc 三个参数处理
 * @date 2023/4/11 18:20
 */
@FunctionalInterface
public interface Consumer3<T1, T2, T3> {

    /**
     *
     */
    void accept(T1 t1, T2 t2, T3 t3);

    /**
     * andThen
     */
    default Consumer3<T1, T2, T3> andThen(final Consumer3<? super T1, ? super T2, ? super T3> after) {
        Objects.requireNonNull(after);
        return (t1, t2, t3) -> {
            accept(t1, t2, t3);
            after.accept(t1, t2, t3);
        };
    }

}



