package com.example.rbapp.student.controller.api;


import com.example.rbapp.course.controller.api.CourseParticipationCompactResponse;

import java.time.LocalDate;
import java.util.List;

public record StudentResponse(Long id,
                              String name,
                              String surname,
                              String email,
                              String phone,
                              String middleName,
                              String city,
                              String studyGoal,
                              String wishes,
                              LocalDate birthday,
                              String parentPhone,
                              List<CourseParticipationCompactResponse> inCourses,
                              Long userId) {
}
