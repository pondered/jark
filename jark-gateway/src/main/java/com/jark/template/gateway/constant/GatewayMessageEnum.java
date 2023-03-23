package com.jark.template.gateway.constant;

import com.jark.template.common.utils.assertion.BizAssertMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 抖音服务断言提示语句枚举类
 */
@Getter
@AllArgsConstructor
public enum GatewayMessageEnum implements BizAssertMessage {

    /**
     * 用户未登录
     */
    UN_LOGIN("登录失效", 10401);

    /**
     * 断言返回的消息
     */
    private final String desc;

    /**
     * 状态码
     */
    private final int code;

    @Override
    public String getName() {
        return null;
    }

    @Override
    public int getCode() {
        return 0;
    }
}
