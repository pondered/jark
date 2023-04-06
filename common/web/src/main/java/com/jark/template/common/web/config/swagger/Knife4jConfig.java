package com.jark.template.common.web.config.swagger;

import java.lang.reflect.Type;

import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.parsers.ReturnTypeParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;

import com.jark.template.common.utils.resp.R;

import cn.hutool.core.lang.ParameterizedTypeImpl;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * @author ponder
 */
@Configuration
public class Knife4jConfig {

    /**
     * OpenAPI配置
     *
     * @return 配置
     */
    @Bean
    public GroupedOpenApi openApi() {
        final String[] paths = {"/**"};
        final String[] packagedToMatch = {"com.jark.template.app.controller"};
        return GroupedOpenApi.builder()
            .group("jark")
            .pathsToMatch(paths)
            .packagesToScan(packagedToMatch).build();
    }

    /**
     * 自定义
     *
     * @return 自定义信息
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("jark API")
                .version("1.0")
                .description("JARK"));
    }

    /**
     * 自定义相应参数配置
     *
     * @return 配置转换器
     */
    @Bean
    public ReturnTypeParser customReturnTypeParser() {
        return new ReturnTypeParser() {
            @Override
            public Type getReturnType(final MethodParameter methodParameter) {
                Type returnType = ReturnTypeParser.super.getReturnType(methodParameter);
                return new ParameterizedTypeImpl(new Type[] {returnType}, R.class, R.class);
            }
        };
    }
}


