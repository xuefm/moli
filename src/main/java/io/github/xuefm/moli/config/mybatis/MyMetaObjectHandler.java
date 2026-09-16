package io.github.xuefm.moli.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * mybatis自动填充配置
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("开始插入填充 ....");
        Date date = new Date();
        String user = "root";
        this.strictInsertFill(metaObject, "createTime", () -> date, Date.class); // 起始版本 3.3.3(推荐)
        this.strictInsertFill(metaObject, "createById", () -> user, String.class); // 起始版本 3.3.3(推荐)
        this.strictInsertFill(metaObject, "updateTime", () -> date, Date.class); // 起始版本 3.3.3(推荐)
        this.strictInsertFill(metaObject, "updateById", () -> user, String.class); // 起始版本 3.3.3(推荐)
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("开始更新填充 ....");
        Date date = new Date();
        String user = "root";
        this.strictInsertFill(metaObject, "updateTime", () -> date, Date.class); // 起始版本 3.3.3(推荐)
        this.strictInsertFill(metaObject, "updateById", () -> user, String.class); // 起始版本 3.3.3(推荐)
    }
}
