package com.jark.template.common.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.jark.template.common.web.config.feign.FeignBeforeInterceptor;
import com.jark.template.common.web.config.feign.FeignMappingDefaultConfiguration;
import com.jark.template.common.web.config.feign.FeignResponseDecoder;

/**
 * 配置
 */
@Configuration
@Import({
    FeignBeforeInterceptor.class,
    FeignConfiguration.class,
    FeignMappingDefaultConfiguration.class,
    FeignResponseDecoder.class,
})
public class FeignConfiguration {
}


