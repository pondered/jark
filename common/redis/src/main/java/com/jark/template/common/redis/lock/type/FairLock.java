package com.jark.template.common.redis.lock.type;

import org.springframework.data.redis.core.RedisTemplate;

import com.jark.template.common.redis.lock.LockInfo;
import com.jark.template.common.utils.exception.NotImplementedException;

import lombok.extern.slf4j.Slf4j;

/**
 * 公平锁。
 * 在提供了自动过期解锁功能的同时，保证了当多个Redisson客户端线程同时请求加锁时，优先分配给先发出请求的线程。
 *
 * @author ponder
 */
@Slf4j
public final class FairLock implements Lock {
    private final LockInfo lockInfo;

    private final RedisTemplate<String, Object> redisTemplate;

    public FairLock(final RedisTemplate<String, Object> redisTemplate, final LockInfo lockInfo) {
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
