package com.example.rbapp.user.controller;

import com.example.rbapp.user.controller.api.ChangePasswordRequest;
import com.example.rbapp.user.controller.api.LoginRequest;
import com.example.rbapp.user.controller.api.LoginResponse;
import com.example.rbapp.user.service.LoginService;
import com.example.rbapp.user.service.UserDeleteProcessor;
import com.example.rbapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final LoginService loginService;
    private final UserService userService;
    private final UserDeleteProcessor userDeleteProcessor;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return loginService.processLogin(loginRequest);
    }

    @PatchMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request,
                                            @RequestHeader(AUTHORIZATION) String token) {
        userService.changePassword(request, token.replace("Bearer ", ""));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        userDeleteProcessor.delete(id);
        return ResponseEntity.ok().build();
    }
}
