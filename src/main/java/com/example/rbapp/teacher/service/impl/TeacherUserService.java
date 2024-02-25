package com.example.rbapp.teacher.service.impl;

import com.example.rbapp.teacher.controller.api.TeacherInviteRequest;
import com.example.rbapp.teacher.entity.Teacher;
import com.example.rbapp.teacher.service.TeacherService;
import com.example.rbapp.user.entity.User;
import com.example.rbapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeacherUserService {

    private final UserService userService;

    private final TeacherService teacherService;

    @Transactional
    public Teacher create(TeacherInviteRequest request) {
        User user = new User();
        user.setUsername(request.email());
        user.setPassword(request.phone());
        user.setGrantType("ROLE_TEACHER");
        Long userId = userService.create(user);

        Teacher teacher = new Teacher();
        teacher.setName(request.name());
        teacher.setSurname(request.surname());
        teacher.setEmail(request.email());
        teacher.setPhone(request.phone());
        teacher.setBankDetails(request.bankDetails());
        teacher.setUserId(userId);
        return teacherService.create(teacher);
    }
}
