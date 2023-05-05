package com.jark.template.common.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.jark.template.common.web.config.FeignBeforeInterceptor;
import com.jark.template.common.web.config.FeignMappingDefaultConfiguration;
import com.jark.template.common.web.config.FeignResponseDecoder;
import com.jark.template.common.web.config.FeignTraceConfig;

/**
 * 配置
 */
@Configuration
@Import({
    FeignBeforeInterceptor.class,
    FeignTraceConfig.class,
    FeignMappingDefaultConfiguration.class,
    FeignResponseDecoder.class,
})
public class FeignConfiguration {
}


