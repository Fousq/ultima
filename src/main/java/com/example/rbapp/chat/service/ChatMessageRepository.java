package com.example.rbapp.chat.service;

import com.example.rbapp.chat.controller.api.ChatMessage;
import com.example.rbapp.chat.service.recordmapper.ChatMessageRecordMapper;
import com.example.rbapp.jooq.codegen.tables.records.ChatRoomMessageRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.rbapp.jooq.codegen.Tables.*;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepository {

    private final DSLContext dslContext;

    private final ChatMessageRecordMapper chatMessageRecordMapper;

    public List<ChatMessage> findAllByRoomId(Long roomId) {
        return dslContext.select().from(CHAT_ROOM_MESSAGE)
                .innerJoin(CHAT_ROOM).on(CHAT_ROOM.ID.eq(CHAT_ROOM_MESSAGE.CHAT_ROOM_ID))
                .innerJoin(APP_USER).on(APP_USER.ID.eq(CHAT_ROOM_MESSAGE.SENDER_ID))
                .where(CHAT_ROOM_MESSAGE.CHAT_ROOM_ID.eq(roomId))
                .orderBy(CHAT_ROOM_MESSAGE.SEND_AT)
                .fetch(chatMessageRecordMapper);
    }

    public void create(ChatRoomMessageRecord record) {
        dslContext.insertInto(CHAT_ROOM_MESSAGE)
                .set(CHAT_ROOM_MESSAGE.MESSAGE, record.getMessage())
                .set(CHAT_ROOM_MESSAGE.SEND_AT, record.getSendAt())
                .set(CHAT_ROOM_MESSAGE.SENDER_ID, record.getSenderId())
                .set(CHAT_ROOM_MESSAGE.CHAT_ROOM_ID, record.getChatRoomId())
                .execute();
    }

    public void deleteAllByUserId(Long userId) {
        dslContext.transaction(conf -> {
            DSLContext context = DSL.using(conf);
            Long chatRoomId = context.select(CHAT_ROOM_USER.CHAT_ROOM_ID)
                    .from(CHAT_ROOM_USER)
                    .where(CHAT_ROOM_USER.USER_ID.eq(userId))
                    .fetchSingleInto(Long.class);
            context.deleteFrom(CHAT_ROOM_MESSAGE)
                    .where(CHAT_ROOM_MESSAGE.CHAT_ROOM_ID.eq(chatRoomId))
                    .execute();
        });
    }
}
