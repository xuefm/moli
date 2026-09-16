package io.github.xuefm.moli.data.sys.sysrole;

import io.github.xuefm.moli.data.web.BasisPageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 系统账号：角色列表请求参数
 */
@Data
@Schema(title = "SysRoleGetListRequest", description = "系统账号：角色列表请求参数")
public class SysRoleGetListRequest extends BasisPageRequest implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(title = "角色名", example = "xx管理员")
    private String title;
}
