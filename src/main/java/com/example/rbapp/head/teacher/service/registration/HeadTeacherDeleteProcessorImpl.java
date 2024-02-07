package com.example.rbapp.head.teacher.service.registration;

import com.example.rbapp.head.teacher.service.HeadTeacherDeleteProcessor;
import com.example.rbapp.head.teacher.service.HeadTeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeadTeacherDeleteProcessorImpl implements HeadTeacherDeleteProcessor {

    private final HeadTeacherService headTeacherService;

    @Override
    public void deleteByUserId(Long userId) {
        if (headTeacherService.existsByUserId(userId)) {
            headTeacherService.deleteByUserId(userId);
        }
    }
}
