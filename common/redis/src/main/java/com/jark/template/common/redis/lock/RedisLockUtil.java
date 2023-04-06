package com.jark.template.common.redis.lock;


import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.jark.template.common.redis.constants.RedisConstEnum;

import cn.hutool.core.util.BooleanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Redis锁工具类。
 *
 * @author ponder
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class RedisLockUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取锁.
     * 如果获取到了返回true，没有取到返回值false
     *
     * @param lockId 锁的id，唯一值
     * @param duration 时间
     *
     * @return true:表示获取到了锁，false表示没有获取到
     */
    public boolean getLock(final String lockId, final Duration duration) {
        final String value = UUID.randomUUID().toString();
        final Boolean success = redisTemplate.opsForValue().setIfAbsent(RedisConstEnum.LOCK_NAME_PREFIX + lockId, value, duration);
        return BooleanUtil.isTrue(success);
    }

    /**
     * 获取锁。
     * 如果获取到了返回true，没有取到返回值false
     *
     * @param lockId 锁的id，唯一值
     * @param value 存入到Redis中的数据
     * @param duration 时间
     *
     * @return true:表示获取到了锁，false表示没有获取到
     */
    public boolean getLock(final String lockId, final Object value, final Duration duration) {
        final Boolean success = redisTemplate.opsForValue().setIfAbsent(RedisConstEnum.LOCK_NAME_PREFIX + lockId, value, duration);
        return BooleanUtil.isTrue(success);
    }

    /**
     * <p>
     * 死循环    去尝试获取分布式锁   - 至少会获取一次.
     * 如果获取到了返回true，没有取到返回值false
     * 本方式会导致 死循环  非必要情况尽量不要使用   尽量使用 getLock
     * </p>
     *
     * @param lockId 锁key
     * @param waitTime 尝试时间,毫秒
     * @param leaseTime 获取到锁以后,占用时间
     * @param timeUnit 占用时间单位
     *
     * @return 是否获取成功
     */
    public boolean tryLock(final String lockId, final long waitTime, final long leaseTime, final TimeUnit timeUnit) {
        final String value = UUID.randomUUID().toString();
        final long now = System.currentTimeMillis();
        while (now <= (now + waitTime)) {
            Boolean success = redisTemplate.opsForValue().setIfAbsent(RedisConstEnum.LOCK_NAME_PREFIX + lockId, value, leaseTime, timeUnit);
            if (BooleanUtil.isTrue(success)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 释放锁.
     *
     * @param lockId 锁Id
     */
    public void release(final String lockId) {
        redisTemplate.delete(RedisConstEnum.LOCK_NAME_PREFIX + lockId);
    }

    /**
     * 释放锁，并获取redis中的值.
     *
     * @param clazz 数据的类型
     * @param lockId 锁id
     * @param <T> 数据类型
     *
     * @return T 数据的类型
     */
    public <T> T release(final String lockId, final Class<T> clazz) {
        final T cast = clazz.cast(redisTemplate.opsForValue().get(RedisConstEnum.LOCK_NAME_PREFIX + lockId));
        redisTemplate.delete(RedisConstEnum.LOCK_NAME_PREFIX + lockId);
        return cast;
    }

}
