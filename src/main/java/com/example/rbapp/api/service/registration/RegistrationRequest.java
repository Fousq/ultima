package com.example.rbapp.api.service.registration;

public record RegistrationRequest(String username,
                                  String password,
                                  String name,
                                  String surname,
                                  String email,
                                  String phone,
                                  String middleName,
                                  String city,
                                  String studyGoal,
                                  String wishes) {
}
