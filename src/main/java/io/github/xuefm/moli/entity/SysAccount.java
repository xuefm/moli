package io.github.xuefm.moli.entity;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Author
 * @since 2026-09-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "ID属性值为空")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String loginName;

    private String loginPassword;

    /**
     * 最后登录时间
     */
    private Long loginTime;

    /**
     * 1:启用
     */
    private Integer enabled;

    /**
     * 逻辑删除标识-0:未删除,1:已删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建时间
     */
    @TableField( fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建人
     */
    @TableField( fill = FieldFill.INSERT)
    private String createById;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 更新人
     */
    @TableField( fill = FieldFill.INSERT_UPDATE)
    private String updateById;



}
