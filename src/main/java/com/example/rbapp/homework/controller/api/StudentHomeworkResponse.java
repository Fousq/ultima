package com.example.rbapp.homework.controller.api;

public record StudentHomeworkResponse(Long id,
                                      Boolean completed,
                                      Boolean inProgress,
                                      String feedback,
                                      String description,
                                      String file,
                                      Long studentId) {
}
