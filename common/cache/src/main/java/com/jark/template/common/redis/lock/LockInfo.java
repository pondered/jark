package com.jark.template.common.redis.lock;

import java.util.Locale;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import com.jark.template.common.redis.constants.RedisConstEnum;
import com.jark.template.common.redis.lock.type.LockType;

import cn.hutool.core.util.IdUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 锁相关信息.
 *
 * @author ponder
 */
@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class LockInfo {

    private String lockName;

    private String value;

    private Long expire;

    private Long waitTime;

    private LockType lockType;

    public static LockInfo get(final RedisLock redisLock, final JoinPoint joinPoint) {
        final String objectId = IdUtil.objectId();
        final String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        final String methodName = ((MethodSignature) joinPoint.getSignature()).getMethod().getName().toLowerCase(Locale.CHINA);

        final String lockName = RedisConstEnum.LOCK_NAME_PREFIX + "" + RedisConstEnum.LOCK_NAME_SEPARATOR
            + className + RedisConstEnum.LOCK_NAME_SEPARATOR
            + methodName + RedisConstEnum.LOCK_NAME_SEPARATOR
            + redisLock.key();

        return new LockInfo(lockName, objectId, redisLock.expire(),
            redisLock.waitTime(), redisLock.lockType());
    }
}
