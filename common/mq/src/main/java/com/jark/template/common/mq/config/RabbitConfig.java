package com.jark.template.common.mq.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.jark.template.common.mq.convert.RabbitJackJsonConverter;
import com.jark.template.common.mq.tracing.MDCMessagePostProcess;
import com.jark.template.common.mq.tracing.MDCReceivePostProcessors;

/**
 * @author ponder
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public final class RabbitConfig {
    private final RabbitTemplate rabbitTemplate;

    @Bean
    public RabbitAdmin rabbitAdmin(final ConnectionFactory defaultConnectionFactory) {
        return new RabbitAdmin(defaultConnectionFactory);
    }

    @Bean
    public AmqpTemplate template() {
        // JSON 消息转换器
        rabbitTemplate.setBeforePublishPostProcessors(new MDCMessagePostProcess());
        rabbitTemplate.setAfterReceivePostProcessors(new MDCReceivePostProcessors());

        rabbitTemplate.setMessageConverter(new RabbitJackJsonConverter());
        rabbitTemplate.setEncoding("UTF-8");

        //设置手工ack确认
        rabbitTemplate.setMandatory(true);
        // 设置消息投递到queue失败回退时回调
        rabbitTemplate.setReturnsCallback(returned -> {
            String correlationId = returned.getMessage().getMessageProperties().getCorrelationId();
            log.error("message：{} send failed, {}", correlationId, returned);
        });

        //  消息确认yml需要配置 设置消息发送到exchange结果回调
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info("send message success to exchange");
            } else {
                log.error("send message failed to exchange,because: {}", cause);
            }
        });
        return rabbitTemplate;
    }
}
