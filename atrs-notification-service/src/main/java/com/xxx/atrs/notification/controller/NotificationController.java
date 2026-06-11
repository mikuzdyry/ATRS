package com.xxx.atrs.notification.controller;

import com.xxx.atrs.common.config.CommonResponse;
import com.xxx.atrs.notification.entity.Notification;
import com.xxx.atrs.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    public CommonResponse<Void> send(@RequestBody Map<String, Object> body) {
        Long userId = body.get("userId") != null
                ? ((Number) body.get("userId")).longValue() : null;
        String title = (String) body.get("title");
        String content = (String) body.get("content");
        String type = (String) body.getOrDefault("type", "SYSTEM");
        notificationService.send(userId, title, content, type);
        return CommonResponse.success();
    }

    @GetMapping("/list")
    public CommonResponse<List<Notification>> list(@RequestHeader("X-User-Id") Long userId) {
        return CommonResponse.success(notificationService.listByUser(userId));
    }

    @GetMapping("/unread-count")
    public CommonResponse<Long> unreadCount(@RequestHeader("X-User-Id") Long userId) {
        return CommonResponse.success(notificationService.unreadCount(userId));
    }

    @PutMapping("/read/{id}")
    public CommonResponse<String> markRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return CommonResponse.success("ok");
    }
}
