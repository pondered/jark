package com.jark.template.common.utils.assertion;

import java.util.Map;
import java.util.function.Supplier;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 业务断言工具类
 */
public final class BizAssert {
    private BizAssert() {
    }

    /**
     * 断言是否为真，如果为 {@code false} 抛出给定的异常<br>
     *
     * <pre class="code">
     * Assert.isTrue(i &gt; 0, BizException::new);
     * </pre>
     *
     * @param <X> 异常类型
     * @param expression 布尔值
     * @param supplier 指定断言不通过时抛出的异常
     *
     * @throws X if expression is {@code false}
     */
    private static <X extends Throwable> void check(final boolean expression, final Supplier<? extends X> supplier) throws X {
        if (expression) {
            throw supplier.get();
        }
    }

    /**
     * 断言是否为真，如果为 {@code false} 抛出 {@code BizException} 异常<br>
     *
     * <pre class="code">
     * Assert.isTrue(i &gt; 0, "The value must be greater than zero");
     * </pre>
     *
     * @param <M> 枚举类型
     * @param expression 布尔值
     * @param template 错误抛出异常附带的消息模板，变量用{}代替
     * @param params 参数列表
     *
     * @throws BizException if expression is {@code false}
     */
    public static <M extends Enum<M> & BizAssertMessage> void isTrue(final boolean expression, final M template, final Object... params)
        throws BizException {
        check(!expression, () -> new BizException(template, params));
    }

    /**
     * 断言是否为假，如果为 {@code true} 抛出 {@code BizException} 异常<br>
     *
     * <pre class="code">
     * Assert.isFalse(i &lt; 0, "The value must not be negative");
     * </pre>
     *
     * @param <M> 枚举类型
     * @param expression 布尔值
     * @param template 错误抛出异常附带的消息模板，变量用{}代替
     * @param params 参数列表
     *
     * @throws BizException if expression is {@code false}
     */
    public static <M extends Enum<M> & BizAssertMessage> void isFalse(final boolean expression, final M template, final Object... params)
        throws BizException {
        check(expression, () -> new BizException(template, params));
    }

    /**
     * 断言对象是否为{@code null} ，如果不为{@code null} 抛出{@link BizException} 异常
     *
     * <pre class="code">
     * Assert.isNull(value, "The value must be null");
     * </pre>
     *
     * @param <M> 枚举类型
     * @param object 被检查的对象
     * @param template 消息模板，变量使用{}表示
     * @param params 参数列表
     *
     * @throws BizException if the object is not {@code null}
     */
    public static <M extends Enum<M> & BizAssertMessage> void isNull(final Object object, final M template, final Object... params)
        throws BizException {
        check(null != object, () -> new BizException(template, params));
    }

    /**
     * 断言对象是否不为{@code null} ，如果为{@code null} 抛出{@link BizException} 异常 Assert that an object is not {@code null} .
     *
     * <pre class="code">
     * Assert.notNull(clazz, "The class must not be null");
     * </pre>
     *
     * @param <M> 枚举类型
     * @param <T> 被检查对象泛型类型
     * @param object 被检查对象
     * @param template 错误消息模板，变量使用{}表示
     * @param params 参数
     *
     * @return 被检查后的对象
     *
     * @throws BizException if the object is {@code null}
     */
    public static <T, M extends Enum<M> & BizAssertMessage> T notNull(final T object, final M template, final Object... params) throws BizException {
        check(null == object, () -> new BizException(template, params));
        return object;
    }

    // ----------------------------------------------------------------------------------------------------------- Check empty

    /**
     * 检查给定字符串是否为空，为空抛出 {@link BizException}
     *
     * <pre class="code">
     * Assert.notEmpty(name, "Name must not be empty");
     * </pre>
     *
     * @param <M> 枚举类型
     * @param <T> 字符串类型
     * @param text 被检查字符串
     * @param template 错误消息模板，变量使用{}表示
     * @param params 参数
     *
     * @return 非空字符串
     *
     * @throws BizException 被检查字符串为空
     * @see StrUtil#isNotEmpty(CharSequence)
     */
    public static <T extends CharSequence, M extends Enum<M> & BizAssertMessage> T notEmpty(final T text, final M template, final Object... params)
        throws BizException {
        check(StrUtil.isEmpty(text), () -> new BizException(template, params));
        return text;
    }

