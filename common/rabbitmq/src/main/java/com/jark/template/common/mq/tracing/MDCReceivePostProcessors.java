package com.jark.template.common.mq.tracing;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Value;

import com.jark.template.common.utils.constant.HeaderConst;
import com.jark.template.common.utils.tools.MDCUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 接受到消息后处理方式。
 *
 * @author ponder
 */
@Slf4j
public final class MDCReceivePostProcessors implements MessagePostProcessor {
    @Value("${spring.application.name}")
    private String appName;

    @Override
    public Message postProcessMessage(final Message message) throws AmqpException {
        MessageProperties messageProperties = message.getMessageProperties();
        messageProperties.setHeader(HeaderConst.REQUEST_ID, MDCUtil.getRequestId());
        messageProperties.setHeader(HeaderConst.FROM, appName);
        return message;
    }
}
