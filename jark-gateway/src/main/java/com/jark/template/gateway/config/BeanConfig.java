package com.jark.template.gateway.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ponder
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BeanConfig {

    private final LoadBalancedExchangeFilterFunction loadBalancedExchangeFilterFunction;

    /**
     * Webclient 配置负载均衡
     *
     * @param builder webBuilder
     *
     * @return 负载均衡的配置
     */
    @Bean
    @LoadBalanced
    public WebClient webClient(final WebClient.Builder builder) {
        return builder.filter(loadBalancedExchangeFilterFunction).build();
    }

}
