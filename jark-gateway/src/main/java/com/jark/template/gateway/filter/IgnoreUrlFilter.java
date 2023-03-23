package com.jark.template.gateway.filter;

import static com.jark.template.common.utils.constant.HeaderConst.FROM;
import static com.jark.template.common.utils.constant.HeaderConst.IGNORE_HEADER;
import static com.jark.template.common.utils.constant.HeaderConst.REAL_REQUEST_PATH_HEADER;
import static com.jark.template.common.utils.constant.HeaderConst.TOKEN;
import static com.jark.template.common.utils.constant.HeaderConst.USER_ID;
import static com.jark.template.common.utils.constant.HeaderConst.X_GATEWAY_START_TIME;
import static com.jark.template.common.utils.constant.HeaderConst.X_SERVER_START_TIME;

import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.jark.template.gateway.constant.GatewayFilterOrderConst;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author ponder
 * 优先级最高：IgnoreUrlFilter -> MdcFilter
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class IgnoreUrlFilter implements WebFilter, Ordered {

    @Override
    public int getOrder() {
        return GatewayFilterOrderConst.IGNORE_URL_FILTER;
    }

    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final WebFilterChain chain) {
        final ServerHttpRequest exchangeRequest = exchange.getRequest();
        ServerHttpRequest request = exchangeRequest.mutate()
            .headers(headers -> headers.remove(FROM))
            .headers(headers -> headers.remove(IGNORE_HEADER))
            .headers(headers -> headers.remove(REAL_REQUEST_PATH_HEADER))
            .headers(headers -> headers.remove(X_GATEWAY_START_TIME))
            .headers(headers -> headers.remove(X_SERVER_START_TIME))
            .headers(headers -> headers.remove(USER_ID))
            .headers(headers -> headers.remove(TOKEN))
            .headers(headers -> headers.add(REAL_REQUEST_PATH_HEADER, exchangeRequest.getURI().getPath()))
            .build();
        return chain.filter(exchange.mutate().request(request).build());
    }
}
