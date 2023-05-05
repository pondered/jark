package com.jark.template.gateway.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.stereotype.Component;

import com.jark.template.common.redis.utils.RedisUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Redis路由
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisRouteDefinitionRepository implements RouteDefinitionRepository {

    private static final Optional<String> ROUTES = Optional.of("gateway:route");

    private final RedisUtil redisUtil;


    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        final List<RouteDefinition> routeDefinitions = redisUtil.hmget(ROUTES).map(route -> (RouteDefinition) route).stream().toList();
        return Flux.fromIterable(routeDefinitions);
    }

    @Override
    public Mono<Void> save(final Mono<RouteDefinition> routeDefinition) {
        return routeDefinition.flatMap(route -> {
            redisUtil.hmset(ROUTES, route.getId(), route);
            return Mono.empty();
        });
    }

    @Override
    public Mono<Void> delete(final Mono<String> routeId) {
        return routeId.flatMap(id -> {
            redisUtil.hdel(ROUTES, id);
            return Mono.empty();
        });
    }
}