    /**
     * 断言给定数组是否包含元素，数组必须不为 {@code null} 且至少包含一个元素
     *
     * <pre class="code">
     * Assert.notEmpty(array, "The array must have elements");
     * </pre>
     *
     * @param <M> 枚举类型
     * @param <T> 数组元素类型
     * @param array 被检查的数组
     * @param template 异常时的消息模板
     * @param params 参数列表
     *
     * @return 被检查的数组
     *
     * @throws BizException if the object array is {@code null} or has no elements
     */
    public static <T, M extends Enum<M> & BizAssertMessage> T[] notEmpty(final T[] array, final M template, final Object... params)
        throws BizException {
        check(ArrayUtil.isEmpty(array), () -> new BizException(template, params));
        return array;
    }

    /**
     * 断言给定集合非空
     *
     * <pre class="code">
     * Assert.notEmpty(collection, "Collection must have elements");
     * </pre>
     *
     * @param <M> 枚举类型
     * @param <E> 集合元素类型
     * @param <T> 集合类型
     * @param collection 被检查的集合
     * @param template 异常时的消息模板
     * @param params 参数列表
     *
     * @return 非空集合
     *
     * @throws BizException if the collection is {@code null} or has no elements
     */
    public static <E, T extends Iterable<E>, M extends Enum<M> & BizAssertMessage> T notEmpty(final T collection, final M template,
                                                                                              final Object... params)
        throws BizException {
        check(CollUtil.isEmpty(collection), () -> new BizException(template, params));
        return collection;
    }

    /**
     * 断言给定Map非空
     *
     * <pre class="code">
     * Assert.notEmpty(map, "Map must have entries");
     * </pre>
     *
     * @param <M> 枚举类型
     * @param <K> Key类型
     * @param <V> Value类型
     * @param <T> Map类型
     * @param map 被检查的Map
     * @param template 异常时的消息模板
     * @param params 参数列表
     *
     * @return 被检查的Map
     *
     * @throws BizException if the map is {@code null} or has no entries
     */
    public static <K, V, T extends Map<K, V>, M extends Enum<M> & BizAssertMessage> T notEmpty(final T map, final M template, final Object... params)
        throws BizException {
        check(MapUtil.isEmpty(map), () -> new BizException(template, params));
        return map;
    }

    /**
     * 检查给定字符串是否为空白（null、空串或只包含空白符），为空抛出 {@link BizException}
     *
     * <pre class="code">
     * Assert.notBlank(name, "Name must not be blank");
     * </pre>
     *
     * @param <M> 枚举类型
     * @param <T> 字符串类型
     * @param text 被检查字符串
     * @param template 错误消息模板，变量使用{}表示
     * @param params 参数
     *
     * @return 非空字符串
     *
     * @throws BizException 被检查字符串为空白
     * @see StrUtil#isNotBlank(CharSequence)
     */
    public static <T extends CharSequence, M extends Enum<M> & BizAssertMessage> T notBlank(final T text, final M template, final Object... params)
        throws BizException {
        check(StrUtil.isBlank(text), () -> new BizException(template, params));
        return text;
    }

    /**
     * 断言给定数组是否不包含{@code null}元素，如果数组为空或 {@code null}将被认为不包含
     *
     * <pre class="code">
     * Assert.noNullElements(array, "The array must not have null elements");
     * </pre>
     *
     * @param <M> 枚举类型
     * @param <T> 数组元素类型
     * @param array 被检查的数组
     * @param template 异常时的消息模板
     * @param params 参数列表
     *
     * @return 被检查的数组
     *
     * @throws BizException if the object array contains a {@code null} element
     */
    public static <T, M extends Enum<M> & BizAssertMessage> T[] noNullElements(final T[] array, final M template, final Object... params)
        throws BizException {
        check(ArrayUtil.hasNull(array), () -> new BizException(template, params));
        return array;
    }

