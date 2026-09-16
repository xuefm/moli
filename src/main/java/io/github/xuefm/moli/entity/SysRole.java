package io.github.xuefm.moli.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统角色
 * </p>
 *
 * @author Author
 * @since 2026-09-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(description = "系统角色")
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    @NotNull(message = "ID属性值为空")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(title = "角色 id", example = "1600000000000000001")
    private String id;

    /**
    * 角色名
    */
    @Schema(title = "角色名", required = true, example = "xx管理员")
    private String title;

    /**
     * 角色名code
     */
    @Schema(title = "角色code", required = true, example = "xx_admin")
    private String code;

    /**
    * 是否启用
    */
    @Schema(title = "是否启用", description = "1：启用，0：禁用", allowableValues = {"0", "1"}, example = "1")
    private Integer enabled;

    /**
    * 逻辑删除标识-0:未删除,1:已删除
    */
    @TableLogic
    @Schema(hidden = true)
    private Integer deleted;

    /**
    * 创建时间
    */
    @TableField(fill = FieldFill.INSERT)
    @Schema(title = "创建时间")
    private Date createTime;

    /**
    * 创建人
    */
    @TableField(fill = FieldFill.INSERT)
    @Schema(hidden = true)
    private String createById;

    /**
    * 更新时间
    */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(title = "更新时间")
    private Date updateTime;

    /**
    * 更新人
    */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(hidden = true)
    private String updateById;


}
