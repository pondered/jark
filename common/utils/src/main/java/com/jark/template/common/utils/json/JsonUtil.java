package com.jark.template.common.utils.json;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ponder
 * 本类不在使用
 */
@Slf4j
public final class JsonUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final ObjectMapper INSTANCE = OBJECT_MAPPER;

    static {
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);

        //针对于Date类型，文本格式化
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN));
        // Java8 日期处理
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class,
            new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN)));
        javaTimeModule.addDeserializer(LocalDateTime.class,
            new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));

        // 序列换成json时,将所有的long变成string
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.class, new LongSerializer());
        simpleModule.addSerializer(Long.TYPE, new LongSerializer());
        simpleModule.addSerializer(BigDecimal.class, new BigDecimalSerializer());
        OBJECT_MAPPER.registerModule(javaTimeModule).registerModule(simpleModule);
        //默认开启：如果一个类没有public的方法或属性时，会导致序列化失败。关闭后，会得到一个空JSON串。
        OBJECT_MAPPER.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        //默认关闭，即使用BigDecimal.toString()序列化。开启后，使用BigDecimal.toPlainString序列化，不输出科学计数法的值。
        OBJECT_MAPPER.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
        OBJECT_MAPPER.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        OBJECT_MAPPER.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        /******
         *  反序列化
         */
        OBJECT_MAPPER.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
    }

    private JsonUtil() {
    }

    /**
     * 返回 ObjectMapper 实例.
     *
     * @return ObjectMapper 实例
     */
    public static ObjectMapper getInstance() {
        return INSTANCE;
    }

    /**
     * 转换为 JSON 字符串.
     *
     * @param obj 对象
     * @param <T> 类型
     *
     * @return JSON字符串
     */
    public static <T> String toJSON(final T obj) {
        if (ObjectUtil.isEmpty(obj)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("转换json字符串异常:{}", obj, e);
        }
        return null;
    }

    /**
     * 将 Map 转换为 JSON。
     *
     * @param map map数据
     *
     * @return JSON字符串
     */
    public static String toJSON(final Map<Object, Object> map) {
        try {
            return OBJECT_MAPPER.writeValueAsString(map);
        } catch (Exception e) {
            log.error("mapToJSON error:{}", map, e);
        }
        return StrUtil.EMPTY;
    }

    /**
     * 对象转换为字符数组
     *
     * @param object 对象
     * @return 字节
     */
    public static byte[] toBytes(final Object object) {
        if (ObjectUtil.isEmpty(object)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsBytes(object);
        } catch (IOException e) {
            log.error("转化为byte异常:{}", object, e);
        }
        return null;
    }


    /**
     * 流转对象.
     *
     * @param inputStream 流
     * @param type 类型
     *
     * @return 对象
     */
    public static Object toBean(final InputStream inputStream, final Type type) {
        try {
            final JavaType javaType = OBJECT_MAPPER.constructType(type);
            return OBJECT_MAPPER.readValue(inputStream, javaType);
        } catch (Exception e) {
            log.error("转换为Bean:{}异常,", type, e);
        }
        return null;
    }

    /**
     * 字符串转对象。
     *
     * @param json JSON字符串
     * @param clazz 数据类型
     * @param <T> 数据类型
     *
     * @return Bean
     */
    public static <T> T toBean(final String json, final Class<T> clazz) {
        if (ObjectUtil.isEmpty(json) || clazz == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error(StrUtil.format("JSON字符串:{}转Bean:{}异常", json, clazz), e);
        }
        return null;
    }

    /**
     * 字符数组反序列化为对象.
     *
     * @param bytes 字节数组
     * @param clazz 类型
     * @param <T> 数据类型
     *
     * @return Bean
     */
    public static <T> T toBean(final byte[] bytes, final Class<T> clazz) {
        if (ObjectUtil.isEmpty(bytes) || clazz == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(bytes, clazz);
        } catch (IOException e) {
            log.error(StrUtil.format("数组:{}转Bean:{}异常", bytes, clazz), e);
        }
        return null;
    }

    /**
     * json转复杂对象.
     *
     * @param json JSON字符串
     * @param type 数据类型
     * @param <T> 数据类型
     *
     * @return Bean
     */
    public static <T> T toBean(final String json, final TypeReference<T> type) {
        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (IOException e) {
            log.error(StrUtil.format("字符串:{}转Bean:{}异常", json, type), e);
        }
        return null;
    }

    /**
     * 将 Map 转换为 JavaBean
     *
     * @param map map对象
     * @param clazz 需要转换的类型
     * @param <T> 数据类型
     *
     * @return 对象
     */
    public static <T> T toBean(final Map<Object, Object> map, final Class<T> clazz) {
        return OBJECT_MAPPER.convertValue(map, clazz);
    }


    /**
     * 转换为 JSON 字符串，忽略空值.
     *
     * @param obj 对象
     *
     * @return JSON字符串
     */
    public static String toJsonIgnoreNull(final Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
            simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);

            JavaTimeModule module = new JavaTimeModule();
            module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
            module.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));
            module.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN)));

            mapper.registerModule(simpleModule).registerModule(module);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("对象:{}转Json字符串,并忽略空值异常", obj, e);
        }
        return null;
    }

    /**
     * 字符串转换为 Map.
     *
     * @param json JSON 字符串
     * @param kClazz key对应的类型
     * @param vClazz value 对应的类型
     * @param <K> 数据类型
     * @param <V> 数据类型
     *
     * @return Map
     */
    public static <K, V> Map<K, V> toMap(final String json, final Class<K> kClazz, final Class<V> vClazz) {
        try {
            return OBJECT_MAPPER.readValue(json, OBJECT_MAPPER.getTypeFactory().constructMapType(Map.class, kClazz, vClazz));
        } catch (JsonProcessingException e) {
            log.error(StrUtil.format("toMap数据异常:{},kClazz:{},vClazz:{}", json, kClazz, vClazz), e);
        }
        return null;
    }

    /**
     * 字符串转换为 Map
     *
     * @param json 字符串
     * @param <T> 数据类型
     * @param vClazz 类型值
     *
     * @return Map类型
     */
    public static <T> Map<String, T> toMap(final String json, final Class<T> vClazz) {
        return toMap(json, String.class, vClazz);
    }

    /**
     * 获取泛型的 Collection Type
     *
     * @param collectionClass 泛型的Collection
     * @param elementClasses 元素类
     *
     * @return JavaType Java类型
     *
     * @since 1.0
     */
    public static JavaType getCollectionType(final Class<?> collectionClass, final Class<?> elementClasses) {
        return OBJECT_MAPPER.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    /**
     * 转译.
     *
     * @param message 字符串
     *
     * @return 转译后的JSON字符串
     */
    public static String parseJsonToString(final String message) {
        return message.replaceAll("\\\\", "").substring(1, message.length() - 1);
    }
}
