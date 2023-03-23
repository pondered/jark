package com.jark.template.common.redis.exception;

import com.jark.template.common.utils.exception.BaseException;

/**
 * Redis锁异常类.
 *
 * @author ponder
 */
public final class RedisLockException extends BaseException {
    public RedisLockException(final Exception ex) {
        super(ex);
    }

    public RedisLockException(final String message) {
        super(message);
    }

    public RedisLockException(final String message, final Exception ex) {
        super(message, ex);
    }
}
