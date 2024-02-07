package com.example.rbapp.teacher.service.impl;

import com.example.rbapp.teacher.service.TeacherDeleteProcessor;
import com.example.rbapp.teacher.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherDeleteProcessorImpl implements TeacherDeleteProcessor {

    private final TeacherService teacherService;

    @Override
    public void deleteByUserId(Long userId) {
        if (teacherService.existsByUserId(userId)) {
            teacherService.deleteByUserId(userId);
        }
    }
}
