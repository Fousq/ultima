package com.example.rbapp.homework.controller.api;

public record StudentHomeworkUpdateRequest(Boolean completed, Boolean progress, String feedback, String file, String description) {
}