    /**
     * 断言给定对象是否是给定类的实例
     *
     * <pre class="code">
     * Assert.instanceOf(Foo.class, foo, "foo must be an instance of class Foo");
     * </pre>
     *
     * @param <M> 枚举类型
     * @param <T> 被检查对象泛型类型
     * @param type 被检查对象匹配的类型
     * @param obj 被检查对象
     * @param template 异常时的消息模板
     * @param params 参数列表
     *
     * @return 被检查对象
     *
     * @throws BizException if the object is not an instance of clazz
     * @see Class#isInstance(Object)
     */
    public static <T, M extends Enum<M> & BizAssertMessage> T isInstanceOf(final Class<?> type, final T obj, final M template, final Object... params)
        throws BizException {
        if (!type.isInstance(obj)) {
            throw new BizException(template, params);
        }
        return obj;
    }

    /**
     * 断言 {@code superType.isAssignableFrom(subType)} 是否为 {@code true}.
     *
     * <pre class="code">
     * Assert.isAssignable(Number.class, myClass, "myClass must can be assignable to class Number");
     * </pre>
     *
     * @param <M> 枚举类型
     * @param superType 需要检查的父类或接口
     * @param subType 需要检查的子类
     * @param template 异常时的消息模板
     * @param params 参数列表
     *
     * @throws BizException 如果子类非继承父类，抛出此异常
     */
    public static <M extends Enum<M> & BizAssertMessage> void isAssignable(final Class<?> superType, final Class<?> subType, final M template,
                                                                           final Object... params)
        throws BizException {
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new BizException(template, params);
        }
    }

    /**
     * 检查值是否在指定范围内
     *
     * @param <M> 枚举类型
     * @param value 值
     * @param min 最小值（包含）
     * @param max 最大值（包含）
     * @param template 异常信息模板，类似于"aa{}bb{}cc"
     * @param params 异常信息参数，用于替换"{}"占位符
     *
     * @return 经过检查后的值
     *
     * @since 5.7.15
     */
    public static <M extends Enum<M> & BizAssertMessage> int checkBetween(final int value, final int min, final int max, final M template,
                                                                          final Object... params) {
        check(value < min || value > max, () -> new BizException(template, params));
        return value;
    }

    /**
     * 断言两个对象是否不相等,如果两个对象相等 抛出BizException 异常
     * <pre class="code">
     *   Assert.notEquals(obj1,obj2,"obj1 must be not equals obj2");
     * </pre>
     *
     * @param <M> 枚举类型
     * @param obj1 对象1
     * @param obj2 对象2
     * @param template 异常信息模板，类似于"aa{}bb{}cc"
     * @param params 异常信息参数，用于替换"{}"占位符
     *
     * @throws BizException obj1 must be not equals obj2
     */
    public static <M extends Enum<M> & BizAssertMessage> void notEquals(final Object obj1, final Object obj2, final M template,
                                                                        final Object... params)
        throws BizException {
        check(ObjectUtil.equals(obj1, obj2), () -> new BizException(template, params));
    }


    /**
     * 断言两个对象是否相等,如果两个对象不相等 抛出BizException 异常
     * <pre class="code">
     *   Assert.isEquals(obj1,obj2,"obj1 must be equals obj2");
     * </pre>
     *
     * @param <M> 枚举类型
     * @param obj1 对象1
     * @param obj2 对象2
     * @param template 异常信息模板，类似于"aa{}bb{}cc"
     * @param params 异常信息参数，用于替换"{}"占位符
     *
     * @throws BizException obj1 must be equals obj2
     */
    public static <M extends Enum<M> & BizAssertMessage> void equals(final Object obj1, final Object obj2, final M template, final Object... params)
        throws BizException {
        check(ObjectUtil.notEqual(obj1, obj2), () -> new BizException(template, params));
    }

}
    


