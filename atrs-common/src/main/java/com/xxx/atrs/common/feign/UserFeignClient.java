package com.xxx.atrs.common.feign;

import com.xxx.atrs.common.config.CommonResponse;
import com.xxx.atrs.common.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "atrs-user-service", path = "/api/user")
public interface UserFeignClient {

    @GetMapping("/internal/{id}")
    CommonResponse<User> getUserById(@PathVariable Long id);
}
