package com.jark.template.common.web.debounce;


import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 防抖注解.
 *
 * @author ponder
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Debounce {

    String value() default "";

    boolean async() default false;

    int delay() default 100;

    int threads() default 1;

    boolean extend() default true;

}
