package com.jark.template.common.utils.resp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.jark.template.common.utils.assertion.BizAssertMessage;
import com.jark.template.common.utils.json.JsonUtil;
import com.jark.template.common.utils.tools.MDCUtil;

import lombok.Data;
import lombok.ToString;

/**
 * @author Ponder
 */
@Data
@ToString
public final class R<T> implements Serializable {
    /**
     * 是否成功
     */
    private boolean succ;

    /**
     * 服务器当前时间戳
     */
    private long ts = System.currentTimeMillis();

    /**
     * 成功数据
     */
    private T data;

    /**
     * 错误码
     */
    private int code;

    /**
     * 错误描述
     */
    private String msg;

    /**
     * 请求id
     */
    private String requestId;

    public R() {
    }

    /**
     * 内部使用，用于构造成功的结果
     */
    private R(final int code, final boolean succ, final String msg, final T data) {
        this.requestId = MDCUtil.getRequestId();
        this.code = code;
        this.succ = succ;
        this.msg = msg;
        this.data = data;
        this.ts = System.currentTimeMillis();
    }

    private R(final int code, final boolean succ, final String msg) {
        this.requestId = MDCUtil.getRequestId();
        this.code = code;
        this.succ = succ;
        this.msg = msg;
    }

    private R(final int code, final boolean succ, final T data) {
        this.requestId = MDCUtil.getRequestId();
        this.code = code;
        this.succ = succ;
        this.data = data;
    }

    private R(final int code, final boolean succ) {
        this.requestId = MDCUtil.getRequestId();
        this.code = code;
        this.succ = succ;
    }

    public static <T> R<T> ok() {
        return new R<T>(RespCode.OK, true);
    }

    public static <T> R<T> ok(final T data) {
        return new R<T>(RespCode.OK, true, data);
    }

    public static <T> R<T> ok(final int code, final T data) {
        return new R<T>(code, true, data);
    }

    public static <T> R<T> ok(final String msg, final T data) {
        return new R<T>(RespCode.OK, true, msg, data);
    }

    public static <T> R<T> ok(final BizAssertMessage template, final T data) {
        return new R<T>(template.getCode(), true, template.getName(), data);
    }

    public static <T> R<T> fail() {
        return new R<T>(RespCode.SERVER_ERR, false);
    }

    public static <T> R<T> fail(final int code) {
        return new R<T>(code, false);
    }

    public static <T> R<T> fail(final int code, final String msg) {
        return new R<T>(code, false, msg);
    }

    public static <T> R<T> fail(final String msg) {
        return new R<T>(RespCode.SERVER_ERR, false, msg);
    }

    public static <T> R<T> fail(final int code, final String msg, final T data) {
        return new R<T>(code, false, msg, data);
    }

    public static <T> R<T> fail(final BizAssertMessage template) {
        return new R<T>(template.getCode(), false, template.getName());
    }

    public static <T> R<T> fail(final BizAssertMessage template, final T data) {
        return new R<T>(template.getCode(), false, template.getName(), data);
    }

    /**
     * @return 转换为JSON字符串
     */
    public String toJson() {
        return JsonUtil.toJSON(this);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>(100);
        map.put("ts", this.ts);
        map.put("succ", this.succ);
        map.put("data", this.data);
        map.put("code", this.code);
        map.put("msg", this.msg);
        map.put("requestId", this.requestId);
        return map;
    }
}
