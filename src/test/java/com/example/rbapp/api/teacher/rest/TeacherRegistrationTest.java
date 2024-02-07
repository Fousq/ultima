package com.example.rbapp.api.teacher.rest;

import com.example.rbapp.api.AbstractComponentTest;
import com.example.rbapp.api.service.registration.RegistrationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.example.rbapp.jooq.codegen.Tables.APP_USER;
import static com.example.rbapp.jooq.codegen.Tables.TEACHER;
import static org.junit.jupiter.api.Assertions.*;

public class TeacherRegistrationTest extends AbstractComponentTest {

    @Autowired
    private DSLContext dslContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldRegistration() throws IOException, InterruptedException {
        var str = generateString();
        var registrationRequest = new RegistrationRequest(str, str, str, str, str, str, str, str, str, str);
        var body = objectMapper.writeValueAsString(registrationRequest);

        HttpRequest request = createRequest("/teacher")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());

        assertEquals(200, response.statusCode());
        checkIfAccountExists(str, str);
    }

    private void checkIfAccountExists(String name, String username) {
        boolean isTeacherAccountExists = dslContext.fetchExists(
                dslContext.select().from(TEACHER)
                        .innerJoin(APP_USER).on(APP_USER.ID.eq(TEACHER.USER_ID))
                        .where(TEACHER.NAME.eq(name))
                        .and(APP_USER.USERNAME.eq(username))
        );
        assertTrue(isTeacherAccountExists);
    }
}
