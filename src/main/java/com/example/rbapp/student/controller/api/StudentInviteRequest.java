package com.example.rbapp.student.controller.api;

import java.time.LocalDate;

public record StudentInviteRequest(String name,
                                   String surname,
                                   String middleName,
                                   String email,
                                   String phone,
                                   LocalDate birthday,
                                   String timePackageType,
                                   Integer timePackageAmount) {
}
