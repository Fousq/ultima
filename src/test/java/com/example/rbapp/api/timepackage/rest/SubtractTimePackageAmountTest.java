package com.example.rbapp.api.timepackage.rest;

import com.example.rbapp.api.AbstractComponentTest;
import com.example.rbapp.timepackage.controller.api.TimePackageResponse;
import com.example.rbapp.timepackage.controller.api.TimePackageSaveRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubtractTimePackageAmountTest extends AbstractComponentTest {

    @Autowired
    private DSLContext dslContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSubtractTimeToTimePackage() throws IOException, InterruptedException {
        String token = createHeadTeacherUser();
        String phone = generatePhone();
        createStudent(phone);
        var createdTimePackage = createTimePackage(phone, token);

        var timePackageSaveRequest = new TimePackageSaveRequest(phone, -60, "individual");
        String body = objectMapper.writeValueAsString(timePackageSaveRequest);
        HttpRequest request = createAuthorizedRequest("/time/package", token)
                .method(HttpMethod.PATCH.name(), HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());


        assertEquals(200, response.statusCode());
        var updatedTimePackage = objectMapper.readValue(response.body(), TimePackageResponse.class);
        int expectedAmount = createdTimePackage.amount() + timePackageSaveRequest.amount(); // 0
        assertEquals(expectedAmount, updatedTimePackage.amount());
    }

    private TimePackageResponse createTimePackage(String phone, String token) throws IOException, InterruptedException {
        var timePackageSaveRequest = new TimePackageSaveRequest(phone, 60, "individual");
        String body = objectMapper.writeValueAsString(timePackageSaveRequest);
        HttpRequest request = createAuthorizedRequest("/time/package", token)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(httpResponse.body(), TimePackageResponse.class);
    }
}
