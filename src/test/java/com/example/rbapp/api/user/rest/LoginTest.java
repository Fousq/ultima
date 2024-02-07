package com.example.rbapp.api.user.rest;

import com.example.rbapp.api.AbstractComponentTest;
import com.example.rbapp.user.controller.api.LoginRequest;
import com.example.rbapp.user.controller.api.LoginResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class LoginTest extends AbstractComponentTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldLogin() throws IOException, InterruptedException {
        String userStr = generateString();
        createUser(userStr, "/student"); // creating student user

        var loginRequest = new LoginRequest(userStr, userStr);
        String body = objectMapper.writeValueAsString(loginRequest);
        HttpRequest request = createRequest("/user/login")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        var loginResponse = objectMapper.readValue(response.body(), LoginResponse.class);
        assertFalse(loginResponse.token().isBlank()); // not blank
        assertFalse(loginResponse.grants().isEmpty()); // not empty
    }
}
