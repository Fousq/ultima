package com.example.rbapp.bitrix.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WazzupSendMessageResponse(String messageId, String chatId) {
}
