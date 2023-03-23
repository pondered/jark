package com.jark.template.app.config;

import org.springframework.boot.LazyInitializationExcludeFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jark.template.app.config.resp.ReturnValueConfig;

/**
 * 懒加载排除配置
 */
@Configuration
public class LazyExcludeConfig {

    /**
     * 懒加载排除
     *
     * @return 懒加载过滤器
     */
    @Bean
    public LazyInitializationExcludeFilter integrationLazyInitExcludeFilter() {
        return LazyInitializationExcludeFilter.forBeanTypes(ReturnValueConfig.class);
    }

}


