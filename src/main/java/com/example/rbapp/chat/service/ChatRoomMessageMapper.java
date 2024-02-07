package com.example.rbapp.chat.service;

import com.example.rbapp.chat.controller.api.ChatMessage;
import com.example.rbapp.config.AppMapperConfig;
import com.example.rbapp.constant.DateTimeConstant;
import com.example.rbapp.jooq.codegen.tables.records.ChatRoomMessageRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.LocalDateTime;

@Mapper(config = AppMapperConfig.class)
public interface ChatRoomMessageMapper {

    @Mapping(target = "message", source = "content")
    @Mapping(target = "chatRoomId", source = "chatId")
    ChatRoomMessageRecord mapChatMessageToRecord(ChatMessage chatMessage);

    default LocalDateTime mapInstantToLocalDateTime(Instant instant) {
        return instant.atZone(DateTimeConstant.ALMATY).toLocalDateTime();
    }
}
