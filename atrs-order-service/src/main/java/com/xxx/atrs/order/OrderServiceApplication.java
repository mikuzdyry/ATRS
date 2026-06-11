package com.xxx.atrs.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.xxx.atrs")
@EntityScan(basePackages = "com.xxx.atrs.common.entity")
@EnableJpaRepositories(basePackages = "com.xxx.atrs.order.repository")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.xxx.atrs.common.feign")
@EnableScheduling
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
