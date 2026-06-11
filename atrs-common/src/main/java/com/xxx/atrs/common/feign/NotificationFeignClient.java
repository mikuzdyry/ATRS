package com.xxx.atrs.common.feign;

import com.xxx.atrs.common.config.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "atrs-notification-service", path = "/api/notification")
public interface NotificationFeignClient {

    @PostMapping("/send")
    CommonResponse<Void> sendNotification(@RequestBody Map<String, Object> notification);
}
