package com.example.rbapp.head.teacher.controller;

import com.example.rbapp.head.teacher.controller.api.HeadTeacherInviteRequest;
import com.example.rbapp.head.teacher.controller.api.HeadTeacherResponse;
import com.example.rbapp.api.service.registration.RegistrationRequest;
import com.example.rbapp.api.exception.NotFoundException;
import com.example.rbapp.head.teacher.service.HeadTeacherInviteService;
import com.example.rbapp.head.teacher.service.HeadTeacherMapper;
import com.example.rbapp.head.teacher.service.HeadTeacherRepository;
import com.example.rbapp.head.teacher.service.registration.HeadTeacherRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/head/teacher")
@RequiredArgsConstructor
public class HeadTeacherController {

    private final HeadTeacherRepository headTeacherRepository;
    private final HeadTeacherRegistrationService headTeacherRegistrationService;
    private final HeadTeacherMapper headTeacherMapper;
    private final HeadTeacherInviteService headTeacherInviteService;

    @GetMapping
    public List<HeadTeacherResponse> headTeacherList() {
        return headTeacherMapper.mapToResponse(headTeacherRepository.findAll());
    }

    @GetMapping("/{id}")
    public HeadTeacherResponse getHeadTeacher(@PathVariable("id") Long id) {
        return headTeacherRepository.findById(id)
                .map(headTeacherMapper::mapToResponse)
                .orElseThrow(() -> new NotFoundException("Head teacher not found"));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody RegistrationRequest registrationRequest) {
        headTeacherRegistrationService.register(registrationRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/invite")
    public ResponseEntity<Object> invite(@RequestBody HeadTeacherInviteRequest request) {
        headTeacherInviteService.processInvite(request);
        return ResponseEntity.ok().build();
    }
}
