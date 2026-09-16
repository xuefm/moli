package io.github.xuefm.moli.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统角色 系统资源 关联表(多对多)
 * </p>
 *
 * @author Author
 * @since 2026-09-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysRoleResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    @NotNull(message = "ID属性值为空")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
    * 角色id
    */
    private String roleId;

    /**
    * 资源id
    */
    private String resourceId;

    /**
    * 逻辑删除标识-0:未删除,1:已删除
    */
//    @TableLogic
    private Integer deleted;

    /**
    * 创建时间
    */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
    * 创建人
    */
    @TableField(fill = FieldFill.INSERT)
    private String createById;

    /**
    * 更新时间
    */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
    * 更新人
    */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateById;


}
