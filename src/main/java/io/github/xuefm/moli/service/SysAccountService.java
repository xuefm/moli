package io.github.xuefm.moli.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.xuefm.moli.data.account.*;
import io.github.xuefm.moli.data.web.PageData;
import io.github.xuefm.moli.data.web.Results;
import io.github.xuefm.moli.entity.SysAccount;


/**
 * <p>
 *  服务类
 * 默认生成CRUD方法
 * </p>
 *
 * @author Author
 * @since 2026-09-15
 */
public interface SysAccountService extends IService<SysAccount> {

    Results<PageData<AccountVO>> getList(SysAccountGetListRequest request);

    Results<AccountDetailsVO> getDetailsById(String id);

    Results<?> createSysAccount(CreateSysAccountRequest request);

    Results<?> sysAccountUpdate(SysAccountUpdateRequest request);

    /**
     * 删除账号，同时删除账号与角色的关联数据
     */
    Results<String> deleteAccount(String id);

}
