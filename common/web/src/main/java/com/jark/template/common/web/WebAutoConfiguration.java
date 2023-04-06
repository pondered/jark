package com.jark.template.common.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.jark.template.common.web.config.LazyExcludeConfig;
import com.jark.template.common.web.config.convert.ConvertConfiguration;
import com.jark.template.common.web.config.convert.JacksonConvertConfiguration;
import com.jark.template.common.web.config.exception.ErrorController;
import com.jark.template.common.web.config.exception.GlobalExceptionHandler;
import com.jark.template.common.web.config.feign.FeignBeforeInterceptor;
import com.jark.template.common.web.config.feign.FeignConfiguration;
import com.jark.template.common.web.config.feign.FeignMappingDefaultConfiguration;
import com.jark.template.common.web.config.feign.FeignResponseDecoder;
import com.jark.template.common.web.config.interceptor.InterceptorConfig;
import com.jark.template.common.web.config.swagger.Knife4jConfig;

/**
 * 配置
 */
@Configuration
@Import({
    InterceptorConfig.class,
    FeignBeforeInterceptor.class,
    FeignConfiguration.class,
    FeignMappingDefaultConfiguration.class,
    FeignResponseDecoder.class,
    LazyExcludeConfig.class,
    Knife4jConfig.class,
    ErrorController.class,
    GlobalExceptionHandler.class,
    ConvertConfiguration.class,
    JacksonConvertConfiguration.class
})
public class WebAutoConfiguration {
}


