package com.example.rbapp.notification.service;

import com.example.rbapp.jooq.codegen.tables.UserNotification;
import com.example.rbapp.jooq.codegen.tables.records.NotificationRecord;
import com.example.rbapp.jooq.codegen.tables.records.UserNotificationRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.InsertReturningStep;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.rbapp.jooq.codegen.Tables.NOTIFICATION;
import static com.example.rbapp.jooq.codegen.Tables.USER_NOTIFICATION;


@Repository
@RequiredArgsConstructor
public class NotificationRepository {

    private final DSLContext dslContext;

    public List<NotificationRecord> findAllUserNotifications(Long userId) {
        return dslContext.select(NOTIFICATION.asterisk()).from(NOTIFICATION)
                .innerJoin(USER_NOTIFICATION).on(USER_NOTIFICATION.NOTIFICATION_ID.eq(NOTIFICATION.ID))
                .where(USER_NOTIFICATION.USER_ID.eq(userId))
                .fetchInto(NotificationRecord.class);
    }

    public Long create(NotificationRecord notification) {
        return dslContext.insertInto(NOTIFICATION)
                .set(NOTIFICATION.CONTENT, notification.getContent())
                .set(NOTIFICATION.TITLE, notification.getTitle())
                .returningResult(NOTIFICATION.ID)
                .fetchSingleInto(Long.class);
    }

    public void createUserNotifications(List<UserNotificationRecord> userNotifications) {
        var insertUserNotifications = userNotifications.stream().map(userNotificationRecord ->
                dslContext.insertInto(USER_NOTIFICATION)
                        .set(USER_NOTIFICATION.NOTIFICATION_ID, userNotificationRecord.getNotificationId())
                        .set(USER_NOTIFICATION.USER_ID, userNotificationRecord.getUserId())
                        .onConflictDoNothing()
        ).toList();
        dslContext.batch(insertUserNotifications).execute();
    }

    public void deleteUserNotification(Long id, Long userId) {
        dslContext.deleteFrom(USER_NOTIFICATION)
                .where(USER_NOTIFICATION.USER_ID.eq(userId)).and(USER_NOTIFICATION.NOTIFICATION_ID.eq(id))
                .execute();
    }

    public int countUserNotificationsById(Long id) {
        return dslContext.fetchCount(
                USER_NOTIFICATION, USER_NOTIFICATION.NOTIFICATION_ID.eq(id)
        );
    }

    public void delete(Long id) {
        dslContext.deleteFrom(NOTIFICATION)
                .where(NOTIFICATION.ID.eq(id))
                .execute();
    }
}
