package com.jark.template.common.redis.lock.type;

import java.time.Duration;
import java.util.Collections;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import com.jark.template.common.redis.constants.RedisConstEnum;
import com.jark.template.common.redis.lock.LockInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * 互斥锁.
 *
 * @author ponder
 */
@Slf4j
public final class ReentrantLock implements Lock {
    private final LockInfo lockInfo;

    private final RedisTemplate<String, Object> redisTemplate;

    public ReentrantLock(final RedisTemplate<String, Object> redisTemplate, final LockInfo lockInfo) {
        this.lockInfo = lockInfo;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean acquire() {
        return Boolean.TRUE.equals(
            redisTemplate.opsForValue().setIfAbsent(lockInfo.getLockName(), lockInfo.getValue(), Duration.ofSeconds(lockInfo.getExpire())));
    }

    @Override
    public boolean release() {
        final RedisScript<Boolean> redisScript = RedisScript.of(RedisConstEnum.LUA_SCRIPT.toString(), Boolean.class);
        return Boolean.TRUE.equals(redisTemplate.execute(redisScript, Collections.singletonList(lockInfo.getLockName()), lockInfo.getValue()));
    }
}
