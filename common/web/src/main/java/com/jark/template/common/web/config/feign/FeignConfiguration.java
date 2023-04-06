package com.jark.template.common.web.config.feign;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import cn.hutool.core.date.DatePattern;
import feign.Contract;
import lombok.RequiredArgsConstructor;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

/**
 * @author psqing
 */
@Configuration
@RequiredArgsConstructor
public class FeignConfiguration {

    private final ConversionService conversionService;

    /**
     * 用feign.Contract.Default替换SpringMvcContract契约
     */
    @Bean
    public Contract feignContract() {
        return new SpringMvcContract(Collections.emptyList(), conversionService);
    }

    /**
     * Feign 参数 format 处理数据
     */
    @Bean
    public FeignFormatterRegistrar feignFormatterRegistrar() {
        return (formatterRegistry) -> {
            DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
            registrar.setUseIsoFormat(false);
            registrar.setDateFormatter(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
            registrar.setTimeFormatter(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN));
            registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN));
            registrar.registerFormatters(formatterRegistry);
        };
    }

    /**
     * Feign 使用 Okhttp bean注册
     */
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .connectionPool(new ConnectionPool(10, 5L, TimeUnit.MINUTES))
            .build();
    }

    /**
     * 使用OkHTTP
     */
    @Bean
    @ConditionalOnMissingBean(name = "restTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate(this.okHttp3ClientHttpRequestFactory());
    }

    /**
     * 使用OkHTTP
     */
    @Bean
    public OkHttp3ClientHttpRequestFactory okHttp3ClientHttpRequestFactory() {
        return new OkHttp3ClientHttpRequestFactory(this.okHttpClient());
    }
}
