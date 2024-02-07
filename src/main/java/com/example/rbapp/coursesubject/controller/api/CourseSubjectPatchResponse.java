package com.example.rbapp.coursesubject.controller.api;

import java.util.List;

public record CourseSubjectPatchResponse(Long id,
                                         String title,
                                         String description,
                                         List<String> files,
                                         Boolean completed) {
}
