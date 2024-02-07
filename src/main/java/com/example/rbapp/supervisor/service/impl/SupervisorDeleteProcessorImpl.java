package com.example.rbapp.supervisor.service.impl;

import com.example.rbapp.supervisor.service.SupervisorDeleteProcessor;
import com.example.rbapp.supervisor.service.SupervisorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupervisorDeleteProcessorImpl implements SupervisorDeleteProcessor {

    private final SupervisorService supervisorService;

    @Override
    public void deleteByUserId(Long userId) {
        if (supervisorService.existsByUserId(userId)) {
            supervisorService.deleteByUserId(userId);
        }
    }
}
