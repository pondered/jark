package com.jark.template.common.mybatis.repository.impl;

import java.util.Collection;
import java.util.Optional;

import com.jark.template.common.mybatis.entity.BaseEntity;
import com.jark.template.common.mybatis.mapper.BaseMapper;
import com.jark.template.common.mybatis.repository.BaseRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Repository 抽象类
 *
 * @param <T> 实体类
 * @param <M> Mapper
 * @param <ID> 主键Id
 */
@Slf4j
@RequiredArgsConstructor
public abstract class BaseRepositoryImpl<T extends BaseEntity<T>, M extends BaseMapper<T, ID>, ID> implements BaseRepository<T, ID> {

    private final M baseMapper;

    @Override
    public int insertSelective(final T entity) {
        return baseMapper.insertSelective(entity);
    }

    @Override
    public int insertMultiple(final Collection<T> records) {
        return baseMapper.insertMultiple(records);
    }

    @Override
    public int updateByPrimaryKeySelective(final T entity) {
        return baseMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public Optional<T> selectById(final ID id) {
        return baseMapper.selectByPrimaryKey(id);
    }

    @Override
    public int save(final T entity) {
        return baseMapper.insert(entity);
    }

    @Override
    public int deleteById(final ID id) {
        return baseMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(final T entity) {
        return baseMapper.updateByPrimaryKey(entity);
    }
}


