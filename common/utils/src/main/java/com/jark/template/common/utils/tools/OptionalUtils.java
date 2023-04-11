package com.jark.template.common.utils.tools;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * Optional 工具类
 */
public final class OptionalUtils {

    private OptionalUtils() {
    }


    /**
     * 支持两个Optional方法参数
     */
    public static <T, U, R> Optional<R> apply(final Optional<T> opt1, final Optional<U> opt2, final BiFunction<T, U, R> mapper) {
        if (Stream.of(opt1, opt2).allMatch(Optional::isPresent)) {
            return Optional.ofNullable(mapper.apply(opt1.get(), opt2.get()));
        }
        return Optional.empty();
    }

    /**
     * 支持三个Optional方法参数
     */
    public static <T, U, V, R> Optional<R> apply(final Optional<T> opt1, final Optional<U> opt2, final Optional<V> opt3,
                                                 final TriFunction<T, U, V, R> mapper) {
        if (Stream.of(opt1, opt2, opt3).allMatch(Optional::isPresent)) {
            return Optional.ofNullable(mapper.apply(opt1.get(), opt2.get(), opt3.get()));
        }
        return Optional.empty();
    }


    /**
     * 支持两个Optional方法参数
     */
    public static <T, U> void accept(final Optional<T> opt1, final Optional<U> opt2, final BiConsumer<T, U> mapper) {
        if (Stream.of(opt1, opt2).allMatch(Optional::isPresent)) {
            mapper.accept(opt1.get(), opt2.get());
        }
    }


    /**
     * 支持三个Optional方法参数
     */
    public static <T, U, V> void accept(final Optional<T> opt1, final Optional<U> opt2, final Optional<V> opt3, final TriConsumer<T, U, V> mapper) {
        if (Stream.of(opt1, opt2, opt3).allMatch(Optional::isPresent)) {
            mapper.accept(opt1.get(), opt2.get(), opt3.get());
        }
    }

}


