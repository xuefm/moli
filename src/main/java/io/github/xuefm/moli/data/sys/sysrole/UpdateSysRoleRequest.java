package io.github.xuefm.moli.data.sys.sysrole;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 系统账号：修改角色请求参数
 */
@Data
@Schema(title = "UpdateSysRoleRequest", description = "系统账号：修改角色请求参数")
public class UpdateSysRoleRequest implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    @Schema(title = "角色id", required = true, example = "1")
    private String id;

    /**
     * 角色名
     */
    @NotNull
    @Schema(title = "角色名", required = true, example = "xx角色")
    private String title;

    @NotNull
    @Schema(title = "角色code", required = true, example = "xx_admin")
    private String code;

    @NotNull
    @Schema(title = "资源列表", required = true, example = "[1,2,3]")
    private List<String> resourceList;
}
