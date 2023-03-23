package com.jark.template.common.redis.lock.type;

import org.springframework.data.redis.core.RedisTemplate;

import com.jark.template.common.redis.lock.LockInfo;
import com.jark.template.common.utils.exception.NotImplementedException;

import lombok.extern.slf4j.Slf4j;

/**
 * Redis读锁.
 *
 * @author ponder
 */
@Slf4j
public final class ReadLock implements Lock {
    private final LockInfo lockInfo;

    private final RedisTemplate<String, Object> redisTemplate;

    public ReadLock(final RedisTemplate<String, Object> redisTemplate, final LockInfo lockInfo) {
        this.lockInfo = lockInfo;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean acquire() {
        throw new NotImplementedException("Method Not implemented");
    }

    @Override
    public boolean release() {
        throw new NotImplementedException("Method Not implemented");
    }
}
