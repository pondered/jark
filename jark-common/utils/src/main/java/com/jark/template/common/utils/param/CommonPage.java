package com.jark.template.common.utils.param;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author ponder
 * 分页请求参数
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonPage implements Serializable {

    /**
     * 页面大小
     */
    @Min(0)
    @Max(100)
    @Builder.Default
    private Integer pageSize = 10;

    /**
     * 页码
     */
    @Min(0)
    @Builder.Default
    private Integer pageIndex = 1;

    /**
     * 搜索数据
     */
    private List<Sorted> search;


    /**
     * 排序字段
     */
    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Sorted {

        /**
         * 优先级
         */
        private Integer ordered;

        /**
         * 排序方式
         */
        @Pattern(regexp = "DESC|ASC")
        private String order;

        /**
         * 排序字段
         */
        private String field;

    }
}
