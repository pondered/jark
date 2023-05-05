package com.jark.template.common.mq.sender;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.jark.template.common.mq.constant.ExchangeEnum;
import com.jark.template.common.mq.constant.QueueEnum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 发送消息到MQ都通过这个方法.
 *
 * @author ponder
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitSender {

    private final RabbitTemplate template;

    /**
     * 发送数据到RabbitMQ
     *
     * @param content 发送内容
     * @param queueEnum 发送的queue名称
     */
    public void send(Object content, QueueEnum queueEnum) {
        log.info("send message, content:{},queueEnum:{}", content, queueEnum.getName());
        template.convertAndSend(queueEnum.getName(), content);
    }

    /**
     * 发送数据到MQ
     *
     * @param content 发送内容
     * @param queueEnum 发送的队列名称
     * @param exchangeEnum exchange名称
     */
    public void send(Object content, QueueEnum queueEnum, ExchangeEnum exchangeEnum) {
        log.info("send message, content:{},routingkey:{}", content, queueEnum);
        template.convertAndSend(exchangeEnum.getName(), queueEnum.getName(), content);
    }
}
