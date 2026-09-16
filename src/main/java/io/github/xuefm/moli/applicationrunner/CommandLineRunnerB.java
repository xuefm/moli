package io.github.xuefm.moli.applicationrunner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Order(2)
@Component
public class CommandLineRunnerB implements CommandLineRunner {
    @Override
    public void run(String... args) {
        log.info("初始化：CommandLineRunnerB");
    }
}
