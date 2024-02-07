package com.example.rbapp.timepackage.controller.api;

public record TimePackageResponse(Long id, String type, Integer amount, Integer initialAmount, Long studentId) {
}
