package com.jark.template.common.mybatis.repository;

import java.util.Collection;
import java.util.Optional;

import com.jark.template.common.mybatis.entity.BaseEntity;

/**
 * Repository 接口
 * @param <T> 实体类
 * @param <ID> 主键Id
 */
public interface BaseRepository<T extends BaseEntity<T>, ID> {

    int save(T entity);

    int insertSelective(T entity);

    int insertMultiple(Collection<T> records);

    boolean deleteById(ID id);

    int updateById(T entity);

    int updateByPrimaryKeySelective(T entity);

    Optional<T> selectById(ID id);



}


