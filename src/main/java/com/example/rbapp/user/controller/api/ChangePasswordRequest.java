package com.example.rbapp.user.controller.api;

public record ChangePasswordRequest(String currentPassword, String newPassword) {
}
