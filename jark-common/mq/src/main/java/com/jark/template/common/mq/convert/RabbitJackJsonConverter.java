package com.jark.template.common.mq.convert;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.MessageConversionException;

import lombok.extern.slf4j.Slf4j;

import com.jark.template.common.utils.json.JsonUtil;

/**
 * 自定义消息转换器.
 *
 * @author ponder
 */
@Slf4j
public class RabbitJackJsonConverter extends AbstractMessageConverter {

    // 消息类型映射对象
    private static final ClassMapper CLASS_MAPPER = new DefaultClassMapper();

    /**
     * 创建消息.
     *
     * @param object 消息对象
     * @param messageProperties 消息属性
     *
     * @return 消息数据
     */
    @Override
    protected Message createMessage(final Object object, final MessageProperties messageProperties) {
        byte[] bytes = JsonUtil.toBytes(object);
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        messageProperties.setContentEncoding(StandardCharsets.UTF_8.name());
        if (bytes != null) {
            messageProperties.setContentLength(bytes.length);
        }
        CLASS_MAPPER.fromClass(object.getClass(), messageProperties);
        return new Message(bytes, messageProperties);
    }

    /**
     * 转换消息为对象.
     *
     * @param message 消息对象
     *
     * @return 具体的消息
     *
     * @throws MessageConversionException 消息转换异常
     */
    @Override
    public Object fromMessage(final Message message) throws MessageConversionException {
        Object content = null;
        MessageProperties properties = message.getMessageProperties();
        if (properties != null) {
            String contentType = properties.getContentType();
            if (contentType != null && contentType.contains("json")) {
                String encoding = properties.getContentEncoding();
                if (encoding == null) {
                    encoding = StandardCharsets.UTF_8.name();
                }
                try {
                    Class<?> targetClass = CLASS_MAPPER.toClass(
                        message.getMessageProperties());

                    content = convertBytesToObject(message.getBody(),
                        encoding, targetClass);
                } catch (IOException e) {
                    throw new MessageConversionException(
                        "Failed to convert Message content", e);
                }
            } else {
                log.error("Could not convert incoming message with content-type ["
                    + contentType + "]");
            }
        }
        if (content == null) {
            content = message.getBody();
        }
        return content;
    }

    /**
     * 将字节数组转换成实例对象.
     *
     * @param body Message对象主体字节数组
     * @param encoding 字符集
     * @param clazz 类型
     *
     * @return 具体对象数据
     *
     * @throws UnsupportedEncodingException 不支持的编码格式
     */
    private Object convertBytesToObject(final byte[] body, final String encoding,
                                        final Class<?> clazz) throws UnsupportedEncodingException {
        String contentAsString = new String(body, encoding);
        return JsonUtil.toBean(contentAsString, clazz);
    }
}
