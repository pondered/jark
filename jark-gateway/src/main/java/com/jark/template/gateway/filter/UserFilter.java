package com.jark.template.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.jark.template.gateway.constant.GatewayFilterOrderConst;
import com.jark.template.gateway.constant.GatewayMessageEnum;
import com.jark.template.gateway.util.RespUtil;
import com.jark.template.common.utils.constant.HeaderConst;
import com.jark.template.common.utils.resp.R;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author ponder
 * 用户token处理过滤器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        final ServerHttpResponse response = exchange.getResponse();
        final String ignore = request.getHeaders().getFirst(HeaderConst.IGNORE_HEADER);
        // 如果是白名单不需要登录页面，直接过滤
        if (StrUtil.isNotEmpty(ignore)) {
            return chain.filter(exchange);
        }

        //        final URI uri = request.getURI();
        //        HttpMethod method = request.getMethod();
        //        String token = request.getHeaders().getFirst(HeaderConst.TOKEN);
        // 管理员认证
        //        Object adminTokenO = redisUtil.get(TokenUserConst.ADMIN_TOKEN + token);
        //        if (ObjectUtil.isNotEmpty(adminTokenO)) {
        //            JSONObject jsonObject = new JSONObject(adminTokenO);
        //            Long adminUserId = jsonObject.getLong(HeaderConst.ID);
        //            redisUtil.expire(TokenUserConst.ADMIN_TOKEN + token, TIMEOUT);
        //            request = request.mutate()
        //                .headers(headers -> headers.add(HeaderConst.IGNORE_HEADER, HeaderConst.FLAG))
        //                .headers(headers -> headers.add(HeaderConst.ADMIN_USER_ID, String.valueOf(adminUserId))).build();
        //            return chain.filter(exchange.mutate().request(request).build());
        //        }
        return RespUtil.response(response, R.fail(GatewayMessageEnum.UN_LOGIN));
    }

    @Override
    public int getOrder() {
        return GatewayFilterOrderConst.USER_FILTER;
    }
}
