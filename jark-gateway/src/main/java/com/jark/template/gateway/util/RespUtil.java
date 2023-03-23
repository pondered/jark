package com.jark.template.gateway.util;

import java.nio.charset.StandardCharsets;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;

import com.jark.template.common.utils.json.JsonUtil;
import com.jark.template.common.utils.resp.R;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author jayden
 * @date 2022/3/2916:42
 */
@Slf4j
public final class RespUtil {
    private RespUtil() {

    }

    public static Mono<Void> response(final ServerHttpResponse response, final R<Object> result) {
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getHeaders().set("Access-Control-Allow-Origin", "*");
        response.getHeaders().set("Cache-Control", "no-cache");
        DataBuffer buffer = response.bufferFactory().wrap(JsonUtil.toJSON(result).getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer))
            .doOnError(error -> DataBufferUtils.release(buffer));
    }

}
