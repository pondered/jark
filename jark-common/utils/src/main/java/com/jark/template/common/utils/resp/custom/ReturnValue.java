package com.jark.template.common.utils.resp.custom;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jark.template.common.utils.resp.custom.impl.DefaultCustomReturn;


/**
 * @ClassName CustomReturnData
 * @Author PYH
 * @Date 2022/4/11 10:15
 * @Version 1.0.0
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReturnValue {

    Class<? extends CustomReturn> clazz() default DefaultCustomReturn.class;

    String contentType() default "application/json;charset=UTF-8";
}
