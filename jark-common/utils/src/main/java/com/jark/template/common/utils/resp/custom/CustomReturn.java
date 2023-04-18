package com.jark.template.common.utils.resp.custom;

import com.jark.template.common.utils.json.JsonUtil;

/**
 * @author ponder
 */
public interface CustomReturn {

    /**
     * 数据转换,自定义数据类型转换
     * @param object 需要自定义序列化的数据
     * @return 返回给前端的数据
     */
    default String of(Object object){
        return JsonUtil.toJSON(object);
    }



}
