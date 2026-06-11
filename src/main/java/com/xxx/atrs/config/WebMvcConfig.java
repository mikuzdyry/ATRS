package com.xxx.atrs.config;

import com.xxx.atrs.interceptor.AdminInterceptor;
import com.xxx.atrs.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/user/profile", "/booking/**", "/order/**")
                .excludePathPatterns("/static/**");

        registry.addInterceptor(new AdminInterceptor())
                .addPathPatterns("/admin/**");
    }
}
