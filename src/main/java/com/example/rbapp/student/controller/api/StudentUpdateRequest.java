package com.example.rbapp.student.controller.api;

import java.time.LocalDate;

public record StudentUpdateRequest(String name,
                                   String surname,
                                   String email,
                                   String phone,
                                   String middleName,
                                   String city,
                                   LocalDate birthday,
                                   String studyGoal,
                                   String wishes) {
}
