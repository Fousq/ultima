package com.example.rbapp.teacher.controller.api;

import com.example.rbapp.course.controller.api.CourseParticipationCompactResponse;

import java.util.List;

public record TeacherResponse(Long id,
                              String name,
                              String surname,
                              String email,
                              String phone,
                              List<CourseParticipationCompactResponse> inCourses,
                              Long userId) {
}
