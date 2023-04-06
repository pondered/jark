package com.jark.template.common.redis.lock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.expression.Expression;
import org.springframework.stereotype.Component;

import com.jark.template.common.redis.lock.type.LockTypeFactory;
import com.jark.template.common.redis.lock.type.Lock;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Redis锁AOP操作具体实现类。
 *
 * @author ponder
 */
@Slf4j
@Aspect
@Component
public final class RedisLockAspect {

    /**
     * Spel语法缓存
     */
    private final Map<String, Expression> keyCache = new ConcurrentHashMap<>(64);

    @Around("@annotation(redisLock)")
    public Object around(final ProceedingJoinPoint joinPoint, final RedisLock redisLock) throws Throwable {
        final LockInfo lockInfo = LockInfo.get(keyCache, redisLock, joinPoint);
        final Lock lock = LockTypeFactory.of(lockInfo.getLockType(), lockInfo);

        // 获取锁
        final boolean acquire = lock.acquire();
        // 获取锁失败
        if (!acquire) {
            redisLock.lockTimeoutStrategy().handle(lockInfo, lock, joinPoint);
            return null;
        }

        // 返回数据
        Object proceed = null;

        // 判断是否进入了异常，如果进入了异常则不在执行相关代码
        boolean failFlag = true;
        try {
            // 执行方法
            proceed = joinPoint.proceed();
        } catch (Exception e) {
            redisLock.failType().handle(lock, lockInfo, redisLock, e);
            failFlag = false;
        }

        if (failFlag) {
            // 释放锁
            final boolean release = lock.release();
            // 释放锁失败
            if (!release) {
                redisLock.releaseTimeoutStrategy().handle(lockInfo);
            }
        }

        return proceed;
    }
}
