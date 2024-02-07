package com.example.rbapp.bitrix.client;

import com.example.rbapp.bitrix.api.WazzupSendMessageRequest;
import com.example.rbapp.bitrix.api.WazzupSendMessageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.PostMapping;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@FeignClient("wazzup")
public interface WazzupClient {

    @PostMapping("/v3/message")
    WazzupSendMessageResponse sendMessage(@Header(AUTHORIZATION) String authToken, WazzupSendMessageRequest request);
}
