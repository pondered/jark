package com.jark.template.common.redis.exception;

import com.jark.template.common.utils.exception.BaseException;

import lombok.extern.slf4j.Slf4j;

/**
 * Redis锁超时异常.
 *
 * @author ponder
 */
@Slf4j
public final class RedisTimeoutException extends BaseException {

    public RedisTimeoutException(final String message) {
        super(message);
    }
}
