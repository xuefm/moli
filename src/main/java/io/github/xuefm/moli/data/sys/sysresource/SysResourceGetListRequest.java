package io.github.xuefm.moli.data.sys.sysresource;

import io.github.xuefm.moli.data.web.BasisPageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 系统账号：资源列表请求参数
 */
@Data
@Schema(title = "SysResourceGetListRequest", description = "系统账号：资源列表请求参数")
public class SysResourceGetListRequest extends BasisPageRequest implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 资源标题
     */
    @Schema(title = "资源标题", example = "xx资源")
    private String title;
}
