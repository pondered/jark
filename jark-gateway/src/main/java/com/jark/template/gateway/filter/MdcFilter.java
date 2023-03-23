package com.jark.template.gateway.filter;

import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.jark.template.gateway.constant.GatewayFilterOrderConst;
import com.jark.template.common.utils.constant.HeaderConst;
import com.jark.template.common.utils.tools.MDCUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author ponder
 * 日志链路打印
 * WebFilter -> GlobalFilter
 * 优先级最高：IgnoreUrlFilter -> MdcFilter
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MdcFilter implements WebFilter, Ordered {
    @Override
    public int getOrder() {
        return GatewayFilterOrderConst.MDC_FILTER;
    }

    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final WebFilterChain chain) {
        final ServerHttpRequest request = exchange.getRequest();
        MDCUtil.setRequestId(null);
        log.debug("用户请求路径为:{},header:{}", request.getURI(), request.getHeaders());
        final ServerHttpRequest buildRequest = request.mutate()
            .headers(header -> header.add(HeaderConst.REQUEST_ID, MDCUtil.getRequestId())).build();
        return chain.filter(exchange.mutate().request(buildRequest).build());
    }
}
