package com.example.rbapp.student.controller.api;

public record StudentInviteRequest(String name,
                                   String surname,
                                   String middleName,
                                   String email,
                                   String phone,
                                   String timePackageType,
                                   Integer timePackageAmount) {
}
