package com.jark.template.common.mybatis.paging;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.SelectModel;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;

/**
 * 分野操作
 */
public final class LimitAndOffsetAdapter<R> {
    private final SelectModel selectModel;

    private final Function<SelectStatementProvider, R> mapperMethod;

    private final int limit;

    private final int offset;

    private LimitAndOffsetAdapter(SelectModel selectModel, Function<SelectStatementProvider, R> mapperMethod,
                                  int limit, int offset) {
        this.selectModel = Objects.requireNonNull(selectModel);
        this.mapperMethod = Objects.requireNonNull(mapperMethod);
        this.limit = limit;
        this.offset = offset;
    }

    public static <R> LimitAndOffsetAdapter<R> of(SelectModel selectModel,
                                                  Function<SelectStatementProvider, R> mapperMethod, int limit, int offset) {
        return new LimitAndOffsetAdapter<>(selectModel, mapperMethod, limit, offset);
    }

    public R execute() {
        return mapperMethod.apply(selectStatement());
    }

    private SelectStatementProvider selectStatement() {
        return new LimitAndOffsetDecorator(
            selectModel.render(RenderingStrategies.MYBATIS3));
    }

    /**
     * 分页
     */
    public class LimitAndOffsetDecorator implements SelectStatementProvider {
        private final Map<String, Object> parameters = new HashMap<>();

        private final String selectStatement;

        public LimitAndOffsetDecorator(SelectStatementProvider delegate) {
            parameters.putAll(delegate.getParameters());
            parameters.put("limit", limit);
            parameters.put("offset", offset);

            selectStatement = delegate.getSelectStatement() +
                " LIMIT #{parameters.limit} OFFSET #{parameters.offset}";
        }

        @Override
        public Map<String, Object> getParameters() {
            return parameters;
        }

        @Override
        public String getSelectStatement() {
            return selectStatement;
        }
    }
}

