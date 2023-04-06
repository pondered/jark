package com.jark.template.common.web.debounce;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import lombok.extern.slf4j.Slf4j;

/**
 * 防抖AOP。
 *
 * @author ponder
 */
@Aspect
@Slf4j
public final class DebounceAspect {

    @Pointcut("execution(public * * (..))")
    private void publicMethod() {
    }

    @Around("publicMethod() && @annotation(debounce)")
    public Object debounce(final ProceedingJoinPoint joinPoint, final Debounce debounce) {
        log.debug("接口防抖:{}", joinPoint.getSignature());


        return null;
    }
}
