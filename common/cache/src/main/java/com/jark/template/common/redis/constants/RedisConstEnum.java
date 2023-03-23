package com.jark.template.common.redis.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Redis 相关常量
 */
@Getter
@AllArgsConstructor
public enum RedisConstEnum {

    /**
     * 锁前缀
     */
    LOCK_NAME_PREFIX("lock"),

    /**
     * Redis锁的 间隔字符
     */
    LOCK_NAME_SEPARATOR(":"),

    /**
     * 释放锁时的lua脚本
     */
    LUA_SCRIPT("""
        if redis.call('get', KEYS[1]) == ARGV[1]
            then
                return redis.call('del', KEYS[1])
            else
                return 0
        end""");

    private String name;


    @Override
    public String toString() {
        return name;
    }
}
