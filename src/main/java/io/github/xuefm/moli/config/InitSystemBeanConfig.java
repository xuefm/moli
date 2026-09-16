package io.github.xuefm.moli.config;


import io.github.xuefm.moli.data.system.SystemConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitSystemBeanConfig {

    @Bean
    public SystemConfig getSystemConfig(){
        return new SystemConfig();
    }
}
