package com.xxx.atrs.llm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.xxx.atrs")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.xxx.atrs.common.feign")
public class LlmServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(LlmServiceApplication.class, args);
    }
}
