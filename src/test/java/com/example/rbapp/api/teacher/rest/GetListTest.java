package com.example.rbapp.api.teacher.rest;

import com.example.rbapp.api.AbstractComponentTest;
import com.example.rbapp.teacher.controller.api.TeacherResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GetListTest extends AbstractComponentTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetList() throws IOException, InterruptedException {
        createTeacherInDB();
        String token = createHeadTeacherUser();

        HttpRequest request = createAuthorizedRequest("/teacher", token).GET().build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Collection<TeacherResponse> teacherList = objectMapper.readValue(response.body(), new TypeReference<>(){});
        assertFalse(teacherList.isEmpty());
    }
}
