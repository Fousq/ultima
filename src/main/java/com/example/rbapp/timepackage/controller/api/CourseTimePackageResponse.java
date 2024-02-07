package com.example.rbapp.timepackage.controller.api;

public record CourseTimePackageResponse(Long id, String type, Integer amount, Integer initialAmount, Long studentId, Long courseId) {
}
