package com.jark.template.common.utils.constant;

/**
 * 全局 请求头常量
 */
public interface HeaderConst {
    /**
     * 网关请求时间统计
     */
    String X_GATEWAY_START_TIME = "x_gateway_start_time";

    /**
     * 用户请求的path
     */
    String REAL_REQUEST_PATH_HEADER = "x_gateway_path";

    /**
     * 请求头标志,如果有这个 header 表示 过滤
     */
    String IGNORE_HEADER = "x_gateway_ignore";

    /**
     * 请求数据来源
     */
    String FROM = "from";

    /**
     * 用户请求 Token
     */
    String TOKEN = "token";

    /**
     * 用户请求Id
     */
    String USER_ID = "userId";

    /**
     * 请求来源Id
     */
    String REQUEST_ID = "requestId";

    String TRACE_ID = "traceId";

    String SPAN_ID = "spanId";

    /**
     * 请求来源时长
     */
    String X_SERVER_START_TIME = "x_server_start_time";


    /**
     * 请求头异常
     */
    String EXCEPTION = "x_server_exception";
}


