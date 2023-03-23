package com.jark.template.common.utils.tools;

import java.util.Map;

import org.slf4j.MDC;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;

import com.jark.template.common.utils.constant.HeaderConst;

/**
 * @author ponder
 */
public final class MDCUtil {

    private MDCUtil() {
    }

    public static void init() {
        setRequestId();
    }


    public static String getRequestId() {
        return MDC.get(HeaderConst.REQUEST_ID);
    }

    public static void setRequestId(final String requestId) {
        if (StrUtil.isEmpty(requestId)) {
            MDC.put(HeaderConst.REQUEST_ID, IdUtil.objectId());
        } else {
            MDC.put(HeaderConst.REQUEST_ID, requestId);
        }
    }

    /**
     * 一个用户完整的请求流程
     */
    public static void setRequestId() {
        final String requestId = MDC.get(HeaderConst.REQUEST_ID);
        if (StrUtil.isEmpty(requestId)) {
            MDC.put(HeaderConst.REQUEST_ID, IdUtil.objectId());
        }
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
