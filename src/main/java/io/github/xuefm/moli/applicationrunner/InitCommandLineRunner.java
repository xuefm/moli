package io.github.xuefm.moli.applicationrunner;

import io.github.xuefm.moli.data.system.SystemConfig;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Order(3)
@Component
@AllArgsConstructor
public class InitCommandLineRunner implements ApplicationRunner {
    private final SystemConfig systemConfig;

    @Override
    @SneakyThrows
    public void run(ApplicationArguments args) throws Exception {
        log.info("项目启动时执行");
        systemConfig.setReady(true);
        new Thread(()->{
            for (int i = 10; i > 0; i--) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("准备中******{}",i);
            }
            systemConfig.setReady(true);
            log.info("系统xxx依赖加载完成");
        }).start();

    }
}
