package com.jark.template.common.redis.lock.release;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.jark.template.common.redis.exception.RedisTimeoutException;
import com.jark.template.common.redis.lock.LockInfo;

/**
 * 释放锁超时策略.
 */
@Slf4j
@Getter
@NoArgsConstructor
public enum ReleaseTimeoutStrategy implements ReleaseTimeoutHandler {

    /**
     * 默认策略，不做任何处理.
     */
    NO_OPERATION() {
        @Override
        public void handle(final LockInfo lockInfo) {
            log.info("Redis锁:{}释放超时,锁信息:{}", lockInfo.getLockName(), lockInfo);
        }
    },

    /**
     * 快速失败.
     */
    FAIL_FAST() {
        @Override
        public void handle(final LockInfo lockInfo) {
            final String message = StrUtil.format("Redis锁:{}释放超时,锁信息:{}", lockInfo.getLockName(), lockInfo);
            throw new RedisTimeoutException(message);
        }
    }

}
