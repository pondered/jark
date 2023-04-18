package com.jark.template.common.web.config.convert;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import cn.hutool.core.date.DatePattern;
import lombok.extern.slf4j.Slf4j;

/**
 * POST 请求 Jackson类型转换
 */
@Slf4j
@Configuration
public class JacksonConvertConfiguration {

    /**
     * jackson 序列话
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder
            .modules(new Jdk8Module())
            .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)))
            .serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)))
            .serializerByType(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN)))
            .serializerByType(Date.class, new DateSerializer(false, new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN)))
            .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)))
            .deserializerByType(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)))
            .deserializerByType(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN)))
            .deserializerByType(Date.class, new DateDeserializers.DateDeserializer(DateDeserializers.DateDeserializer.instance,
                new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN), DatePattern.NORM_DATETIME_PATTERN))
            ;
    }

}


