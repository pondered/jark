package com.jark.template.common.redis.lock.release;

import com.jark.template.common.redis.lock.LockInfo;

/**
 * 释放锁时超时策略.
 * @author ponder
 */
public interface ReleaseTimeoutHandler {

    void handle(LockInfo lockInfo);
}
