package com.example.rbapp.api;

import com.example.rbapp.api.service.registration.RegistrationRequest;
import com.example.rbapp.user.controller.api.LoginRequest;
import com.example.rbapp.user.controller.api.LoginResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.jobrunr.scheduling.JobScheduler;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static com.example.rbapp.jooq.codegen.Tables.*;
import static com.example.rbapp.jooq.codegen.Tables.STUDENT_COURSE_SUBJECT;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public abstract class AbstractComponentTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DSLContext dslContext;

    @MockBean
    JobScheduler jobScheduler;

    protected static HttpClient httpClient = HttpClient.newHttpClient();

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:13-alpine"
    );

    @BeforeAll
    static void setUp() {
        postgres.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    protected HttpRequest.Builder createRequest(String path) {
        return HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/api" + path))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    protected HttpRequest.Builder createAuthorizedRequest(String path, String token) {
        return createRequest(path)
                .header("Authorization", "Bearer " + token);
    }

    protected String createHeadTeacherUser(String phone) {
        return createUserReturningToken(phone, "/head/teacher");
    }

    protected String createHeadTeacherUser() {
        String phone = generatePhone();
        return createHeadTeacherUser(phone);
    }

    protected String createStudent(String phone) {
        return createUserReturningToken(phone, "/student");
    }

    protected String createStudent() {
        String phone = generatePhone();
        return createStudent(phone);
    }

    private String createUserReturningToken(String phone, String registrationUrl) {
        try {
            var str = generateString();
            createUser(str, phone, registrationUrl);

            var loginRequest = new LoginRequest(str, str);
            var body = objectMapper.writeValueAsString(loginRequest);
            var request = createRequest("/user/login").POST(HttpRequest.BodyPublishers.ofString(body)).build();
            var httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            var loginResponse = objectMapper.readValue(httpResponse.body(), LoginResponse.class);
            return loginResponse.token();
        } catch (IOException | InterruptedException e) {
            log.error("Error on user creation, {}", e.getMessage(), e);
            throw new RuntimeException();
        }
    }

    private void createUser(String str, String phone, String registrationUrl) {
        try {
            RegistrationRequest registrationRequest = new RegistrationRequest(str, str, str,
                    str, str + "@mail.ml", phone, str, str, str, str);
            var body = objectMapper.writeValueAsString(registrationRequest);
            var request = createRequest(registrationUrl).POST(HttpRequest.BodyPublishers.ofString(body)).build();
            httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        } catch (IOException | InterruptedException e) {
            log.error("Error on user creation, {}", e.getMessage(), e);
            throw new RuntimeException();
        }
    }

    protected void createUser(String str, String registrationUrl) {
        var phone = generatePhone();
        createUser(str, phone, registrationUrl);
    }

    protected String generatePhone() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    protected String generateString() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    protected Long createStudentInDB() {
        var str = generateString();

        Long userId = createUser(str);

        return dslContext.insertInto(STUDENT)
                .set(STUDENT.SURNAME, str)
                .set(STUDENT.NAME, str)
                .set(STUDENT.EMAIL, str)
                .set(STUDENT.PHONE, str)
                .set(STUDENT.USER_ID, userId)
                .returningResult(STUDENT.ID)
                .fetchSingleInto(Long.class);
    }

    protected Long createTimePackageInDB(String type, Long studentId) {
        return dslContext.insertInto(TIME_PACKAGE)
                .set(TIME_PACKAGE.TYPE, type)
                .set(TIME_PACKAGE.AMOUNT_IN_MINUTES, 60)
                .set(TIME_PACKAGE.STUDENT_ID, studentId)
                .returningResult(TIME_PACKAGE.ID)
                .fetchSingleInto(Long.class);
    }

    protected void createCourseSubjectInDB(String type, Long studentId) {
        var courseStr = generateString();

        var courseId = dslContext.insertInto(COURSE)
                .set(COURSE.TITLE, courseStr)
                .set(COURSE.TYPE, type)
                .returningResult(COURSE.ID)
                .fetchSingleInto(Long.class);

        var courseSubjectStr = generateString();
        var courseSubjectId = dslContext.insertInto(COURSE_SUBJECT)
                .set(COURSE_SUBJECT.TITLE, courseSubjectStr)
                .set(COURSE_SUBJECT.DURATION, 40)
                .set(COURSE_SUBJECT.START_AT, LocalDateTime.now().minusHours(1))
                .set(COURSE_SUBJECT.COURSE_ID, courseId)
                .returningResult(COURSE_SUBJECT.ID)
                .fetchSingleInto(Long.class);

        dslContext.insertInto(STUDENT_COURSE)
                .set(STUDENT_COURSE.STUDENT_ID, studentId)
                .set(STUDENT_COURSE.COURSE_ID, courseId)
                .execute();

        dslContext.insertInto(STUDENT_COURSE_SUBJECT)
                .set(STUDENT_COURSE_SUBJECT.STUDENT_ID, studentId)
                .set(STUDENT_COURSE_SUBJECT.COURSE_SUBJECT_ID, courseSubjectId)
                .execute();
    }

    protected Long createTeacherInDB() {
        var str = generateString();
        Long userId = createUser(str);

        return dslContext.insertInto(TEACHER)
                .set(TEACHER.NAME, str)
                .set(TEACHER.SURNAME, str)
                .set(TEACHER.EMAIL, str)
                .set(TEACHER.PHONE, str)
                .set(TEACHER.USER_ID, userId)
                .returningResult(TEACHER.ID)
                .fetchSingleInto(Long.class);
    }

    protected Long createUser(String string) {
        return dslContext.insertInto(APP_USER)
                .set(APP_USER.USERNAME, string)
                .set(APP_USER.PASSWORD, string)
                .set(APP_USER.GRANT_TYPE, string)
                .returningResult(APP_USER.ID)
                .fetchSingleInto(Long.class);
    }
}
