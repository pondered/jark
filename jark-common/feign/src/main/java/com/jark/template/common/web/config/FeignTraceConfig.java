package com.jark.template.common.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;

/**
 * Feign 链路数据
 */
@Slf4j
@Configuration
public class FeignTraceConfig {

    /**
     * Feign请求 链路
     */
    @Bean
    public RequestInterceptor feignTraceInterceptor() {
        return template -> {

        };
    }

}


