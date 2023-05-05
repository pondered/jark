package com.jark.template.gateway.swagger;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


import cn.hutool.core.util.StrUtil;

import com.github.xiaoymin.knife4j.spring.gateway.Knife4jGatewayProperties;


/**
 * @author ponder
 */
@Configuration
@Profile({"dev", "test", "local"})
public class Knife4jConfiguration implements WebFluxConfigurer {
    /**
     * 分组url
     */
    public static final String GATEWAY_SWAGGER_GROUP_URL = "/swagger-resources";

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * knife4j 路由配置
     *
     * @param routeDefinitionLocator 路由信息
     *
     * @return Knife4j路由配置信息
     */
    @Bean
    public RouterFunction<ServerResponse> gatewaySwaggerRoute(final RouteDefinitionLocator routeDefinitionLocator) {
        final List<Knife4jGatewayProperties.Router> routes = new ArrayList<>();

        routeDefinitionLocator.getRouteDefinitions().subscribe(routeDefinition -> {
            final Optional<String> location =
                routeDefinition.getPredicates().stream().filter(predicateDefinition -> "Path".equalsIgnoreCase(predicateDefinition.getName()))
                    .map(predicateDefinition -> {
                        final String pattern = StrUtil.nullToDefault(predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0"),
                            predicateDefinition.getArgs().get("pattern"));
                        return pattern.replace("**", "v2/api-docs?group=" + routeDefinition.getUri().getHost());
                    }).findFirst();
            final Knife4jGatewayProperties.Router knife4jGatewayRoute = new Knife4jGatewayProperties.Router();
            knife4jGatewayRoute.setOrder(routeDefinition.getOrder());
            knife4jGatewayRoute.setServiceName(routeDefinition.getId());
            knife4jGatewayRoute.setUrl(location.orElse(""));
            knife4jGatewayRoute.setName(routeDefinition.getId());
            routes.add(knife4jGatewayRoute);
        });
        return RouterFunctions.route().GET(GATEWAY_SWAGGER_GROUP_URL,
            request -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(build(routes))).build();
    }


    private List<Map<String, String>> build(final List<Knife4jGatewayProperties.Router> routes) {
        List<Map<String, String>> dataMaps = new ArrayList<>();
        if (routes != null && routes.size() > 0) {
            long count = routes.stream().filter(r -> r.getOrder() > 0).count();
            if (count > 0) {
                // 排序
                routes.sort(Comparator.comparing(Knife4jGatewayProperties.Router::getOrder));
            }
            for (Knife4jGatewayProperties.Router route : routes) {
                Map<String, String> routeMap = new LinkedHashMap<>();
                routeMap.put("name", route.getName());
                routeMap.put("url", route.getUrl());
                String source = route.getName() + route.getUrl() + route.getServiceName();
                String id = Base64.getEncoder().encodeToString(source.getBytes(StandardCharsets.UTF_8));
                routeMap.put("id", id);
                dataMaps.add(routeMap);
            }
        }
        return dataMaps;
    }
}
