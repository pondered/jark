package com.jark.template.common.mybatis.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.SqlTable;
import org.mybatis.dynamic.sql.delete.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.select.CountDSLCompleter;
import org.mybatis.dynamic.sql.select.QueryExpressionDSL;
import org.mybatis.dynamic.sql.select.SelectDSL;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateDSLCompleter;
import org.mybatis.dynamic.sql.update.UpdateModel;
import org.mybatis.dynamic.sql.util.mybatis3.CommonCountMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonDeleteMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonInsertMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonUpdateMapper;

import com.jark.template.common.mybatis.entity.BaseEntity;
import com.jark.template.common.mybatis.paging.LimitAndOffsetAdapter;

/**
 * @param <T> 实体类
 * @param <ID> 主键Id
 *
 * @author Ponder
 */
@Mapper
public interface BaseMapper<T extends BaseEntity<T>, ID>
    extends CommonCountMapper, CommonDeleteMapper, CommonInsertMapper<T>, CommonUpdateMapper {
    List<T> selectMany(SelectStatementProvider selectStatement);

    long count(CountDSLCompleter completer);

    int delete(DeleteDSLCompleter completer);

    int deleteByPrimaryKey(ID id);

    int insert(T row);

    int insertMultiple(Collection<T> records);

    int insertSelective(T row);

    Optional<T> selectOne(SelectStatementProvider selectStatement);

    Optional<T> selectOne(SelectDSLCompleter completer);

    List<T> select(SelectDSLCompleter completer);

    List<T> selectDistinct(SelectDSLCompleter completer);

    Optional<T> selectByPrimaryKey(ID id);

    int update(UpdateDSLCompleter completer);

    UpdateDSL<UpdateModel> updateAllColumns(T row, UpdateDSL<UpdateModel> dsl);

    UpdateDSL<UpdateModel> updateSelectiveColumns(T row, UpdateDSL<UpdateModel> dsl);

    int updateByPrimaryKey(T row);

    int updateByPrimaryKeySelective(T row);

    /**
     * 分页
     */
    default QueryExpressionDSL<LimitAndOffsetAdapter<List<T>>> selectWithLimitAndOffset(int limit, int offset, SqlTable table,
                                                                                        BasicColumn... selectList) {
        return SelectDSL.select(selectModel ->
            LimitAndOffsetAdapter.of(selectModel, this::selectMany, limit, offset), selectList).from(table);
    }

    default QueryExpressionDSL<LimitAndOffsetAdapter<List<T>>> selectWithLimitAndOffset(int limit, int offset, SqlTable table,
                                                                                        Function<SelectStatementProvider, List<T>> mapperMethod,
                                                                                        BasicColumn... selectList) {
        return SelectDSL.select(selectModel ->
            LimitAndOffsetAdapter.of(selectModel, mapperMethod, limit, offset), selectList).from(table);
    }
}



