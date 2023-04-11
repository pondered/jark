package com.jark.template.common.web.config.convert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

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
                return Optional.ofNullable(source).map(s -> new DateTime(s).toLocalDateTime()).orElse(null);
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
                return Optional.ofNullable(source).map(s -> new DateTime(s).toLocalDateTime().toLocalDate()).orElse(null);
            }
        };
    }

}


