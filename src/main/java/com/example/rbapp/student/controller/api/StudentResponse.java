package com.example.rbapp.student.controller.api;


import com.example.rbapp.course.controller.api.CourseParticipationCompactResponse;

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
                              List<CourseParticipationCompactResponse> inCourses,
                              Long userId) {
}
