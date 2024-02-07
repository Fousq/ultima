package com.example.rbapp.api.timepackage.rest;

import com.example.rbapp.api.AbstractComponentTest;
import com.example.rbapp.timepackage.controller.api.TimePackageSaveRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.example.rbapp.jooq.codegen.Tables.STUDENT;
import static com.example.rbapp.jooq.codegen.Tables.TIME_PACKAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateTimePackageTest extends AbstractComponentTest {

    @Autowired
    private DSLContext dslContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateTimePackage() throws IOException, InterruptedException {
        String token = createHeadTeacherUser();
        String phone = generatePhone();
        createStudent(phone);

        var timePackageSaveRequest = new TimePackageSaveRequest(phone, 60, "individual");
        String body = objectMapper.writeValueAsString(timePackageSaveRequest);
        HttpRequest request = createAuthorizedRequest("/time/package", token)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());

        assertEquals(200, response.statusCode());
        checkIfTimePackageExists(phone);
    }

    private void checkIfTimePackageExists(String phone) {
        Long studentId = dslContext.select(STUDENT.ID).from(STUDENT)
                .where(STUDENT.PHONE.eq(phone))
                .fetchSingleInto(Long.class);
        boolean isExists = dslContext.fetchExists(
                dslContext.selectFrom(TIME_PACKAGE).where(TIME_PACKAGE.STUDENT_ID.eq(studentId))
        );
        assertTrue(isExists);
    }
}
