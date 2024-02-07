package com.example.rbapp.api.teacher.rest;

import com.example.rbapp.api.AbstractComponentTest;
import com.example.rbapp.jooq.codegen.tables.records.TeacherRecord;
import com.example.rbapp.teacher.controller.api.TeacherResponse;
import com.example.rbapp.teacher.controller.api.TeacherSaveRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.example.rbapp.jooq.codegen.Tables.TEACHER;
import static org.junit.jupiter.api.Assertions.*;

public class TeacherUpdateTest extends AbstractComponentTest {

    @Autowired
    private DSLContext dslContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldUpdate() throws IOException, InterruptedException {
        String token = createHeadTeacherUser();
        var teacherId = createTeacherInDB();
        TeacherRecord teacherBefore = getTeacher(teacherId);

        var str = generateString();
        TeacherSaveRequest teacherSaveRequest = new TeacherSaveRequest(str, str, str, str);
        String body = objectMapper.writeValueAsString(teacherSaveRequest);
        HttpRequest request = createAuthorizedRequest("/teacher/" + teacherId, token)
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        TeacherResponse teacherResponse = objectMapper.readValue(response.body(), TeacherResponse.class);
        assertNotNull(teacherResponse);
        checkIfTeacherChanged(teacherId, teacherBefore);
    }

    private void checkIfTeacherChanged(Long teacherId, TeacherRecord teacherBefore) {
        TeacherRecord teacherRecord = getTeacher(teacherId);
        assertNotEquals(teacherRecord, teacherBefore);
    }

    private TeacherRecord getTeacher(Long teacherId) {
        return dslContext.selectFrom(TEACHER)
                .where(TEACHER.ID.eq(teacherId))
                .fetchSingleInto(TeacherRecord.class);
    }
}
