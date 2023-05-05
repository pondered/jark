package com.jark.template.common.utils.tools;

import static com.jark.template.common.utils.constant.HeaderConst.REQUEST_ID;
import static com.jark.template.common.utils.constant.HeaderConst.SPAN_ID;
import static com.jark.template.common.utils.constant.HeaderConst.TRACE_ID;

import java.util.Map;

import org.slf4j.MDC;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @author ponder
 */
public final class MDCUtil {

    private MDCUtil() {
    }

    public static void addTrace() {
        MDC.put(REQUEST_ID, IdUtil.objectId());
        MDC.put(TRACE_ID, IdUtil.objectId());
        MDC.put(SPAN_ID, IdUtil.objectId());
    }

    public static String getRequestId() {
        return MDC.get(REQUEST_ID);
    }

    public static void setRequestId(final String requestId) {
        if (StrUtil.isEmpty(requestId)) {
            MDC.put(REQUEST_ID, IdUtil.objectId());
        } else {
            MDC.put(REQUEST_ID, requestId);
        }
    }

    public static String nextSpanId() {
        return StrUtil.format("{}.{}", MDC.get(SPAN_ID), IdUtil.objectId());
    }

    public static Map<String, String> getCopyOfContextMap() {
        return MDC.getCopyOfContextMap();
    }

    public static void setContextMap(final Map<String, String> contextMap) {
        MDC.setContextMap(contextMap);
    }

    public static void clear() {
        MDC.clear();
    }
}
