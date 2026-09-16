package io.github.xuefm.moli.controller;

import io.github.xuefm.moli.data.account.*;
import io.github.xuefm.moli.data.web.PageData;
import io.github.xuefm.moli.data.web.Results;
import io.github.xuefm.moli.service.SysAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Author
 * @since 2026-09-15
 */
@Tag(name = "账号模块", description = "后台账号的增删改查")
@Slf4j
@RestController
@AllArgsConstructor
public class SysAccountController {
    private final SysAccountService accountInfoService;



    @Operation(summary = "账号列表", description = "按登录名模糊查询，分页返回账号列表")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "查询成功"),
            @ApiResponse(responseCode = "401", description = "未登录或 token 已失效"),
            @ApiResponse(responseCode = "403", description = "无权限（缺少 account:select）")
    })
    @PreAuthorize("hasAuthority('account:select')")
    @GetMapping("/sys/sysAccount/list")
    public Results<PageData<AccountVO>> getList(@Valid @ModelAttribute SysAccountGetListRequest request) {
        return accountInfoService.getList(request);
    }

    @Operation(summary = "账号详情", description = "根据账号 id 查询详情，包含该账号拥有的角色列表")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "查询成功"),
            @ApiResponse(responseCode = "401", description = "未登录或 token 已失效"),
            @ApiResponse(responseCode = "403", description = "无权限（缺少 account:select）")
    })
    @PreAuthorize("hasAuthority('account:select')")
    @GetMapping("/sys/sysAccount/details/{id}")
    public Results<AccountDetailsVO> getDetailsById(
            @Parameter(description = "账号 id", required = true, example = "1600000000000000001")
            @PathVariable String id) {
        return accountInfoService.getDetailsById(id);
    }

    @Operation(summary = "创建账号", description = "新增后台账号，并绑定角色")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "创建成功"),
            @ApiResponse(responseCode = "401", description = "未登录或 token 已失效"),
            @ApiResponse(responseCode = "403", description = "无权限（缺少 account:insert）"),
            @ApiResponse(responseCode = "9999", description = "业务失败，例如登录名已存在")
    })
    @PreAuthorize("hasAuthority('account:insert')")
    @PostMapping("/sys/sysAccount")
    public Results<?> createSysAccount(@Valid @RequestBody CreateSysAccountRequest request) {
        return accountInfoService.createSysAccount(request);
    }

    @Operation(summary = "更新账号", description = "修改账号绑定的角色列表")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "更新成功"),
            @ApiResponse(responseCode = "401", description = "未登录或 token 已失效"),
            @ApiResponse(responseCode = "403", description = "无权限（缺少 account:update）"),
            @ApiResponse(responseCode = "9999", description = "业务失败，例如账号不存在")
    })
    @PreAuthorize("hasAuthority('account:update')")
    @PutMapping("/sys/sysAccount")
    public Results<?> sysAccountUpdate(@Valid @RequestBody SysAccountUpdateRequest request) {
        return accountInfoService.sysAccountUpdate(request);
    }

    @Operation(summary = "删除账号", description = "删除账号，并同步删除该账号与角色的关联数据")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "删除成功"),
            @ApiResponse(responseCode = "401", description = "未登录或 token 已失效"),
            @ApiResponse(responseCode = "403", description = "无权限（缺少 account:delete）"),
            @ApiResponse(responseCode = "9999", description = "业务失败，例如账号不存在")
    })
    @PreAuthorize("hasAuthority('account:delete')")
    @DeleteMapping("/sys/sysAccount/{id}")
    public Results<String> deleteAccount(
            @Parameter(description = "账号 id", required = true) @PathVariable String id) {
        return accountInfoService.deleteAccount(id);
    }
}
