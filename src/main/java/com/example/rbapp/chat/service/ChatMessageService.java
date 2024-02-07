package com.example.rbapp.chat.service;

import com.example.rbapp.chat.controller.api.ChatMessage;
import com.example.rbapp.chat.exception.ChatRoomNotExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomMessageService chatRoomMessageService;
    private final ChatRoomService chatRoomService;

    public void create(ChatMessage chatMessage) {
        if (chatRoomService.notExists(chatMessage.chatId())) {
            throw new ChatRoomNotExistsException("Room " + chatMessage.chatId() + " doesn't exist");
        }
        chatRoomMessageService.create(chatMessage);
    }
}
