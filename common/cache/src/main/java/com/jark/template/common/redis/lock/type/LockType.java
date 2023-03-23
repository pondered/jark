package com.jark.template.common.redis.lock.type;

/**
 * 锁类型.
 *
 * @author ponder
 */
public enum LockType {

    /**
     * 可重入锁.
     */
    Reentrant,
    /**
     * 公平锁.
     */
    Fair,
    /**
     * 读锁.
     */
    Read,
    /**
     * 写锁.
     */
    Write;

    LockType() {
    }

}
