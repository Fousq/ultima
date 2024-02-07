package com.example.rbapp.student.controller.api;


public record StudentResponse(Long id,
                              String name,
                              String surname,
                              String email,
                              String phone,
                              String middleName,
                              String city,
                              String studyGoal,
                              String wishes,
                              Long userId) {
}
