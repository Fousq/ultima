package com.example.rbapp.bitrix.service;

import com.example.rbapp.bitrix.api.WazzupSendMessageRequest;
import com.example.rbapp.bitrix.client.WazzupClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WhatsAppWazzupService {

    private final WazzupClient wazzupClient;

    @Autowired
    @Value("${wazzup.auth.token}")
    private String authToken;

    @Autowired
    @Value("${wazzup.whatsapp.channelId}")
    private String channelId;

    @Autowired
    @Value("${wazzup.whatsapp.chatId}")
    private String chatId;

    public void sendMessage(String message) {
        String bearerToken = String.format("Bearer %s", authToken);
        WazzupSendMessageRequest request = new WazzupSendMessageRequest(channelId, chatId, "whatsapp", message);
        wazzupClient.sendMessage(bearerToken, request);
    }
}
