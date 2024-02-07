package com.example.rbapp.homework.controller.api;

import java.util.List;

public record HomeworkUpdateRequest(Long id,
                                    String title,
                                    String description,
                                    List<String>files,
                                    Long courseSubjectId,
                                    Boolean completed,
                                    Long studentId) {
}
