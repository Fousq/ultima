package com.example.rbapp.student.controller.api;

public record StudentUpdateRequest(String name,
                                   String surname,
                                   String email,
                                   String phone,
                                   String middleName,
                                   String city,
                                   String studyGoal,
                                   String wishes) {
}
