package com.jark.template.gateway;

import reactor.core.publisher.Mono;

import org.springframework.web.server.ServerWebExchange;

/**
 * 路由相关操作
 * @author ponder
 */
public interface RouteService {

    /**
     * 过滤黑名单操作
     */
    Mono<Void> filterBlackList(ServerWebExchange exchange);

    /**
     * 过滤限流
     */
    Mono<Void> filterRateLimit(ServerWebExchange exchange);


}


