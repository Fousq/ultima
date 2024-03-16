package com.example.rbapp.student.service;

import com.example.rbapp.bitrix.service.BitrixService;
import com.example.rbapp.student.controller.api.StudentApplicationRequest;
import com.example.rbapp.student.controller.api.StudentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentApplicationService {

    private final StudentService studentService;
    private final BitrixService bitrixService;


    public void create(Long userId, StudentApplicationRequest request) {
        var student = studentService.getStudentByUserId(userId);
        student.setStudyGoal(request.studyGoal());
        student.setLangLevel(request.level());
        studentService.updateEntity(student);
        bitrixService.createClientApplication(student);
    }
}
