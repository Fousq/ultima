package com.example.rbapp.user.controller.api;

import java.util.List;

public record LoginResponse(String token, List<String> grants, Long id) {
}
