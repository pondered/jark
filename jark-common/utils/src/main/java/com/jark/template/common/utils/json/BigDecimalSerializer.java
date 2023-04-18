package com.jark.template.common.utils.json;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * BigDecimal 返回前端序列化工具
 *
 * @author ponder
 */
public class BigDecimalSerializer extends JsonSerializer<BigDecimal> implements ContextualSerializer {

    /**
     * 默认小数位数
     */
    private static final int DEFAULT_SCALE = 20;

    /**
     *
     */
    private DecimalFormat decimalFormat;

    public BigDecimalSerializer() {
    }

    public BigDecimalSerializer(final DecimalFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
    }

    @Override
    public void serialize(final BigDecimal value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
        BigDecimal value1 = NumberUtil.toBigDecimal(value);
        if (ObjectUtil.isEmpty(decimalFormat)) {
            gen.writeString(NumberUtil.toStr(value1.setScale(DEFAULT_SCALE, RoundingMode.DOWN).stripTrailingZeros()));
        } else {
            gen.writeString(decimalFormat.format(new BigDecimal(decimalFormat.format(value1))));
        }
    }

    @Override
    public JsonSerializer<?> createContextual(final SerializerProvider prov, final BeanProperty property) {
        JsonFormat.Value format = findFormatOverrides(prov, property, handledType());
        if (format == null) {
            return this;
        }

        if (format.hasPattern()) {
            DecimalFormat decimalFormat = new DecimalFormat(format.getPattern());
            decimalFormat.setRoundingMode(RoundingMode.DOWN);
            return new BigDecimalSerializer(decimalFormat);
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
