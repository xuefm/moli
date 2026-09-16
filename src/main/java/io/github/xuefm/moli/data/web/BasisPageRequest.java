package io.github.xuefm.moli.data.web;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Positive;

/**
 * 分页请求参数
 */
@Data
public class BasisPageRequest implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Positive
    @Schema(title = "当前页", required = true, allowableValues = "range[1, infinity]", example = "1")
    private Integer current = 1;

    @Positive
    @Schema(title = "页面数据量", required = true, allowableValues = "range[1, 50]", example = "10")
    private Integer size = 10;
}
