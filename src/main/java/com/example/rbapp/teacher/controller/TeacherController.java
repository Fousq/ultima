package com.example.rbapp.teacher.controller;

import com.example.rbapp.teacher.controller.api.TeacherInviteRequest;
import com.example.rbapp.teacher.controller.api.TeacherResponse;
import com.example.rbapp.api.service.registration.RegistrationRequest;
import com.example.rbapp.teacher.controller.api.TeacherSaveRequest;
import com.example.rbapp.teacher.service.TeacherService;
import com.example.rbapp.teacher.service.impl.TeacherInviteService;
import com.example.rbapp.teacher.service.registartion.TeacherRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherRegistrationService teacherRegistrationService;
    private final TeacherService teacherService;
    private final TeacherInviteService teacherInviteService;

    @GetMapping
    public List<TeacherResponse> teacherList() {
        return teacherService.getTeacherList();
    }

    @GetMapping("/{id}")
    public TeacherResponse getTeacher(@PathVariable("id") Long id) {
        return teacherService.getById(id);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody RegistrationRequest registrationRequest) {
        teacherRegistrationService.register(registrationRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public TeacherResponse update(@PathVariable("id") Long id, @RequestBody TeacherSaveRequest teacherSaveRequest) {
        return teacherService.update(id, teacherSaveRequest);
    }

    @PostMapping("/invite")
    public ResponseEntity<Object> invite(@RequestBody TeacherInviteRequest request) {
        teacherInviteService.processInvite(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{id}")
    public TeacherResponse getTeacherByUserId(@PathVariable("id") Long userId) {
        return teacherService.getByUserId(userId);
    }
}

