package com.jark.template.common.mybatis.mapper;

import java.util.Collection;
import java.util.Optional;

import org.mybatis.dynamic.sql.util.mybatis3.CommonCountMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonDeleteMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonInsertMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonUpdateMapper;

import com.jark.template.common.mybatis.entity.BaseEntity;

/**
 * @param <T> 实体类
 * @param <ID> 主键Id
 *
 * @author Ponder
 */
public interface BaseMapper<T extends BaseEntity<T>, ID>
    extends CommonCountMapper, CommonDeleteMapper, CommonInsertMapper<T>, CommonUpdateMapper {
    int insert(T entity);

    boolean deleteByPrimaryKey(ID id);

    int updateByPrimaryKey(T entity);

    int insertSelective(T entity);

    int insertMultiple(Collection<T> records);

    int updateByPrimaryKeySelective(T entity);

    Optional<T> selectByPrimaryKey(ID id);

}



