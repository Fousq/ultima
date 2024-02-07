package com.example.rbapp.chat.controller;

import com.example.rbapp.chat.controller.api.ChatMessage;
import com.example.rbapp.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RoomMessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/send")
    public void message(@Payload ChatMessage message) {
        chatMessageService.create(message);
        messagingTemplate.convertAndSendToUser(String.valueOf(message.chatId()), "/queue/messages", message);
    }
}
