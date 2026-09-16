package io.github.xuefm.moli.data.account;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 创建账号请求参数
 */
@Data
@Schema(title = "CreateSysAccountRequest", description = "系统账号：创建账号请求参数")
public class CreateSysAccountRequest implements java.io.Serializable{

    private static final long serialVersionUID = 1L;

    @NotNull
    @Schema(title = "登录名", required = true, example = "admin")
    private String loginName;

    @NotNull
    @Schema(title = "登录密码", required = true, example = "123456")
    private String loginPassword;

    @NotNull
    @Schema(title = "角色列表", required = true, example = "[1,2,3]")
    private List<String> roleList;
}
