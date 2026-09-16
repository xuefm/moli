package io.github.xuefm.moli.data.sys.sysresource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


@Data
@Schema(description = "资源树响应数据")
public class SysResourceTreeVO implements java.io.Serializable {
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
    @Schema(title = "上级 id")
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

    @Schema(title = "下级资源")
    private List<SysResourceTreeVO> children;

}
