package com.example.rbapp.notification.controller.api;

import java.util.List;

public record CreateNotificationRequest(String title, String content, List<Long> userIdList, List<String> roleGroup) {
}
