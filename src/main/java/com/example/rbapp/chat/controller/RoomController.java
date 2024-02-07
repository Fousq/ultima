package com.example.rbapp.chat.controller;

import com.example.rbapp.chat.controller.api.ChatMessage;
import com.example.rbapp.chat.controller.api.ChatRoomCreateRequest;
import com.example.rbapp.chat.controller.api.ChatRoomResponse;
import com.example.rbapp.chat.service.ChatRoomMessageService;
import com.example.rbapp.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {

    private final ChatRoomService chatRoomService;
    private final ChatRoomMessageService chatRoomMessageService;

    @GetMapping("/user/{userId}")
    public List<ChatRoomResponse> getChatRoomList(@PathVariable("userId") Long userId) {
        return chatRoomService.getChatRoomListByUser(userId);
    }

    @PostMapping
    public ChatRoomResponse create(@RequestBody ChatRoomCreateRequest chatRoomCreateRequest) {
        return chatRoomService.create(chatRoomCreateRequest);
    }

    @GetMapping("/{id}/messages")
    public List<ChatMessage> getChatMessages(@PathVariable("id") Long id) {
        return chatRoomMessageService.getChatMessagesByRoom(id);
    }
}
