package com.example.rbapp.teacher.controller.api;

public record TeacherInviteRequest(String email,
                                   String phone,
                                   String name,
                                   String surname,
                                   String currency,
                                   String bankDetails,
                                   Double individualPrice,
                                   Double pairPrice,
                                   Double groupPrice,
                                   Boolean payableForCanceledLesson) {
}
