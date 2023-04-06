package com.jark.template.common.web.config.interceptor;


import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.jark.template.common.utils.constant.HeaderConst;
import com.jark.template.common.utils.tools.MDCUtil;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ponder
 */
@Slf4j
public class RequestIdInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        final String from = request.getHeader(HeaderConst.FROM);
        final String requestId = StrUtil.nullToDefault(response.getHeader(HeaderConst.REQUEST_ID), request.getHeader(HeaderConst.REQUEST_ID));
        MDCUtil.setRequestId(requestId);
        response.setHeader(HeaderConst.X_SERVER_START_TIME, String.valueOf(System.currentTimeMillis()));
        response.setHeader(HeaderConst.REQUEST_ID, MDCUtil.getRequestId());
        if (StrUtil.isEmpty(from)) {
            log.info("用户请求路径为:{}", request.getRequestURL());
        } else {
            log.info("{}服务调用请求路径为:{}", from, request.getRequestURL());
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
                           final ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
        final String startTime = response.getHeader(HeaderConst.X_SERVER_START_TIME);
        if (ObjectUtil.isNotEmpty(startTime)) {
            Long executeTime = System.currentTimeMillis() - Long.parseLong(startTime);
            log.info("接口耗时:{}ms", executeTime);
        }
    }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex)
        throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
