package com.example.rbapp.chat.service.impl;

import com.example.rbapp.chat.service.ChatRoomDeleteProcessor;
import com.example.rbapp.chat.service.ChatRoomMessageService;
import com.example.rbapp.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomDeleteProcessorImpl implements ChatRoomDeleteProcessor {

    private final ChatRoomService chatRoomService;
    private final ChatRoomMessageService chatRoomMessageService;

    @Override
    public void deleteUserChatRooms(Long userId) {
        if (chatRoomService.userRoomExists(userId)) {
            chatRoomMessageService.deleteByUserId(userId);
            chatRoomService.deleteAllUserChats(userId);
        }
    }
}
