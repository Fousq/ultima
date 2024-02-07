package com.example.rbapp.notification.service;

import com.example.rbapp.jooq.codegen.tables.records.NotificationRecord;
import com.example.rbapp.jooq.codegen.tables.records.UserNotificationRecord;
import com.example.rbapp.notification.controller.api.CreateNotificationRequest;
import com.example.rbapp.notification.controller.api.UserNotificationResponse;
import com.example.rbapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserService userService;

    public List<UserNotificationResponse> getUserNotification(Long userId) {
        List<NotificationRecord> notifications = notificationRepository.findAllUserNotifications(userId);
        return notificationMapper.mapRecordToUserNotificationResponse(notifications);
    }

    @Transactional
    public void create(CreateNotificationRequest request) {
        NotificationRecord notificationRecord = notificationMapper.mapCreateRequestToRecord(request);
        Long id = notificationRepository.create(notificationRecord);
        List<Long> userIdListByRoles = userService.getUserIdListForRoles(request.roleGroup());
        List<Long> notificationUserIdList = ListUtils.union(userIdListByRoles, request.userIdList());
        List<UserNotificationRecord> userNotifications = notificationUserIdList.stream().map(userId -> {
            UserNotificationRecord userNotificationRecord = new UserNotificationRecord();
            userNotificationRecord.setNotificationId(id);
            userNotificationRecord.setUserId(userId);
            return userNotificationRecord;
        }).toList();
        notificationRepository.createUserNotifications(userNotifications);
    }

    @Transactional
    public void delete(Long id, Long userId) {
        notificationRepository.deleteUserNotification(id, userId);
        if (notificationRepository.countUserNotificationsById(id) == 0) {
            notificationRepository.delete(id);
        }
    }
}
