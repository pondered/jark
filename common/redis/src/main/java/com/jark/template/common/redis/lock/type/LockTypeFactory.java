package com.jark.template.common.redis.lock.type;

import org.springframework.data.redis.core.RedisTemplate;

import com.jark.template.common.redis.lock.LockInfo;
import com.jark.template.common.utils.tools.SpringUtil;

import cn.hutool.core.lang.TypeReference;
import lombok.extern.slf4j.Slf4j;

/**
 * 锁类型工厂类.
 *
 * @author ponder
 */
@Slf4j
public final class LockTypeFactory {
    private static final LockTypeFactory LOCK_TYPE_FACTORY = new LockTypeFactory();

    private LockTypeFactory() {
    }

    public static Lock of(final LockType lockType, final LockInfo lockInfo) {
        return LOCK_TYPE_FACTORY.getLock(lockType, lockInfo);
    }

    public Lock getLock(final LockType lockType, final LockInfo lockInfo) {
        Lock lock;
        switch (lockType) {
            case Fair:
                lock = new FairLock(LazyHolder.INSTANCE, lockInfo);
            case Read:
                lock = new ReadLock(LazyHolder.INSTANCE, lockInfo);
            case Write:
                lock = new WriteLock(LazyHolder.INSTANCE, lockInfo);
            default:
                lock = new ReentrantLock(LazyHolder.INSTANCE, lockInfo);
        }
        return lock;
    }

    /**
     * 懒加载模块。
     */
    private static class LazyHolder {
        public static final RedisTemplate<String, Object> INSTANCE = SpringUtil.getBean(new TypeReference<RedisTemplate<String, Object>>() {
        });
    }
}
