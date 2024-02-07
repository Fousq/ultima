package com.example.rbapp.api.teacher.rest;

import com.example.rbapp.api.AbstractComponentTest;
import com.example.rbapp.teacher.controller.api.TeacherResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GetByIdTest extends AbstractComponentTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetById() throws IOException, InterruptedException {
        var token = createHeadTeacherUser();
        var teacherId = createTeacherInDB();

        HttpRequest request = createAuthorizedRequest("/teacher/" + teacherId, token).GET().build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        TeacherResponse teacherResponse = objectMapper.readValue(response.body(), TeacherResponse.class);
        assertNotNull(teacherResponse);
        assertEquals(teacherId, teacherResponse.id());
    }
}
