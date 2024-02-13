package com.example.rbapp.student.controller;

import com.example.rbapp.student.controller.api.StudentApplicationRequest;
import com.example.rbapp.student.controller.api.StudentInviteRequest;
import com.example.rbapp.student.controller.api.StudentResponse;
import com.example.rbapp.api.service.registration.RegistrationRequest;
import com.example.rbapp.student.controller.api.StudentUpdateRequest;
import com.example.rbapp.student.service.*;
import com.example.rbapp.student.service.registration.StudentRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentMapper studentMapper;
    private final StudentService studentService;
    private final StudentRegistrationService studentRegistrationService;
    private final StudentInviteService studentInviteService;
    private final StudentApplicationService studentApplicationService;

    @GetMapping
    public List<StudentResponse> studentList(
            @RequestParam(
                    value = "include-course",
                    defaultValue = "true",
                    required = false
            )
            Boolean includeCourseParticipation
    ) {
        return studentService.getAllStudents(includeCourseParticipation);
    }

    @GetMapping("/{id}")
    public StudentResponse getStudent(@PathVariable("id") Long id) {
        return studentMapper.mapEntityToResponse(studentService.getStudent(id));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody RegistrationRequest registrationRequest) {
        studentRegistrationService.register(registrationRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public StudentResponse update(@PathVariable("id") Long id, @RequestBody StudentUpdateRequest request) {
        return studentService.update(id, request);
    }

    @PostMapping("/invite")
    public ResponseEntity<Object> inviteStudent(@RequestBody StudentInviteRequest request) {
        studentInviteService.processInvitation(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send/application")
    public ResponseEntity<Object> postCourseApplication(@RequestBody StudentApplicationRequest request) {
        studentApplicationService.create(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{id}")
    public StudentResponse getStudentByUserId(@PathVariable("id") Long userId) {
        return studentService.getStudentByUserId(userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteStudent(@PathVariable("id") Long id) {
        studentService.delete(id);
        return ResponseEntity.ok().build();
    }
}
