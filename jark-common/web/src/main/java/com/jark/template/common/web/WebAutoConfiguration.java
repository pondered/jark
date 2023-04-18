package com.jark.template.common.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.jark.template.common.web.config.LazyExcludeConfig;
import com.jark.template.common.web.config.convert.ConvertConfiguration;
import com.jark.template.common.web.config.convert.JacksonConvertConfiguration;
import com.jark.template.common.web.config.exception.ErrorController;
import com.jark.template.common.web.config.exception.GlobalExceptionHandler;
import com.jark.template.common.web.config.swagger.Knife4jConfig;

/**
 * 配置
 */
@Configuration
@Import({
    LazyExcludeConfig.class,
    Knife4jConfig.class,
    ErrorController.class,
    GlobalExceptionHandler.class,
    ConvertConfiguration.class,
    JacksonConvertConfiguration.class
})
public class WebAutoConfiguration {
}


