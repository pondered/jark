package com.jark.template.common.utils.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限注解
 *
 * @author ponder
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {

    /**
     * 是否需要登录
     * true: 要登录
     * false: 不需要登录
     */
    boolean login() default true;

    /**
     * 权限名称
     */
    String name();

    /**
     * 描述
     */
    String desc();

    /**
     * <b>全局唯一</b>
     * 权限名称，如: admin:order:...:get
     * admin: 用户类型  user/admin/inner 三种类型
     * user: 普通用户
     * admin: 后台管理员用户
     * inner: 表示仅内部使用服务调用,不暴露给外部
     * order: 订单相关
     * get: 查看单条记录
     * <p>
     * 结尾只能为: update/delete/insert/list/get/check/validate/page
     * 每个接口尽量做到原子化,一个接口尽量只做一件事情
     * <p>
     * 可以为多个
     */
    String[] code();

    /**
     * 隐藏还是显示,默认显示
     * 0:隐藏, 1:显示
     */
    int hidden() default 1;

    /**
     * 是否删除,默认不删除
     * 0：未删除，1：删除
     */
    int deleted() default 0;

    /**
     * 排序
     * 默认为0,越大表示越前
     */
    int sort() default 0;
}
