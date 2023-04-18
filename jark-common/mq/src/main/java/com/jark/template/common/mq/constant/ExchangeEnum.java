package com.jark.template.common.mq.constant;

import lombok.Getter;

/**
 * Exchange常量, 当使用不同的Exchange时,需要在此添加.
 *
 * @author ponder
 */
@Getter
public enum ExchangeEnum {
    /**
     * 默认交换机。
     */
    DEFAULT(ExchangeConst.DEFAULT),

    /**
     * 测试交换机。
     */
    TEST(ExchangeConst.TEST);

    private final String name;

    ExchangeEnum(final String name) {
        this.name = name;
    }

}
