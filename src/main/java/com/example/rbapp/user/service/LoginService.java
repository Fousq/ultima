package com.example.rbapp.user.service;

import com.example.rbapp.user.controller.api.LoginRequest;
import com.example.rbapp.user.controller.api.LoginResponse;

public interface LoginService {
    LoginResponse processLogin(LoginRequest request);
}
