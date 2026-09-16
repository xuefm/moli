package io.github.xuefm.moli.data.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 账号列表响应数据
 */
@Data
@Schema(description = "账号列表响应数据")
public class AccountVO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(title = "账号 id", example = "1600000000000000001")
    private String id;

    @Schema(title = "登录名", example = "admin")
    private String loginName;

    /**
     * 1:启用
     */
    @Schema(title = "是否启用", description = "1：启用，0：禁用", allowableValues = {"0", "1"}, example = "1")
    private Integer enabled;

    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    private Date createTime;


}
