package io.github.xuefm.moli.config;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//@Configuration
public class MinIoConfig {

    @Bean
    public MinioClient minioClient(){
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint("http://127.0.0.1:9000")
                        .credentials("trnsj7iwLFpeuFgG", "mGVPaFALAkwfAqFs9scfwIpvAaJNX1EN")
                        .build();
        return minioClient;
    }
}
