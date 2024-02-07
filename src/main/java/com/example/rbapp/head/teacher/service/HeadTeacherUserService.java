package com.example.rbapp.head.teacher.service;

import com.example.rbapp.head.teacher.controller.api.HeadTeacherInviteRequest;
import com.example.rbapp.head.teacher.entity.HeadTeacher;
import com.example.rbapp.user.entity.User;
import com.example.rbapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HeadTeacherUserService {

    private final HeadTeacherService headTeacherService;

    private final UserService userService;

    @Transactional
    public HeadTeacher create(HeadTeacherInviteRequest request) {
        User user = new User();
        user.setUsername(request.email());
        user.setPassword(request.phone());
        user.setGrantType("ROLE_HEAD_TEACHER");
        Long userId = userService.create(user);

        HeadTeacher headTeacher = new HeadTeacher();
        headTeacher.setName(request.name());
        headTeacher.setSurname(request.surname());
        headTeacher.setEmail(request.email());
        headTeacher.setPhone(request.phone());
        headTeacher.setUserId(userId);
        return headTeacherService.create(headTeacher);
    }
}
