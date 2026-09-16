package io.github.xuefm.moli.data.sys.sysrole;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "角色列表响应数据")
public class SysRoleVO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Schema(title = "角色 id", example = "1600000000000000001")
    private String id;

    /**
     * 角色名
     */
    @Schema(title = "角色名", example = "xx管理员")
    private String title;

    @Schema(title = "角色名code", example = "xx_admin")
    private String code;

    /**
     * 是否启用
     */
    @Schema(title = "是否启用", description = "1：启用，0：禁用", allowableValues = {"0", "1"}, example = "1")
    private Integer enabled;


    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    private Date createTime;


}
