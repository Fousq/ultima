package com.example.rbapp.chat.service.recordmapper;

import com.example.rbapp.chat.controller.api.ChatMessage;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.example.rbapp.jooq.codegen.Tables.*;

@Component
public class ChatMessageRecordMapper implements RecordMapper<Record, ChatMessage> {

    @Override
    public ChatMessage map(Record record) {
        Long chatRoomId = record.getValue(CHAT_ROOM.ID);
        Long senderId = record.getValue(CHAT_ROOM_MESSAGE.SENDER_ID);
        LocalDateTime sendAt = record.getValue(CHAT_ROOM_MESSAGE.SEND_AT);
        String content = record.getValue(CHAT_ROOM_MESSAGE.MESSAGE);
        String senderName = record.getValue(APP_USER.USERNAME);

        return new ChatMessage(chatRoomId, senderId, senderName, content, sendAt.toInstant(ZoneOffset.UTC));
    }
}
