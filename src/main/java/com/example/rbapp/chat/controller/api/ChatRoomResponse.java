package com.example.rbapp.chat.controller.api;

public record ChatRoomResponse(Long id, String title, Long senderId, Long receiverId) {
}
