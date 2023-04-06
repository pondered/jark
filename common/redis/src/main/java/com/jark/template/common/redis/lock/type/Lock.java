package com.jark.template.common.redis.lock.type;

/**
 * 锁操作.
 *
 * @author ponder
 */
public interface Lock {

    /**
     * 获取锁.
     *
     * @return true：获取成功，false：获取失败
     */
    boolean acquire();

    /**
     * 释放锁.
     *
     * @return true：释放成功，false：释放失败
     */
    boolean release();
}
