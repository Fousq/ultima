package com.example.rbapp.notification.controller;

import com.example.rbapp.notification.controller.api.CreateNotificationRequest;
import com.example.rbapp.notification.controller.api.UserNotificationResponse;
import com.example.rbapp.notification.service.NotificationService;
import com.example.rbapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    @GetMapping("/user")
    public List<UserNotificationResponse> getUserNotifications(@RequestHeader(AUTHORIZATION) String token) {
        Long userId = userService.loadUserByToken(token).getId();
        return notificationService.getUserNotification(userId);
    }

    @PostMapping
    public ResponseEntity<?> createNotification(@RequestBody CreateNotificationRequest request) {
        notificationService.create(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}/user")
    public ResponseEntity<?> deleteUserNotification(@PathVariable("id") Long id,
                                                    @RequestHeader(AUTHORIZATION) String token) {
        Long userId = userService.loadUserByToken(token).getId();
        notificationService.delete(id, userId);
        return ResponseEntity.ok().build();
    }
}
