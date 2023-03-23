package com.jark.template.gateway.config;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ponder
 */
@Configuration
public class ResourceWebPropertiesConfig {

    /**
     * 资源处理起
     *
     * @return 资源处理
     */
    @Bean
    public WebProperties.Resources resources() {
        return new WebProperties.Resources();
    }

}
