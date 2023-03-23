package com.jark.template.gateway.constant;

import org.springframework.core.Ordered;

/**
 * @author Ponder
 * @Type GatewayFilterOrderConst.java
 * @Desc 网关过滤器顺序常量
 * @date 2023/3/2 10:07
 */
public interface GatewayFilterOrderConst {

    /**
     * 优先级高，从请求头移除特殊token
     */
    int IGNORE_URL_FILTER = 0;

    /**
     * 日志打印
     */
    int LOGGING_FILTER = Ordered.LOWEST_PRECEDENCE;

    /**
     * MDC添加
     */
    int MDC_FILTER = IGNORE_URL_FILTER + 1;

    /**
     * 用户Token处理
     */
    int USER_FILTER = Ordered.LOWEST_PRECEDENCE;

    /**
     * 异常处理过滤器
     */
    int EXCEPTION_FILTER = -2;
}



