package com.xxx.atrs.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.xxx.atrs")
@EntityScan(basePackages = "com.xxx.atrs.common.entity")
@EnableJpaRepositories(basePackages = "com.xxx.atrs.notification.repository")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.xxx.atrs.common.feign")
public class NotificationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}
