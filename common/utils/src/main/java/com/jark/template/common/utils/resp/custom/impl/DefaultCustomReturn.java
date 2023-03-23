package com.jark.template.common.utils.resp.custom.impl;


import com.jark.template.common.utils.json.JsonUtil;
import com.jark.template.common.utils.resp.custom.CustomReturn;

/**
 * @author ponder
 * 默认返回空字符串
 */
public class DefaultCustomReturn implements CustomReturn {
    @Override
    public String of(final Object object) {
        return JsonUtil.toJSON(object);
    }
}
