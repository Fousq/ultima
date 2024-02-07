package com.example.rbapp.videoclass.controller.api;

import com.example.rbapp.studentvideoclass.api.StudentVideoClassResponse;

import java.util.List;

public record VideoClassResponse(Long id,
                                 String title,
                                 String link,
                                 Integer duration,
                                 List<StudentVideoClassResponse> studentList) {
}
