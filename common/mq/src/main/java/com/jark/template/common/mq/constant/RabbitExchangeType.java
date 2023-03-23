package com.jark.template.common.mq.constant;

/**
 * AMQP 交换机类型。
 *
 * @author ponder
 */
public interface RabbitExchangeType {

    /**
     * Topic类型。
     * 将消息路由到 binding key 与 routing key 相匹配的 Queue 中
     */
    String TOPIC_EXCHANGE = "topic";

    /**
     * 它会把所有发送到该 Exchange 的消息路由到所有与它绑定的 Queue 中。
     */
    String FANOUT_EXCHANGE = "fanout";

    /**
     * 它会把消息路由到那些 binding key 与
     * routing key 完全匹配的 Queue 中。
     */
    String DIRECT_EXCHANGE = "direct";

    /**
     * 延迟队列。
     */
    String LAZY_EXCHANGE = "lazy";

}
