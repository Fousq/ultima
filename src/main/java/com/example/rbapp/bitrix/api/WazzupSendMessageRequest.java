package com.example.rbapp.bitrix.api;

public record WazzupSendMessageRequest(String channelId, String chatId, String chatType, String text) {
}
