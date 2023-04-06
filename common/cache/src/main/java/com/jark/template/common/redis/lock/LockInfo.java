package com.jark.template.common.redis.lock;

import java.util.Locale;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

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

    public static LockInfo get(final Map<String, Expression> keyCache, final RedisLock redisLock, final JoinPoint joinPoint) {
        final String objectId = IdUtil.objectId();
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final String className = signature.getDeclaringType().getSimpleName();
        final String methodName = signature.getMethod().getName().toLowerCase(Locale.CHINA);
        final Object[] args = joinPoint.getArgs();

        String key = redisLock.key();
        if (args != null && args.length != 0) {
            final String expressionKey = className + "#" + methodName + "#" + key;
            Expression expression = keyCache.get(expressionKey);
            if (expression == null) {
                expression = new SpelExpressionParser().parseExpression(key);
                keyCache.put(expressionKey, expression);
            }
            final EvaluationContext context = createEvaluationContext(signature.getParameterNames(), args);
            key = expression.getValue(context, String.class);
        }

        final String lockName = RedisConstEnum.LOCK_NAME_PREFIX
            + className + RedisConstEnum.LOCK_NAME_SEPARATOR
            + methodName + RedisConstEnum.LOCK_NAME_SEPARATOR
            + key;

        return new LockInfo(lockName, objectId, redisLock.expire(),
            redisLock.waitTime(), redisLock.lockType());
    }


    private static EvaluationContext createEvaluationContext(final String[] names, final Object[] args) {
        final EvaluationContext context = new StandardEvaluationContext();
        //设置上下文变量
        for (int i = 0; i < names.length; i++) {
            context.setVariable(names[i], args[i]);
        }
        return context;
    }

}
