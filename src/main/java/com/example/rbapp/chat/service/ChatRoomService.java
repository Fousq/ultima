package com.example.rbapp.chat.service;

import com.example.rbapp.api.exception.NotFoundException;
import com.example.rbapp.chat.controller.api.ChatRoomCreateRequest;
import com.example.rbapp.chat.controller.api.ChatRoomResponse;
import com.example.rbapp.jooq.codegen.tables.records.ChatRoomRecord;
import com.example.rbapp.jooq.codegen.tables.records.ChatRoomUserRecord;
import com.example.rbapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMapper chatRoomMapper;
    private final UserService userService;

    public List<ChatRoomResponse> getChatRoomListByUser(Long userId) {
        List<ChatRoomRecord> chatRoomRecords = chatRoomRepository.findAllByUserId(userId);
        List<Long> ids = chatRoomRecords.stream().map(ChatRoomRecord::getId).toList();
        var roomUserMap = chatRoomRepository.findRoomReceiverIdByIdsAndNotUserId(ids, userId);

        return chatRoomRecords.stream().map(chatRoomRecord -> {
            Long id = chatRoomRecord.getId();
            return new ChatRoomResponse(id, chatRoomRecord.getTitle(), userId, roomUserMap.get(id));
        }).toList();
    }

    public ChatRoomResponse create(ChatRoomCreateRequest chatRoomCreateRequest) {
        Long chatRoomId = chatRoomRepository.createRoom(chatRoomCreateRequest.title());
        String receiverFullName = userService.getUserFullName(chatRoomCreateRequest.receiverId());
        String senderFullName = userService.getUserFullName(chatRoomCreateRequest.senderId());
        chatRoomRepository.addUserToRoom(receiverFullName, chatRoomId, chatRoomCreateRequest.senderId());
        chatRoomRepository.addUserToRoom(senderFullName, chatRoomId, chatRoomCreateRequest.receiverId());
        return getById(chatRoomId, chatRoomCreateRequest.senderId());
    }

    private ChatRoomResponse getById(Long chatRoomId, Long userId) {
        return chatRoomRepository.findByIdAndUserId(chatRoomId, userId)
                .map(chatRoomMapper::mapRecordToResponse)
                .orElseThrow(() -> new NotFoundException("Chat room not found"));
    }

    public boolean notExists(Long id) {
        return !chatRoomRepository.isExistsById(id);
    }

    public boolean userRoomExists(Long userId) {
        return chatRoomRepository.existsByUserId(userId);
    }

    public void deleteAllUserChats(Long userId) {
        chatRoomRepository.deleteAllByUserId(userId);
    }
}
