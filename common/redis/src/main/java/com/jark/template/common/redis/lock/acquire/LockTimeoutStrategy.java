package com.jark.template.common.redis.lock.acquire;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.JoinPoint;

import com.jark.template.common.redis.exception.RedisLockException;
import com.jark.template.common.redis.exception.RedisTimeoutException;
import com.jark.template.common.redis.lock.LockInfo;
import com.jark.template.common.redis.lock.type.Lock;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 锁超时策略.
 *
 * @author ponder
 */
@Slf4j
@Getter
@NoArgsConstructor
public enum LockTimeoutStrategy implements LockTimeoutHandler {
    /**
     * 不做任何处理.
     */
    NO_OPERATION() {
        @Override
        public void handle(final LockInfo lockInfo, final Lock lock, final JoinPoint joinPoint) {
            log.info("{}没有获取到锁,锁信息:{}", lockInfo.getLockName(), lockInfo);
        }
    },

    /**
     * 快速失败.
     */
    FAIL_FAST() {
        @Override
        public void handle(final LockInfo lockInfo, final Lock lock, final JoinPoint joinPoint) {
            final String message = StrUtil.format("{}没有获取到锁,锁信息:{}", lockInfo.getLockName(), lockInfo);
            throw new RedisTimeoutException(message);
        }
    },


    /**
     * 一直阻塞，直到获得锁，在太多的尝试后，仍会报错.
     */
    KEEP_ACQUIRE() {
        @Override
        public void handle(final LockInfo lockInfo, final Lock lock, final JoinPoint joinPoint) {
            LocalDateTime expireTime = LocalDateTime.now().plusSeconds(lockInfo.getWaitTime());
            while (!lock.acquire()) {
                if (expireTime.isAfter(LocalDateTime.now())) {
                    final String message = StrUtil.format("Redis锁:{}获取超时,锁信息:{}", lockInfo.getLockName(), lockInfo);
                    throw new RedisTimeoutException(message);
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RedisLockException(StrUtil.format("获取锁异常:{}", lockInfo), e);
                }
            }
        }
    }

}
