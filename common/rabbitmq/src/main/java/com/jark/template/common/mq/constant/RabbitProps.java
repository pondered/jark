package com.jark.template.common.mq.constant;


import java.io.Serializable;
import java.util.List;

import lombok.Getter;

/**
 * 这个是MQ初始化配置, MQ添加队列或exchange时, 需要在这里添加对应的信息.
 *
 * @author ponder
 */
@Getter
public enum RabbitProps implements Serializable {

    /**
     * 默认，无实际意义，仅供参考。
     */
    DEFAULT(ExchangeConst.DEFAULT, QueueEnum.DEFAULT.getName(), null, false, RabbitExchangeType.DIRECT_EXCHANGE),

    /**
     * 测试使用，仅判断是否自动生成。
     */
    TEST(ExchangeConst.TEST, QueueEnum.TEST.getName(), null, false, RabbitExchangeType.DIRECT_EXCHANGE);

    private String exchangeName;

    private String queueName;

    private List<String> routingKeyName;

    private Boolean delayType;

    private String type;

    /**
     * 枚举.
     *
     * @param exchangeName   交换机类型
     * @param queueName      队列名称
     * @param routingKeyName routingKey名称
     * @param delayType      是否延迟队列
     * @param type           类型
     */
    RabbitProps(final String exchangeName, final String queueName, final List<String> routingKeyName, final Boolean delayType, final String type) {
        this.exchangeName = exchangeName;
        this.queueName = queueName;
        this.routingKeyName = routingKeyName;
        this.delayType = delayType;
        this.type = type;
    }

}

