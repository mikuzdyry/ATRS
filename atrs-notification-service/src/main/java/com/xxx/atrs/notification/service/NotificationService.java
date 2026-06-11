package com.xxx.atrs.notification.service;

import com.xxx.atrs.notification.entity.Notification;
import com.xxx.atrs.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void send(Long userId, String title, String content, String type) {
        Notification notification = Notification.builder()
                .userId(userId)
                .title(title)
                .content(content)
                .type(type)
                .build();
        notificationRepository.save(notification);
        log.info("通知已发送: userId={}, title={}", userId, title);
    }

    public List<Notification> listByUser(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public long unreadCount(Long userId) {
        return notificationRepository.countByUserIdAndReadFlagFalse(userId);
    }

    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId)
                .ifPresent(n -> { n.setReadFlag(true); notificationRepository.save(n); });
    }
}
