package com.jark.template.common.logging.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 日志处理
 * @author ponder 
 */
@Slf4j
@Aspect
@Component
public class ComponentLogAop {

    @Value("${logging.service.aop:false}")
    private Boolean serviceLog;

    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void service() {
    }

    @Pointcut("@within(org.springframework.stereotype.Controller)")
    public void controller() {
    }

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void restController() {
    }

    /**
     * AOP环绕
     */
    @Around("service() || controller() || restController()")
    public Object around(final ProceedingJoinPoint joinPoint) throws Throwable {
        if (serviceLog) {
            Object object = null;
            final Object[] args = joinPoint.getArgs();
            final String className = joinPoint.getTarget().getClass().getName();
            final String methodName = joinPoint.getSignature().getName();

            try {
                object = joinPoint.proceed();
            } finally {
                log.info("请求{}#{},参数为:{},返回参数为:{}", className, methodName, args, object);
            }
            return object;
        } else {
            return joinPoint.proceed();
        }
    }

}


