package com.jark.template.common.redis.lock.fail;


import com.jark.template.common.redis.lock.LockInfo;
import com.jark.template.common.redis.lock.RedisLock;
import com.jark.template.common.redis.lock.type.Lock;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 执行业务代码时出现异常
 * 需要处理方式
 */
@Slf4j
@Getter
@NoArgsConstructor
public enum FailType implements FailTypeHandler {

    /**
     * 不做任何处理，直接抛出异常(不释放锁)
     */
    NO_OPERATION() {
        @Override
        public void handle(final Lock lock, final LockInfo lockInfo, final RedisLock redisLock, final Exception exception) throws Exception {
            log.info("{}执行业务时遇到异常,锁信息:{}", lockInfo.getLockName(), lockInfo);
            throw exception;
        }
    },


    /**
     * 释放锁并不抛出异常
     * 注意： 本方法会导致 调用方获取到的数据为 null
     * 如 a调用b方法，b使用了锁，b运行完后a的获取到的值为 null
     */
    RELEASE() {
        @Override
        public void handle(final Lock lock, final LockInfo lockInfo, final RedisLock redisLock, final Exception exception) {
            log.info("{}执行业务时遇到异常,锁信息:{}", lockInfo.getLockName(), lockInfo);
            // 释放锁
            final boolean release = lock.release();
            // 释放锁失败
            if (!release) {
                log.info("{}执行业务遇到异常后释放锁失败", lockInfo.getLockName());
                redisLock.releaseTimeoutStrategy().handle(lockInfo);
            }
        }
    },

    /**
     * 释放锁，并抛出异常
     */
    RELEASE_THROW() {
        @Override
        public void handle(final Lock lock, final LockInfo lockInfo, final RedisLock redisLock, final Exception exception) throws Exception {
            log.info("{}执行业务时遇到异常,锁信息:{}", lockInfo.getLockName(), lockInfo);
            // 释放锁
            final boolean release = lock.release();
            // 释放锁失败
            if (!release) {
                log.info("{}执行业务遇到异常后释放锁失败", lockInfo.getLockName());
                redisLock.releaseTimeoutStrategy().handle(lockInfo);
            }
            throw exception;
        }
    }
}
