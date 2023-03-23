package com.jark.template.common.utils.json;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * Long 返回前端序列化工具
 *
 * @author ponder
 */
public class LongSerializer extends JsonSerializer<Long> implements ContextualSerializer {

    private NumberFormat numberFormat;

    public LongSerializer() {
    }

    public LongSerializer(final NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
    }

    @Override
    public void serialize(final Long value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
        Long value1 = ObjectUtil.defaultIfNull(value, 0L);
        if (ObjectUtil.isEmpty(numberFormat)) {
            gen.writeString(StrUtil.toString(value1));
        } else {
            if (0L == value1) {
                gen.writeString(StrUtil.EMPTY);
            } else {
                gen.writeString(StrUtil.toString(value1));
            }
        }
    }

    @Override
    public JsonSerializer<?> createContextual(final SerializerProvider prov, final BeanProperty property) {
        JsonFormat.Value format = findFormatOverrides(prov, property, handledType());
        if (format == null) {
            return this;
        }

        if (format.hasPattern()) {
            NumberFormat decimalFormat = new DecimalFormat(format.getPattern());
            decimalFormat.setRoundingMode(RoundingMode.DOWN);
            return new LongSerializer(decimalFormat);
        }

        return this;
    }

    private JsonFormat.Value findFormatOverrides(final SerializerProvider provider,
                                                   final BeanProperty prop, final Class<?> typeForDefaults) {
        if (prop != null) {
            return prop.findPropertyFormat(provider.getConfig(), typeForDefaults);
        }
        return provider.getDefaultPropertyFormat(typeForDefaults);
    }
}
