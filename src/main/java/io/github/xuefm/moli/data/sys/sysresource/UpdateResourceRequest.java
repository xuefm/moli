package io.github.xuefm.moli.data.sys.sysresource;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 系统账号：修改资源权限请求参数
 */
@Data
@Schema(title = "UpdateResourceRequest", description = "系统账号：修改资源权限请求参数")
public class UpdateResourceRequest {


    @NotNull(message = "id不能为null")
    @Schema(title = "资源id", required = true)
    private String id;

    @NotNull(message = "title不能为null")
    @Schema(title = "资源标题", required = true, example = "xx资源")
    private String title;


}
