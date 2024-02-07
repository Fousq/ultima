package com.example.rbapp.chat.service;

import com.example.rbapp.chat.controller.api.ChatRoomResponse;
import com.example.rbapp.config.AppMapperConfig;
import com.example.rbapp.jooq.codegen.tables.records.ChatRoomRecord;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = AppMapperConfig.class)
public interface ChatRoomMapper {

    ChatRoomResponse mapRecordToResponse(ChatRoomRecord chatRoomRecord);

    List<ChatRoomResponse> mapRecordToResponse(List<ChatRoomRecord> chatRoomRecordList);
}
