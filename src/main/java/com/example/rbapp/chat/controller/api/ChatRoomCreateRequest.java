package com.example.rbapp.chat.controller.api;

public record ChatRoomCreateRequest(Long senderId, Long receiverId, String title) {
}
