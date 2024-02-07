package com.example.rbapp.studentcourse.controller.api;

public record StudentCourseCreateRequest(String studentPhone,
                                         Long courseId,
                                         String courseTitle,
                                         Long timePackage) {
}
