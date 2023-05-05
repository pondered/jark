package com.jark.template.common.mq.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.jark.template.common.mq.constant.RabbitExchangeType;
import com.jark.template.common.mq.constant.RabbitProps;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * AMQP 初始化默认数据.
 *
 * @author ponder
 */
@Slf4j
@Component
@RequiredArgsConstructor
public final class RabbitInitConfig {
    private final RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        log.info("Rabbit Config init!");
        ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();
        List<RabbitProps> props = Arrays.asList(RabbitProps.values());

        for (RabbitProps prop : props) {
            String type = prop.getType();

            RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);

            Queue queue;
            if (BooleanUtil.isTrue(prop.getDelayType())) {

                Map<String, Object> params = new HashMap<>(100);
                // x-dead-letter-exchange 声明了队列里的死信转发到的DLX名称，
                params.put("x-dead-letter-exchange", prop.getExchangeName());
                // x-dead-letter-routing-key 声明了这些死信在转发时携带的 routing-key 名称。
                params.put("x-dead-letter-routing-key", prop.getRoutingKeyName());

                queue = new Queue(prop.getQueueName(), true, false, false);
            } else {
                queue = new Queue(prop.getQueueName());
            }
            //queue
            rabbitAdmin.declareQueue(queue);
            //exchange
            Exchange exchange;
            if (type.equalsIgnoreCase(RabbitExchangeType.FANOUT_EXCHANGE)) {
                exchange = new FanoutExchange(prop.getExchangeName(), true, false);
            } else if (type.equalsIgnoreCase(RabbitExchangeType.DIRECT_EXCHANGE)) {
                exchange = new DirectExchange(prop.getExchangeName(), true, false);
            } else if (type.equalsIgnoreCase(RabbitExchangeType.TOPIC_EXCHANGE)) {
                exchange = new TopicExchange(prop.getExchangeName(), true, false);
            } else if (type.equalsIgnoreCase(RabbitExchangeType.LAZY_EXCHANGE)) {
                Map<String, Object> params = new HashMap<>(100);
                params.put("x-delayed-type", "direct");
                exchange = new CustomExchange(prop.getExchangeName(), "x-delayed-message", true, false, params);
            } else {
                continue;
            }
            log.info("exchangeType:{}, exchangeName:{}", exchange.getType(), exchange.getName());
            rabbitAdmin.declareExchange(exchange);
            //binding
            List<String> routingKeyNames = CollUtil.emptyIfNull(prop.getRoutingKeyName());
            for (String routingKeyName : routingKeyNames) {
                rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(routingKeyName).noargs());
            }
        }
        log.info("Rabbit Config init start!");
    }
}
