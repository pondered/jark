package com.jark.template.app.config.convert;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import cn.hutool.core.date.DateTime;
import lombok.extern.slf4j.Slf4j;

/**
 * 转型转换
 */
@Slf4j
@Configuration
public class ConvertConfiguration {

    /**
     * 字符串转LocalDateTime
     */
    @Bean
    public Converter<String, LocalDateTime> string2localDateTimeConverter() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(final String source) {
                return new DateTime(source).toLocalDateTime();
            }
        };
    }

    /**
     * 字符串转LocalDate
     */
    @Bean
    public Converter<String, LocalDate> string2localDateConverter() {
        return new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(final String source) {
                return new DateTime(source).toLocalDateTime().toLocalDate();
            }
        };
    }

}


