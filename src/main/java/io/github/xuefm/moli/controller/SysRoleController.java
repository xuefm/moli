package io.github.xuefm.moli.controller;


import io.github.xuefm.moli.data.sys.sysrole.CreateSysRoleRequest;
import io.github.xuefm.moli.data.sys.sysrole.SysRoleGetListRequest;
import io.github.xuefm.moli.data.sys.sysrole.SysRoleVO;
import io.github.xuefm.moli.data.sys.sysrole.UpdateSysRoleRequest;
import io.github.xuefm.moli.data.web.PageData;
import io.github.xuefm.moli.data.web.Results;
import io.github.xuefm.moli.entity.SysRole;
import io.github.xuefm.moli.service.SysRoleService;
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

import java.util.List;

/**
 * <p>
 * 系统角色 前端控制器
 * </p>
 *
 * @author Author
 * @since 2026-09-15
 */
@Tag(name = "系统角色", description = "角色管理及角色资源分配")
@Slf4j
@RestController
@AllArgsConstructor
public class SysRoleController {
    private final SysRoleService sysRoleService;

    @Operation(summary = "角色列表", description = "按角色名模糊查询，分页返回")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "查询成功"),
            @ApiResponse(responseCode = "401", description = "未登录或 token 已失效")
    })
    @PreAuthorize("hasAuthority('role:select')")
    @GetMapping("/sys/sysRole/list")
    public Results<PageData<SysRoleVO>> getList(@Valid @ModelAttribute SysRoleGetListRequest sysRoleGetListRequest) {
        return sysRoleService.getList(sysRoleGetListRequest);
    }

    @Operation(summary = "全部角色", description = "不分页，返回全部角色，常用于下拉选择")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "查询成功"),
            @ApiResponse(responseCode = "401", description = "未登录或 token 已失效")
    })
    @PreAuthorize("hasAuthority('role:select')")
    @GetMapping("/sys/sysRole/all")
    public Results<List<SysRole>> getAll() {
        return sysRoleService.getAll();
    }

    @Operation(summary = "创建角色", description = "新增角色，并一次性绑定资源（权限）列表")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "创建成功"),
            @ApiResponse(responseCode = "401", description = "未登录或 token 已失效"),
            @ApiResponse(responseCode = "9999", description = "业务失败，例如角色名已存在")
    })
    @PreAuthorize("hasAuthority('role:insert')")
    @PostMapping("/sys/sysRole")
    public Results<String> createRole(@Valid @RequestBody CreateSysRoleRequest createSysRoleRequest) {
        return sysRoleService.createRole(createSysRoleRequest);
    }

    @Operation(summary = "修改角色", description = "修改角色名称，并全量覆盖其资源（权限）列表")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "修改成功"),
            @ApiResponse(responseCode = "401", description = "未登录或 token 已失效"),
            @ApiResponse(responseCode = "9999", description = "业务失败，例如角色不存在")
    })
    @PreAuthorize("hasAuthority('role:update')")
    @PutMapping("/sys/sysRole")
    public Results<String> updateRole(@Valid @RequestBody UpdateSysRoleRequest updateSysRoleRequest) {
        return sysRoleService.updateRole(updateSysRoleRequest);
    }

    @Operation(summary = "删除角色", description = "删除角色，并同步删除该角色与资源的关联数据")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "删除成功"),
            @ApiResponse(responseCode = "401", description = "未登录或 token 已失效"),
            @ApiResponse(responseCode = "403", description = "无权限（缺少 role:delete）"),
            @ApiResponse(responseCode = "9999", description = "业务失败，例如角色不存在")
    })
    @PreAuthorize("hasAuthority('role:delete')")
    @DeleteMapping("/sys/sysRole/{id}")
    public Results<String> deleteRole(
            @Parameter(description = "角色 id", required = true) @PathVariable String id) {
        return sysRoleService.deleteRole(id);
    }
}
