package com.jark.template.common.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jark.template.common.utils.constant.HeaderConst;
import com.jark.template.common.utils.tools.MDCUtil;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Feign调用的时候添加请求头from
 * 所有的feign请求都会带有 feignForm 请求头
 *
 * @author ponder
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FeignBeforeInterceptor implements RequestInterceptor {
    @Value("${spring.application.name}")
    private String appName;


    @Override
    public void apply(RequestTemplate template) {
        log.info("调用其他服务为:{}, 路径为:{}", template.feignTarget().url(), template.path());
        template.header(HeaderConst.FROM, appName);
        template.header(HeaderConst.REQUEST_ID, MDCUtil.getRequestId());
    }
}
