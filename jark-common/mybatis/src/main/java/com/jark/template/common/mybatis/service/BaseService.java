package com.jark.template.common.mybatis.service;

import java.util.Optional;

import com.jark.template.common.mybatis.entity.BaseEntity;

/**
 * 基础Service类
 * @param <T> 实体类
 * @param <ID> 主键Id
 */
public interface BaseService<T extends BaseEntity<T>, ID> {

    Optional<T> getById(ID id);

    /**
     * 添加数据并返回Id
     */
    int save(T entity);

    /**
     * 根据Id删除数据(逻辑删除)
     */
    boolean deleteById(ID id);

    /**
     * 根据Id更新记录
     */
    int updateById(T entity);

}


