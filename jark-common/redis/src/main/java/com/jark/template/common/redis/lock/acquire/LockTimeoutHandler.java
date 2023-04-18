package com.jark.template.common.redis.lock.acquire;


import org.aspectj.lang.JoinPoint;

import com.jark.template.common.redis.lock.LockInfo;
import com.jark.template.common.redis.lock.type.Lock;

/**
 * 锁超时后相关操作.
 *
 * @author ponder
 */
public interface LockTimeoutHandler {

    /**
     * 具体处理业务。
     *
     * @param lockInfo 锁信息
     * @param lock 释放锁还是获取锁
     * @param joinPoint aop信息
     */
    void handle(LockInfo lockInfo, Lock lock, JoinPoint joinPoint);

}
