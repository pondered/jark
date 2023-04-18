package com.jark.template.common.web.processor;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.HEAD;
import static org.springframework.web.bind.annotation.RequestMethod.OPTIONS;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static org.springframework.web.bind.annotation.RequestMethod.TRACE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.jark.template.common.utils.annotation.Permission;
import com.jark.template.common.utils.json.JsonUtil;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ponder
 */
@Profile({"dev"})
@Component
@Slf4j
public class PermissionProcessor implements ApplicationListener<ApplicationReadyEvent> {
    private static final int DEFAULT_CAPACITY = 1000;

    private static final Map<Long, String> SQL_MAP = new HashMap<>(DEFAULT_CAPACITY);

    private static final List<String> CHECK_URLS = new ArrayList<>(DEFAULT_CAPACITY);

    private static final List<String> CHECK_CODES = new ArrayList<>(DEFAULT_CAPACITY);

    private final String sql =
        "INSERT INTO `permission` (`id`, `service_name`, `class_method`, `class_name`, `http_method`, `login`, `hidden`, `tenant_id`,"
            + " `url`, `resource`, `name`, `level`, `sort`, `icon`, `code`, `type`, `description`, `deleted`) VALUES "
            + "('{id}','{serviceName}','{classMethod}','{className}','{httpMethod}','{login}', '{hidden}','{tenantId}',"
            + "'{url}', '{resource}', '{name}','{level}','{sort}','{icon}','{code}', '{type}','{description}','{deleted}');\n";

    @Value("${spring.application.name}")
    private String serviceName;

    /**
     *
     */
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        final ApplicationContext context = event.getApplicationContext();
        final RequestMappingHandlerMapping mapping = context.getBean(RequestMappingHandlerMapping.class);
        final Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();

        List<String> finalSqls = new ArrayList<>(DEFAULT_CAPACITY);
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo key = entry.getKey();
            HandlerMethod value = entry.getValue();
            final String className = value.getBeanType().getName();
            final String classMethod = value.getMethod().getName();

            final Permission methodAnnotation = value.getMethodAnnotation(Permission.class);
            final int login = methodAnnotation.login() ? 0 : 1;
            final int deleted = methodAnnotation.deleted();
            final int sort = methodAnnotation.sort();
            final int hidden = methodAnnotation.hidden();
            final String[] codes = methodAnnotation.code();
            final String desc = methodAnnotation.desc();

            if (ObjectUtil.isEmpty(methodAnnotation)) {
                log.error("方法: {}#{} 没有添加 @Permission 注解", className, classMethod);
//                System.exit(0);
            }

            if (ArrayUtil.isEmpty(codes)) {
                log.error("方法: {}#{} 编码不能为空", className, classMethod);
//                System.exit(0);
            }

            final Set<String> urls = key.getPatternsCondition().getPatterns();
            Set<RequestMethod> requestMethods = key.getMethodsCondition().getMethods();

            requestMethods = (Set) CollUtil.defaultIfEmpty(requestMethods, CollUtil.toList(GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE));
            List<String> sqls = genSql(serviceName, classMethod, className, requestMethods, codes, hidden, deleted, sort, login, urls, desc);
            finalSqls.addAll(sqls);
        }
        log.info("\n##########################################################################################################\n");
        log.info("\n 初始化SQL为: \n {} \n", finalSqls);
        log.info("\n###########################################################################################################\n");
    }

    private List<String> genSql(final String serverName, final String methodName, final String className, final Collection<RequestMethod> httpMethod,
                                final String[] codes, final Integer hidden, final Integer delete, final Integer sort, final Integer login,
                                final Set<String> urls, final String name) {
        List<String> sqls = new ArrayList<>(DEFAULT_CAPACITY);
        final String urlStr = serviceName.replace("zkys-", "/").replace("-service", StrUtil.EMPTY);
        for (String url : urls) {
            for (RequestMethod requestMethod : httpMethod) {
                String s = url + requestMethod.name();
                if (CHECK_URLS.contains(url)) {
                    throw new RuntimeException(StrUtil.format("url:{}存在重复数据", s));
                }
                CHECK_URLS.add(s);
            }
            url = urlStr + url;
            for (String code : codes) {
                if (CHECK_CODES.contains(code)) {
                    throw new RuntimeException(StrUtil.format("code:{}存在重复数据", code));
                }
                CHECK_CODES.add(code);
                Long id = Math.floorMod(Long.valueOf("" + Math.abs(serverName.hashCode()) + Math.abs(String.valueOf(
                    Math.abs(className.hashCode()) + Math.abs(methodName.hashCode()) + Math.abs(code.hashCode()) + Math.abs(url.hashCode())
                        + Math.abs(httpMethod.hashCode()) + Math.abs(name.hashCode())).hashCode())), NumberUtil.pow(10, 19).longValue());
                if (SQL_MAP.containsKey(id)) {
                    throw new RuntimeException(StrUtil.format("存在Id一致的SQL:{}", SQL_MAP.get(id)));
                }
                PermissionInfo info =
                    new PermissionInfo(id, serviceName, className, methodName, JsonUtil.toJSON(httpMethod), login, hidden, url, code, name, 0, code,
                        name, delete);
                String finalSql = StrUtil.format(sql, info.toMap());
                SQL_MAP.put(id, finalSql);
                sqls.add(finalSql);
            }
        }
        return sqls;
    }

    /**
     * 权限信息.
     *
     * @author ponder
     */
    @Data
    private static class PermissionInfo {
        private Long id;

        private String serviceName;

        private String className;

        private String classMethod;

        private String httpMethod;

        private Integer login;

        private Integer hidden;

        private Long tenantId = 0L;

        private String url;

        private String resource;

        private String name;

        private Integer level = 0;

        private Integer sort = 0;

        private String icon = "";

        private String code;

        private Integer type = 1;

        private String description;

        private Integer deleted;

        private String createTime = "NOW()";

        private String updateTime = "NOW()";

        public PermissionInfo() {
        }

        public PermissionInfo(final Long id, final String serviceName, final String className, final String classMethod, final String httpMethod,
                              final Integer login, final Integer hidden, final String url, final String resource, final String name,
                              final Integer sort, final String code, final String description, final Integer deleted) {
            this.id = id;
            this.serviceName = serviceName;
            this.className = className;
            this.httpMethod = httpMethod;
            this.classMethod = classMethod;
            this.login = login;
            this.url = url;
            this.hidden = hidden;
            this.resource = resource;
            this.name = name;
            this.sort = sort;
            this.code = code;
            this.description = description;
            this.deleted = deleted;
        }

        public Map<String, Object> toMap() {
            return BeanUtil.beanToMap(this);
        }
    }
}
