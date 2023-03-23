package com.jark.template.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.jark.template.gateway.constant.GatewayFilterOrderConst;
import com.jark.template.common.utils.constant.HeaderConst;
import com.jark.template.common.utils.tools.MDCUtil;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author ponder
 */
@Slf4j
@Component
public class LoggingFilter implements GlobalFilter, Ordered {


    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final GatewayFilterChain chain) {
        String info = String.format("Method:{%s} Host:{%s} Path:{%s} Query:{%s}",
            exchange.getRequest().getMethod().name(),
            exchange.getRequest().getURI().getHost(),
            exchange.getRequest().getURI().getPath(),
            exchange.getRequest().getQueryParams());
        log.info(info);

        exchange.getAttributes().put(HeaderConst.X_GATEWAY_START_TIME, System.currentTimeMillis());
        exchange.getAttributes().put(HeaderConst.REQUEST_ID, exchange.getRequest().getHeaders().getFirst(HeaderConst.REQUEST_ID));
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            final Long startTime = ObjectUtil.defaultIfNull(exchange.getAttribute(HeaderConst.X_GATEWAY_START_TIME), 0L);
            final String requestId = exchange.getAttribute(HeaderConst.REQUEST_ID);
            MDCUtil.setRequestId(requestId);
            if (ObjectUtil.isNotEmpty(startTime)) {
                Long executeTime = System.currentTimeMillis() - startTime;
                log.info("网关请求路径为:{},接口耗时:{} ms", exchange.getRequest().getURI().getRawPath(), executeTime);
            }
        }));
    }

    @Override
    public int getOrder() {
        return GatewayFilterOrderConst.LOGGING_FILTER;
    }
}
