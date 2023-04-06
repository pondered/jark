package com.jark.template.common.redis.lock.fail;

import com.jark.template.common.redis.lock.LockInfo;
import com.jark.template.common.redis.lock.RedisLock;
import com.jark.template.common.redis.lock.type.Lock;

/**
 * @author Ponder
 * @Type FailTypeHandler.java
 * @Desc
 * @date 2023/3/8 14:04
 */
public interface FailTypeHandler {


    /**
     * 具体处理业务。
     *
     * @param lock 锁
     * @param lockInfo 锁信息
     * @param redisLock 注解
     * @param exception 异常
     */
    void handle(Lock lock, LockInfo lockInfo, RedisLock redisLock, Exception exception) throws Exception;

}



