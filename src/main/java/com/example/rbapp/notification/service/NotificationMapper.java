package com.example.rbapp.notification.service;

import com.example.rbapp.config.AppMapperConfig;
import com.example.rbapp.jooq.codegen.tables.records.NotificationRecord;
import com.example.rbapp.notification.controller.api.CreateNotificationRequest;
import com.example.rbapp.notification.controller.api.UserNotificationResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = AppMapperConfig.class)
public interface NotificationMapper {


    List<UserNotificationResponse> mapRecordToUserNotificationResponse(List<NotificationRecord> notifications);

    NotificationRecord mapCreateRequestToRecord(CreateNotificationRequest request);
}
