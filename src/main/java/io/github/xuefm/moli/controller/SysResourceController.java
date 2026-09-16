package io.github.xuefm.moli.controller;


import io.github.xuefm.moli.data.sys.sysresource.*;
import io.github.xuefm.moli.data.web.PageData;
import io.github.xuefm.moli.data.web.Results;
import io.github.xuefm.moli.service.SysResourceService;
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
 * 系统资源 前端控制器
 * </p>
 *
 * @author Author
 * @since 2026-09-15
 */
@Tag(name = "系统资源", description = "菜单 / 接口资源（权限点）管理")
@Slf4j
@RestController
@AllArgsConstructor
public class SysResourceController {
    private final SysResourceService sysResourceService;


    @Operation(summary = "资源列表", description = "按资源标题模糊查询，分页返回")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "查询成功"),
            @ApiResponse(responseCode = "401", description = "未登录或 token 已失效")
    })
    @PreAuthorize("hasAuthority('resource:select')")
    @GetMapping("/sys/sysResource/list")
    public Results<PageData<SysResourceVO>> getList(@Valid @ModelAttribute SysResourceGetListRequest sysResourceGetListRequest) {
        return sysResourceService.getList(sysResourceGetListRequest);
    }

    @Operation(summary = "获取全部资源（树）", description = "一次性返回全部资源，组装成树形结构，常用于权限树展示")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "查询成功"),
            @ApiResponse(responseCode = "401", description = "未登录或 token 已失效")
    })
    @PreAuthorize("hasAuthority('resource:select')")
    @GetMapping("/sys/sysResource/treeAll")
    public Results<List<SysResourceTreeVO>> treeAll() {
        return sysResourceService.treeAll();
    }

    @Operation(summary = "逐级获取资源", description = "根据上级 id 获取下一级资源；不传 superiorId 时返回顶级资源")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "查询成功"),
            @ApiResponse(responseCode = "401", description = "未登录或 token 已失效")
    })
    @GetMapping("/sys/sysResource/getStepByStep")
    public Results<List<SysResourceVO>> getStepByStep(@Valid @ModelAttribute GetStepByStepRequest getStepByStepRequest) {
        return sysResourceService.getStepByStep(getStepByStepRequest);
    }

    @Operation(summary = "添加资源权限", description = "新增菜单（type=0）或接口（type=1）资源")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "添加成功"),
            @ApiResponse(responseCode = "400", description = "参数校验失败"),
            @ApiResponse(responseCode = "401", description = "未登录或 token 已失效"),
            @ApiResponse(responseCode = "9999", description = "业务失败，例如资源 code 重复")
    })
    @PreAuthorize("hasAuthority('resource:insert')")
    @PostMapping("/sys/sysResource")
    public Results<String> addResource(@Valid @RequestBody AddResourceRequest addResourceRequest) {
        return sysResourceService.addResource(addResourceRequest);
    }

    @Operation(summary = "修改资源权限", description = "修改资源标题")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "修改成功"),
            @ApiResponse(responseCode = "400", description = "参数校验失败"),
            @ApiResponse(responseCode = "401", description = "未登录或 token 已失效"),
            @ApiResponse(responseCode = "9999", description = "业务失败，例如资源不存在")
    })
    @PreAuthorize("hasAuthority('resource:update')")
    @PutMapping("/sys/sysResource")
    public Results<String> updateResource(@Valid @RequestBody UpdateResourceRequest updateResourceRequest) {
        return sysResourceService.updateResource(updateResourceRequest);
    }

    @Operation(summary = "获取该角色拥有资源列表", description = "查询指定角色已绑定的资源 id 列表，用于回显权限树")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "查询成功，data 为资源 id 列表"),
            @ApiResponse(responseCode = "401", description = "未登录或 token 已失效")
    })
    @PreAuthorize("hasAuthority('resource:select')")
    @GetMapping("/sys/sysResource/byRoleId/{id}")
    public Results<List<String>> getByRoleId(
            @Parameter(description = "角色 id", required = true, example = "1600000000000000001")
            @PathVariable String id) {
        return sysResourceService.getByRoleId(id);
    }

    @Operation(summary = "删除资源", description = "删除资源，并同步删除该资源与角色的关联数据（不会级联删除下级资源）")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "删除成功"),
            @ApiResponse(responseCode = "401", description = "未登录或 token 已失效"),
            @ApiResponse(responseCode = "403", description = "无权限（缺少 resource:delete）"),
            @ApiResponse(responseCode = "9999", description = "业务失败，例如资源不存在")
    })
    @PreAuthorize("hasAuthority('resource:delete')")
    @DeleteMapping("/sys/sysResource/{id}")
    public Results<String> deleteResource(
            @Parameter(description = "资源 id", required = true) @PathVariable String id) {
        return sysResourceService.deleteResource(id);
    }

}
