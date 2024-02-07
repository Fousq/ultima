package com.example.rbapp.chat.controller.api;

import java.time.Instant;


public record ChatMessage(Long chatId,
                          Long senderId,
                          String senderName,
                          String content,
                          Instant sendAt) {

}
