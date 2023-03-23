package com.jark.template.common.mq.tracing;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;

import com.jark.template.common.utils.constant.HeaderConst;
import com.jark.template.common.utils.tools.MDCUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 发送MQ消息前处理。
 *
 * @author ponder
 */
@Slf4j
public final class MDCMessagePostProcess implements MessagePostProcessor {
    @Override
    public Message postProcessMessage(final Message message) throws AmqpException {
        final MessageProperties messageProperties = message.getMessageProperties();
        final String requestId = String.valueOf(messageProperties.getHeader(MDCUtil.getRequestId()));
        final String from = String.valueOf(messageProperties.getHeader(HeaderConst.FROM));
        log.debug("AMQP接受来自:{}的消息:{}", from, message);
        MDCUtil.setRequestId(requestId);
        return message;
    }
}
