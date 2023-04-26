package com.jark.template.common.mybatis.service.impl;

import java.util.Optional;

import com.jark.template.common.mybatis.entity.BaseEntity;
import com.jark.template.common.mybatis.repository.BaseRepository;
import com.jark.template.common.mybatis.service.BaseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @param <T> 实体类
 * @param <ID> 主键Id
 * @param <R> Repository实现类
 *
 * @author ponder
 */
@Slf4j
@RequiredArgsConstructor
public abstract class BaseServiceImpl<T extends BaseEntity<T>, R extends BaseRepository<T, ID>, ID> implements BaseService<T, ID> {

    private final R baseRepository;

    @Override
    public Optional<T> getById(final ID id) {
        return baseRepository.selectById(id);
    }

    @Override
    public int save(final T entity) {
        return baseRepository.save(entity);
    }

    @Override
    public boolean deleteById(final ID id) {
        return baseRepository.deleteById(id);
    }

    @Override
    public int updateById(final T entity) {
        return baseRepository.updateById(entity);
    }
}


