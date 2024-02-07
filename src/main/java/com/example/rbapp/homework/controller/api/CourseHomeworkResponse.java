package com.example.rbapp.homework.controller.api;

public record CourseHomeworkResponse(Long id,
                                    String title,
                                    String description,
                                    Boolean completed,
                                    Boolean inProgress,
                                    String feedback,
                                    String file,
                                    Long studentId) {
}
