package com.jark.template.common.redis.utils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.jark.template.common.utils.tools.OptionalUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ponder
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisUtil {

    /**
     * 一天 + 1个小时  后面的随机数防止缓存穿透和击穿
     */
    private static final long EXPIRE_TIME = 60L * 60L * 24L + RandomUtil.randomInt(1, 60 * 60);

    private final RedisTemplate<String, Object> redisTemplate;

    // ============================= Redis 消息订阅/发布 ==============

    /**
     * 消息发布
     *
     * @param channel 发布topic
     * @param obj 发送消息
     */
    public void publish(final String channel, final Object obj) {
        redisTemplate.convertAndSend(channel, obj);
    }
    // =============================common============================

    /**
     * 获取所有Key
     *
     * @param pattern 为空时 匹配所有key
     *
     * @return 匹配的Key
     */
    public Set<String> keys(final String pattern) {
        if (StrUtil.isEmpty(pattern)) {
            return redisTemplate.keys("*");
        }
        return redisTemplate.keys(pattern);
    }

    /**
     * 指定缓存失效时间
     *
     * @param key 键
     * @param time 时间(秒)
     */
    public void expire(final Optional<String> key, final long time) {
        try {
            if (time > 0) {
                key.map(k -> redisTemplate.expire(k, time, TimeUnit.SECONDS));
            }
        } catch (Exception e) {
            log.error("redis expire cache error", e);
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     *
     * @return 时间(秒) 返回0代表为永久有效
     */
    public Optional<Long> getExpire(final String key) {
        return Optional.ofNullable(redisTemplate.getExpire(key, TimeUnit.SECONDS));
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     *
     * @return true 存在 false不存在
     */
    public Optional<Boolean> hasKey(final Optional<String> key) {
        try {
            return key.map(redisTemplate::hasKey);
        } catch (Exception e) {
            log.error("redis hasKey error", e);
            return Optional.of(false);
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public void del(final String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollUtil.toList(key));
            }
        }
    }


    /**
     * 删除缓存
     *
     * @param keys 可以传一个值 或多个
     */
    public void del(final Set<String> keys) {
        if (keys != null && keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 删除以key为前缀的数据
     *
     * @param key 前缀
     */
    public void delPrefix(final String key) {
        // 获取Redis中特定前缀
        Set<String> keys = redisTemplate.keys(key + "*");
        redisTemplate.delete(keys);
    }

    /**
     * 删除patternKey数据, 正则表达式
     *
     * @param patternKey 需要匹配的key，可以为正则
     */
    public void delPattern(final Optional<String> patternKey) {
        patternKey.map(redisTemplate::keys).map(redisTemplate::delete);
    }

    // ============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     *
     * @return 值
     */
    public Optional<Object> get(final Optional<String> key) {
        return key.map(k -> Optional.ofNullable(redisTemplate.opsForValue().get(k)));
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @param clazz 类型
     * @param <T> 类型
     *
     * @return 值
     */
    public <T> Optional<T> get(final Optional<String> key, final Class<T> clazz) {
        return this.get(key).map(clazz::cast);
    }

    /**
     * 普通缓存放入
     * 默认为一天
     *
     * @param key 键
     * @param value 值
     *
     * @return true成功 false失败
     */
    public boolean set(final String key, final Object value) {
        return this.set(key, value, EXPIRE_TIME);
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     *
     * @return true成功 false 失败
     */
    public boolean set(final String key, final Object value, final long time) {
        return set(key, Optional.ofNullable(value), time);
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     *
     * @return true成功 false 失败
     */
    public boolean set(final String key, final Optional<Object> value, final long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("redis set key:{},value:{},time:{} error", key, value, time, e);
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key 键
     * @param delta 要增加几(大于0)
     *
     * @return key后的长度
     */
    public Optional<Long> incr(final Optional<String> key, final long delta) {
        Assert.isTrue(delta > 0, "递增因子必须大于0");
        return key.map(k -> redisTemplate.opsForValue().increment(k, delta));
    }

    /**
     * 递减
     *
     * @param key 键
     * @param delta 要减少几(小于0)
     *
     * @return key后的长度
     */
    public Optional<Long> decr(final Optional<String> key, final long delta) {
        Assert.isTrue(delta > 0, "递增因子必须大于0");
        return key.map(k -> redisTemplate.opsForValue().increment(k, -delta));
    }

    // ================================Map=================================

    /**
     * HashGet
     *
     * @param key 键 不能为null
     * @param item 项 不能为null
     *
     * @return 值
     */
    public Optional<Object> hget(final Optional<String> key, final Optional<String> item) {
        return OptionalUtils.apply(key, item, (k, i) -> redisTemplate.opsForHash().get(k, i));
    }

    /**
     * 获取所有value
     *
     * @param key
     *
     * @return key对应的所有内容
     */
    public Optional<List<Object>> hgets(final Optional<String> key) {
        return key.map(k -> redisTemplate.opsForHash().values(k));
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     *
     * @return 对应的多个键值
     */
    public Optional<Map<Object, Object>> hmget(final Optional<String> key) {
        return key.map(k -> redisTemplate.opsForHash().entries(k));
    }

    /**
     * 返回 key 中的数量
     *
     * @param key
     *
     * @return key对应数据的长度
     */
    public Optional<Long> hSize(final Optional<String> key) {
        return key.map(k -> redisTemplate.opsForHash().size(k));
    }

    /**
     * HashSet 默认为一天
     *
     * @param key 键
     * @param map 对应多个键值
     *
     * @return true 成功 false 失败
     */
    public boolean hmset(final Optional<String> key, final Map<String, Object> map) {
        return this.hmset(key, map, EXPIRE_TIME);
    }

    /**
     * HashSet 默认为一天
     *
     * @param key 键
     * @param hashKey 键
     * @param value 值
     */
    public void hmset(final Optional<String> key, final String hashKey, final Object value) {
        key.ifPresent(k -> redisTemplate.opsForHash().put(key.get(), hashKey, value));
    }

    /**
     * HashSet 并设置时间
     *
     * @param key 键
     * @param map 对应多个键值
     * @param time 时间(秒)
     *
     * @return true成功 false失败
     */
    public boolean hmset(final Optional<String> key, final Map<String, Object> map, final long time) {
        try {
            key.ifPresent(k -> redisTemplate.opsForHash().putAll(k, map));
            if (time > 0) {
                expire(key, time);
            } else {
                expire(key, EXPIRE_TIME);
            }
            return true;
        } catch (Exception e) {
            log.error("redis hmset key:{},value:{},time:{} error", key, map, time, e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * 默认为一天
     *
     * @param key 键
     * @param item 项
     * @param value 值
     *
     * @return true 成功 false失败
     */
    public boolean hset(final Optional<String> key, final Optional<String> item, final Optional<Object> value) {
        return this.hset(key, item, value, EXPIRE_TIME);
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key 键
     * @param item 项
     * @param value 值
     * @param time 时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     *
     * @return true 成功 false失败
     */
    public boolean hset(final Optional<String> key, final Optional<String> item, final Optional<Object> value, final long time) {
        try {
            OptionalUtils.accept(key, item, value, (k, i, v) -> {
                redisTemplate.opsForHash().put(k, i, v);
            });
            if (time > 0) {
                expire(key, time);
            } else {
                expire(key, EXPIRE_TIME);
            }
            return true;
        } catch (Exception e) {
            log.error("redis hset key:{},item:{},value:{},time:{} error", key, item, value, time, e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key 键 不能为null
     * @param item 项 可以使多个 不能为null
     *
     * @return 值
     */
    public Optional<Long> hdel(final Optional<String> key, final Object... item) {
        return key.map(k -> redisTemplate.opsForHash().delete(k, item));
    }

    /**
     * 判断hash表中是否有该项的值。
     *
     * @param key 键 不能为null
     * @param item 项 不能为null
     *
     * @return true 存在 false不存在
     */
    public Optional<Boolean> hHasKey(final Optional<String> key, final Optional<String> item) {
        return OptionalUtils.apply(key, item, (k, i) -> redisTemplate.opsForHash().hasKey(k, i));
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * 默认为一天
     *
     * @param key 键
     * @param item 项
     * @param by 要增加几(大于0)
     *
     * @return key的位置
     */
    public Optional<Double> hincr(final Optional<String> key, final Optional<String> item, final Double by) {
        return OptionalUtils.apply(key, item, (k, i) -> {
            final Double increment = redisTemplate.opsForHash().increment(k, i, by);
            expire(key, EXPIRE_TIME);
            return increment;
        });
    }

    /**
     * hash递减
     * 默认为一天
     *
     * @param key 键
     * @param item 项
     * @param by 要减少记(小于0)
     *
     * @return 值
     */
    public Optional<Double> hdecr(final Optional<String> key, final Optional<String> item, final Double by) {
        return OptionalUtils.apply(key, item, (k, i) -> {
            final Double increment = redisTemplate.opsForHash().increment(k, i, -by);
            expire(key, EXPIRE_TIME);
            return increment;
        });
    }

    // ============================set=============================

    /**
     * 根据key获取Set中的所有值。
     *
     * @param key 键
     *
     * @return 所有不重复的数据
     */
    public Optional<Set<Object>> sGet(final Optional<String> key) {
        return key.map(k -> redisTemplate.opsForSet().members(k));
    }

    /**
     * 根据value从一个set中查询,是否存在。
     *
     * @param key 键
     * @param value 值
     *
     * @return true 存在 false不存在
     */
    public Optional<Boolean> sHasKey(final Optional<String> key, final Optional<Object> value) {
        return OptionalUtils.apply(key, value, (k, v) -> redisTemplate.opsForSet().isMember(k, v));
    }

    /**
     * 将数据放入set缓存。
     * 默认为一天
     *
     * @param key 键
     * @param values 值 可以是多个
     *
     * @return 成功个数
     */
    public Optional<Long> sSet(final Optional<String> key, final Object... values) {
        return key.map(k -> {
            final Long add = redisTemplate.opsForSet().add(k, values);
            expire(key, EXPIRE_TIME);
            return add;
        });
    }

    /**
     * 将set数据放入缓存。
     *
     * @param key 键
     * @param time 时间(秒)
     * @param values 值 可以是多个
     *
     * @return 成功个数
     */
    public Optional<Long> sSetAndTime(final Optional<String> key, final long time, final Object... values) {
        return key.map(k -> {
            final Long count = redisTemplate.opsForSet().add(k, values);
            expire(key, time);
            return count;
        });
    }

    /**
     * 获取set缓存的长度。
     *
     * @param key 键
     *
     * @return 长度
     */
    public Optional<Long> sGetSetSize(final Optional<String> key) {
        return key.map(k -> redisTemplate.opsForSet().size(k));
    }

    /**
     * 移除值为value的。
     *
     * @param key 键
     * @param values 值 可以是多个
     *
     * @return 移除的个数
     */
    public Optional<Long> setRemove(final Optional<String> key, final Object... values) {
        return key.map(k -> redisTemplate.opsForSet().remove(k, values));
    }
    // ===============================list=================================

    /**
     * 获取list缓存的内容。
     *
     * @param key 键
     * @param start 开始
     * @param end 结束 0 到 -1代表所有值
     *
     * @return 所有数据
     */
    public Optional<List<Object>> lGet(final Optional<String> key, final long start, final long end) {
        return key.map(k -> redisTemplate.opsForList().range(k, start, end));
    }

    /**
     * 获取list缓存的长度。
     *
     * @param key 键
     *
     * @return 长度
     */
    public Optional<Long> lGetListSize(final Optional<String> key) {
        return key.map(k -> redisTemplate.opsForList().size(k));
    }

    /**
     * 通过索引 获取list中的值.
     *
     * @param key 键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     *
     * @return 返回数据
     */
    public Optional<Object> lGetIndex(final Optional<String> key, final long index) {
        return key.map(k -> redisTemplate.opsForList().index(k, index));
    }

    /**
     * 将list放入缓存。
     * 默认为一天
     *
     * @param key 键
     * @param value 值
     *
     * @return true：成功
     */
    public Optional<Long> lSet(final Optional<String> key, final Optional<Object> value) {
        return this.lSet(key, value, EXPIRE_TIME);
    }

    /**
     * 将list放入缓存。
     *
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     *
     * @return true：成功
     */
    public Optional<Long> lSet(final Optional<String> key, final Optional<Object> value, final long time) {
        return OptionalUtils.apply(key, value, (k, v) -> {
            final Long count = redisTemplate.opsForList().rightPush(k, v);
            if (time > 0) {
                expire(key, time);
            } else {
                expire(key, EXPIRE_TIME);
            }
            return count;
        });
    }

    /**
     * 将list放入缓存。
     * 默认为一天
     *
     * @param key 键
     * @param value 值
     *
     * @return true：成功
     */
    public Optional<Long> lSet(final Optional<String> key, final List<Object> value) {
        return this.lSet(key, value, EXPIRE_TIME);
    }

    /**
     * 将list放入缓存。
     *
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     *
     * @return true: 成功
     */
    public Optional<Long> lSet(final Optional<String> key, final List<Object> value, final long time) {
        return key.map(k -> {
            final Long count = redisTemplate.opsForList().rightPush(k, value);
            if (time > 0) {
                expire(key, time);
            } else {
                expire(key, EXPIRE_TIME);
            }
            return count;
        });
    }

    /**
     * 根据索引修改list中的某条数据.
     *
     * @param key 键
     * @param index 索引
     * @param value 值
     */
    public void lUpdateIndex(final Optional<String> key, final Optional<Long> index, final Optional<Object> value) {
        OptionalUtils.accept(key, index, value, (k, i, v) -> {
            redisTemplate.opsForList().set(k, i, v);
            expire(key, EXPIRE_TIME);
        });
    }

    /**
     * 移除N个值为value.
     *
     * @param key 键
     * @param count 移除多少个
     * @param value 值
     *
     * @return 移除的个数
     */
    public Optional<Long> lRemove(final Optional<String> key, final Optional<Long> count, final Optional<Object> value) {
        return OptionalUtils.apply(key, count, value, (k, c, v) -> redisTemplate.opsForList().remove(k, c, v));
    }
}
