package com.example.rbapp.chat.service;

import com.example.rbapp.chat.controller.api.ChatMessage;
import com.example.rbapp.jooq.codegen.tables.records.ChatRoomMessageRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomMessageMapper chatRoomMessageMapper;

    public List<ChatMessage> getChatMessagesByRoom(Long roomId) {
        return chatMessageRepository.findAllByRoomId(roomId);
    }

    public void create(ChatMessage chatMessage) {
        ChatRoomMessageRecord record = chatRoomMessageMapper.mapChatMessageToRecord(chatMessage);
        chatMessageRepository.create(record);
    }

    public void deleteByUserId(Long userId) {
        chatMessageRepository.deleteAllByUserId(userId);
    }
}
