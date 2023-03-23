package com.jark.template.common.redis.lock;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jark.template.common.redis.lock.fail.FailType;
import com.jark.template.common.redis.lock.type.LockType;
import com.jark.template.common.redis.lock.acquire.LockTimeoutStrategy;
import com.jark.template.common.redis.lock.release.ReleaseTimeoutStrategy;

/**
 * Redis锁注解
 * <p>
 * 没有获取到锁时返回值为null
 *
 * @author ponder
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLock {

    /**
     * 锁名称。
     */
    String key();

    /**
     * 失效时间。
     * 秒
     */
    long expire();

    /**
     * 当业务处理出现异常时锁的处理方式
     */
    FailType failType();

    /**
     * 锁类型，默认可重入锁。
     */
    LockType lockType() default LockType.Reentrant;

    /**
     * 如果没有获取到锁 单位 秒。
     * 尝试获取锁， 最多等待时间 默认不等待
     * lockTimeoutStrategy 为 KEEP_ACQUIRE 是 waitTime 需要传值
     */
    long waitTime() default Long.MIN_VALUE;

    /**
     * 加锁超时的处理策略，默认快速失败。
     * lockTimeoutStrategy 为 KEEP_ACQUIRE 是 waitTime 必填
     */
    LockTimeoutStrategy lockTimeoutStrategy() default LockTimeoutStrategy.FAIL_FAST;

    /**
     * 释放锁时已超时的处理策略。
     */
    ReleaseTimeoutStrategy releaseTimeoutStrategy() default ReleaseTimeoutStrategy.NO_OPERATION;
}
