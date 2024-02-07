package com.example.rbapp.course.controller.api;

import com.example.rbapp.teacher.controller.api.TeacherResponse;

public record StudentCourseResponse(Long id, String title, String type, TeacherResponse teacher) {
}
