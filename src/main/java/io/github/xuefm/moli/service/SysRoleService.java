package io.github.xuefm.moli.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.xuefm.moli.data.sys.sysrole.CreateSysRoleRequest;
import io.github.xuefm.moli.data.sys.sysrole.SysRoleGetListRequest;
import io.github.xuefm.moli.data.sys.sysrole.SysRoleVO;
import io.github.xuefm.moli.data.sys.sysrole.UpdateSysRoleRequest;
import io.github.xuefm.moli.data.web.PageData;
import io.github.xuefm.moli.data.web.Results;
import io.github.xuefm.moli.entity.SysRole;

import java.util.List;

/**
 * <p>
 * 系统角色 服务类
 * 默认生成CRUD方法
 * </p>
 *
 * @author Author
 * @since 2026-09-15
 */
public interface SysRoleService extends IService<SysRole> {

    Results<PageData<SysRoleVO>> getList(SysRoleGetListRequest sysRoleGetListRequest);

    Results<List<SysRole>> getAll();

    Results<String> createRole(CreateSysRoleRequest createSysRoleRequest);

    Results<String> updateRole(UpdateSysRoleRequest updateSysRoleRequest);

    /**
     * 删除角色，同时删除角色与资源的关联数据
     */
    Results<String> deleteRole(String id);
}
