package com.example.rbapp.homework.controller.api;

import java.util.List;

public record StudentHomeworkDetailsResponse(Long id,
                                             String title,
                                             String description,
                                             List<String> files,
                                             Boolean completed,
                                             String feedback,
                                             Boolean inProgress,
                                             Long studentId,
                                             String name,
                                             String surname) {
}
