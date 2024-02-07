package com.example.rbapp.homework.controller.api;

import java.util.List;

public record HomeworkResponse(Long id,
                               String title,
                               String description,
                               List<String> files,
                               Long courseSubjectId) {
}
