package io.github.xuefm.moli.controller;

import io.github.xuefm.moli.data.login.LoginRequest;
import io.github.xuefm.moli.data.web.Results;
import io.github.xuefm.moli.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "登录模块", description = "账号登录、登出")
@RestController
@AllArgsConstructor
public class LoginController {

    public final LoginService loginService;


    @Operation(summary = "登录", description = "校验登录名与密码，成功后返回 JWT（后续请求需放在请求头 token 中）")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "登录成功，data 为 JWT 字符串",
                    content = @Content(schema = @Schema(implementation = Results.class))),
            @ApiResponse(responseCode = "9999", description = "账号或密码错误",
                    content = @Content(schema = @Schema(implementation = Results.class)))
    })
    @PostMapping("/user/login")
    public Results<String> login(@RequestBody LoginRequest loginRequest) {
        return loginService.login(loginRequest);
    }

    @Operation(summary = "登出", description = "清除 Redis 中缓存的登录用户信息，使当前 token 失效")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "登出成功",
                    content = @Content(schema = @Schema(implementation = Results.class))),
            @ApiResponse(responseCode = "401", description = "未登录或 token 已失效")
    })
    @PostMapping("/user/logout")
    public Results<String> logout() {
        return loginService.logout();
    }


}
