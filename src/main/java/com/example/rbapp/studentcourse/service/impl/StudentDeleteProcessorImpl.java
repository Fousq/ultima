package com.example.rbapp.studentcourse.service.impl;

import com.example.rbapp.student.service.StudentService;
import com.example.rbapp.studentcourse.service.StudentDeleteProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentDeleteProcessorImpl implements StudentDeleteProcessor {

    private final StudentService studentService;

    @Override
    public void deleteByUserId(Long userId) {
        if (studentService.existsByUserId(userId)) {
            studentService.deleteByUserId(userId);
        }
    }
}
