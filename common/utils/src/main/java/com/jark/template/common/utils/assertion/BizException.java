package com.jark.template.common.utils.assertion;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

import com.jark.template.common.utils.exception.BaseException;

/**
 * 业务异常类
 */
@Getter
public final class BizException extends BaseException {
    /**
     * 返回给前端的状态码
     */
    private final int code;

    /**
     * 业务处理
     *
     * @param template 提示语句范型类
     * @param <M> 范型接口
     */
    public <M extends Enum<M> & BizAssertMessage> BizException(final M template) {
        super(template.toString());
        code = template.getCode();
    }

    /**
     * 业务处理
     *
     * @param template 提示语句范型类
     * @param <M> 范型接口
     * @param params 参数
     */
    public <M extends Enum<M> & BizAssertMessage> BizException(final M template, final Object[] params) {
        super(StrUtil.format(template.toString(), params));
        code = template.getCode();
    }
}


