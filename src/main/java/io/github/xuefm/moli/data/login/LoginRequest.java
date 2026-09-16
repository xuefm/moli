package io.github.xuefm.moli.data.login;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 登录请求参数
 */
@Data
@Schema(title = "LoginRequest", description = "系统账号：登录请求参数")
public class LoginRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Schema(title = "登录名", required = true, example = "admin")
    private String loginName;

    @NotNull
    @Schema(title = "登录密码", required = true, example = "123456")
    private String loginPassword;
}
