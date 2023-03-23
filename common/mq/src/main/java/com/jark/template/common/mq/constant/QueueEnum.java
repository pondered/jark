package com.jark.template.common.mq.constant;

import lombok.Getter;

/**
 * 发送消息枚举.
 *
 * @author ponder
 */
@Getter
public enum QueueEnum {
    /**
     * AMQP队列。
     * 默认队列，无实际意义，仅供参考
     */
    DEFAULT(QueueConst.DEFAULT),


    /**
     * 测试队列。
     */
    TEST(QueueConst.TEST);

    private String name;

    QueueEnum(final String name) {
        this.name = name;
    }
}
