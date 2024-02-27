package com.example.rbapp.student.service;

import com.example.rbapp.bitrix.service.BitrixService;
import com.example.rbapp.student.controller.api.StudentApplicationRequest;
import com.example.rbapp.student.entity.Student;
import com.example.rbapp.studentuser.StudentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentApplicationService {

    private final StudentUserService studentUserService;
    private final BitrixService bitrixService;


    public void create(StudentApplicationRequest request) {
        Student student = studentUserService.createUserStudent(request);
        bitrixService.createClientApplication(student);
    }
}
