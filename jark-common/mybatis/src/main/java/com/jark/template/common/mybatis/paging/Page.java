package com.jark.template.common.mybatis.paging;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 分页
 */
@ToString
@EqualsAndHashCode
public class Page<T> implements IPage<T> {
    static final Page EMPTY = new Page();

    /**
     * 页码，从1开始
     */
    private final int pageIndex;

    /**
     * 页面大小
     */
    private final int pageSize;

    /**
     * 总数
     */
    private final long total;

    /**
     * 分页合理化
     */
    private final Boolean reasonable;

    /**
     * 查询数据列表
     */
    private List<T> records = Collections.emptyList();

    public Page() {
        this(0, 0);
    }

    /**
     * 分页构造函数
     *
     * @param current 当前页
     * @param size 每页显示条数
     */
    public Page(int current, int size) {
        this(current, size, 0);
    }

    public Page(int current, int size, int total) {
        this(current, size, total, true);
    }

    public Page(int current, int size, int total, boolean reasonable) {
        this(Collections.emptyList(), current, size, 0, reasonable);
    }

    public Page(final List<T> records, final int pageIndex, final int pageSize, final long total, final Boolean reasonable) {
        this.records = records;
        this.pageIndex = pageIndex;
        this.total = total;
        //分页合理化，针对不合理的页码自动处理
        if (pageIndex == 1 && pageSize == Integer.MAX_VALUE) {
            this.pageSize = 0;
        } else {
            this.pageSize = pageSize;
        }
        this.reasonable = reasonable;
    }

    @Override
    public long getTotal() {
        return this.total;
    }

    @Override
    public long getSize() {
        return this.total;
    }

    @Override
    public long getCurrent() {
        return this.pageIndex;
    }

    @Override
    public Integer getPageSize() {
        return this.pageSize;
    }

    @Override
    public List<T> getRecords() {
        return this.records;
    }

    @Override
    public IPage<T> setRecords(final List<T> records) {
        this.records = records;
        return this;
    }

    @Override
    public Iterator<T> iterator() {
        return records.iterator();
    }

    @Override
    public Stream<T> get() {
        return stream();
    }
}


