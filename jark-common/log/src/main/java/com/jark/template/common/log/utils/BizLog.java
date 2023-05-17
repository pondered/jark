package com.jark.template.common.log.utils;

import java.util.function.Supplier;

import org.slf4j.event.Level;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Log日志处理类
 */
@Slf4j
public final class BizLog {
    private BizLog() {
    }

    private static void log(final Level level, final String message) {
        switch (level) {
            case TRACE -> log.trace(message);
            case DEBUG -> log.debug(message);
            case INFO -> log.info(message);
            case WARN -> log.warn(message);
            case ERROR -> log.error(message);
            default -> log.info(message);
        }
    }

    public static void isTrue(final Level level, final boolean expression, final Supplier<String> supplier) {
        if (!expression) {
            log(level, supplier.get());
        }
    }

    public static void isTrue(final Level level, final boolean expression, final String message, final Object... params) {
        isTrue(level, expression, () -> StrUtil.format(message, params));
    }

    public static void isFalse(final Level level, final boolean expression, final Supplier<String> supplier) {
        if (expression) {
            log(level, supplier.get());
        }
    }

    public static void isFalse(final Level level, final boolean expression, final String message, final Object... params) {
        isFalse(level, expression, () -> StrUtil.format(message, params));
    }

    public static void isNull(final Level level, final Object object, final Supplier<String> supplier) {
        if (object != null) {
            log(level, supplier.get());
        }
    }

    public static void isNull(final Level level, final Object object, final String message, final Object... params) {
        isNull(level, object, () -> StrUtil.format(message, params));
    }

    public static void notNull(final Level level, final Object object, final Supplier<String> supplier) {
        if (object == null) {
            log(level, supplier.get());
        }
    }

    public static void notNull(final Level level, final Object object, final String message, final Object... params) {
        notNull(level, object, () -> StrUtil.format(message, params));
    }

    public static void notEmpty(final Level level, final String text, final Supplier<String> supplier) {
        if (StrUtil.isEmpty(text)) {
            log(level, supplier.get());
        }
    }

    public static void notEmpty(final Level level, final String text, final String message, final Object... params) {
        notEmpty(level, text, () -> StrUtil.format(message, params));
    }

    public static void isEmpty(final Level level, final String text, final Supplier<String> supplier) {
        if (StrUtil.isNotEmpty(text)) {
            log(level, supplier.get());
        }
    }

    public static void isEmpty(final Level level, final String text, final String message, final Object... params) {
        notEmpty(level, text, () -> StrUtil.format(message, params));
    }


    public static void notBlank(final Level level, final String text, final Supplier<String> supplier) {
        if (StrUtil.isNotBlank(text)) {
            log(level, supplier.get());
        }
    }

    public static void notBlank(final Level level, final String text, final String message, final Object... params) {
        notBlank(level, text, () -> StrUtil.format(message, params));
    }

}
