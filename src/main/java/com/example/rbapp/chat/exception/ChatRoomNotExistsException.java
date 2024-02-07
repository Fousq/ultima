package com.example.rbapp.chat.exception;

public class ChatRoomNotExistsException extends RuntimeException {

    public ChatRoomNotExistsException(String message) {
        super(message);
    }
}
