package io.github.xuefm.moli.data.sys.sysresource;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 系统账号：添加资源权限请求参数
 */
@Data
@Schema(title = "AddResourceRequest", description = "系统账号：添加资源权限请求参数")
public class AddResourceRequest {



    @NotNull(message = "type不能为null")
    @Schema(title = "类型", description = "0：组（菜单），1：接口", required = true, allowableValues = {"0", "1"}, example = "0")
    private Integer type;

    @Schema(title = "上级 id", description = "为空表示顶级资源")
    private String superiorId;

    @NotNull(message = "title不能为null")
    @Schema(title = "资源标题", required = true, example = "xx资源")
    private String title;

    @NotNull(message = "code不能为null")
    @Schema(title = "资源code", required = true, example = "xxCode")
    private String code;

}
