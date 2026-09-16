package io.github.xuefm.moli.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.github.xuefm.moli.entity.SysAccount;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Author
 * @since 2026-09-15
 */
@Mapper
public interface SysAccountMapper extends BaseMapper<SysAccount> {

    default SysAccount getByLoginName(String loginName) {
        return this.selectOne(Wrappers.lambdaQuery(SysAccount.class)
                .eq(SysAccount::getLoginName, loginName));
    }

}
