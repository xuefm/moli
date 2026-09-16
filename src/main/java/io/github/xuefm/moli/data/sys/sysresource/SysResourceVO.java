package io.github.xuefm.moli.data.sys.sysresource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "资源响应数据")
public class SysResourceVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Schema(title = "资源 id", example = "1600000000000000001")
    private String id;

    /**
     * 类型 0组 1接口
     */
    @Schema(title = "类型", description = "0：组（菜单），1：接口", allowableValues = {"0", "1"}, example = "0")
    private Integer type;

    /**
     * 层级
     */
    @Schema(title = "层级", example = "1")
    private Integer level;

    /**
     * 上级id
     */
    @Schema(title = "上级 id", description = "顶级资源的上级 id 为空")
    private String superiorId;

    /**
     * 资源标题
     */
    @Schema(title = "资源标题", example = "xx资源")
    private String title;

    /**
     * 资源code
     */
    @Schema(title = "资源 code", example = "xxCode")
    private String code;

    /**
     * 是否启用
     */
    @Schema(title = "是否启用", description = "1：启用，0：禁用", allowableValues = {"0", "1"}, example = "1")
    private Integer enabled;


    /**
     * 是否启用
     */
    @Schema(title = "是否含有下级", description = "true 表示还有下级，可继续逐级加载")
    private boolean hasChildren;


}
