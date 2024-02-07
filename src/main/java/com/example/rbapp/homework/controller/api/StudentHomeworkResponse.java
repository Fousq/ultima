package com.example.rbapp.homework.controller.api;

public record StudentHomeworkResponse(Long id,
                                      Boolean completed,
                                      Boolean inProgress,
                                      String feedback,
                                      String file,
                                      Long studentId) {
}
