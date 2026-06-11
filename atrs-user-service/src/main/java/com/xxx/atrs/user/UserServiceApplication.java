package com.xxx.atrs.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.xxx.atrs")
@EntityScan(basePackages = "com.xxx.atrs.common.entity")
@EnableJpaRepositories(basePackages = "com.xxx.atrs.user.repository")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.xxx.atrs.common.feign")
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
