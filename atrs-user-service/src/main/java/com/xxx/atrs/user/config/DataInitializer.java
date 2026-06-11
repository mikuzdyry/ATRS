package com.xxx.atrs.user.config;

import com.xxx.atrs.common.entity.User;
import com.xxx.atrs.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("用户数据已存在，跳过初始化");
            return;
        }

        User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .realName("系统管理员")
                .role("ADMIN")
                .build();
        userRepository.save(admin);

        User test = User.builder()
                .username("test")
                .password(passwordEncoder.encode("123456"))
                .realName("测试用户")
                .role("USER")
                .build();
        userRepository.save(test);

        log.info("初始化用户数据完成: admin, test");
    }
}
