package com.jark.template.common.mybatis.paging;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Ponder
 * @Type IPage.java
 * @date 2023/4/27 10:19
 */
public interface IPage<T> extends Iterable<T>, Supplier<Stream<T>> {
    static Page empty() {
        return Page.EMPTY;
    }

    /**
     * 当前满足条件总行数
     *
     * @return 总条数
     */
    long getTotal();

    /**
     * 获取每页显示条数
     *
     * @return 每页显示条数
     */
    long getSize();

    /**
     * 当前页
     *
     * @return 当前页
     */
    long getCurrent();

    Integer getPageSize();

    List<T> getRecords();

    IPage<T> setRecords(List<T> records);

    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    default <R> IPage<R> map(Function<? super T, ? extends R> converter) {
        List<R> collect = StreamSupport.stream(spliterator(), false).map(converter).collect(Collectors.toList());
        return ((IPage<R>) this).setRecords(collect);
    }
}



