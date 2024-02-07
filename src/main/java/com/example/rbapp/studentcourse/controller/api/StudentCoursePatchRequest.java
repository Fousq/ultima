package com.example.rbapp.studentcourse.controller.api;

public record StudentCoursePatchRequest(String studentPhone, Long courseId, Long timePackage) {
}
