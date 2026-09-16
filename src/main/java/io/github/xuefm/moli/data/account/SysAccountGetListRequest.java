package io.github.xuefm.moli.data.account;

import io.github.xuefm.moli.data.web.BasisPageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 系统账号：获取列表请求参数
 */
@Data
@Schema(title = "SysAccountGetListRequest", description = "系统账号：获取列表请求参数")
public class SysAccountGetListRequest extends BasisPageRequest implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(title = "登录名", example = "admin")
    private String loginName;
}
