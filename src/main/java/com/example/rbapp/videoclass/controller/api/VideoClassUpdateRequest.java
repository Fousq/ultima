package com.example.rbapp.videoclass.controller.api;

import com.example.rbapp.studentvideoclass.api.StudentVideoClassUpdateRequest;
import com.example.rbapp.studentvideoclass.entity.StudentVideoClass;

import java.util.List;

public record VideoClassUpdateRequest(Long id,
                                      String title,
                                      String link,
                                      Integer duration,
                                      Long teacherId,
                                      Long courseSubjectId,
                                      List<StudentVideoClassUpdateRequest> studentList) {
}
