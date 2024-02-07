package com.example.rbapp.user.service.impl;

import com.example.rbapp.chat.service.ChatRoomDeleteProcessor;
import com.example.rbapp.head.teacher.service.HeadTeacherDeleteProcessor;
import com.example.rbapp.studentcourse.service.StudentDeleteProcessor;
import com.example.rbapp.supervisor.service.SupervisorDeleteProcessor;
import com.example.rbapp.teacher.service.TeacherDeleteProcessor;
import com.example.rbapp.user.service.UserDeleteProcessor;
import com.example.rbapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDeleteProcessorImpl implements UserDeleteProcessor {

    private final StudentDeleteProcessor studentDeleteProcessor;
    private final TeacherDeleteProcessor teacherDeleteProcessor;
    private final HeadTeacherDeleteProcessor headTeacherDeleteProcessor;
    private final ChatRoomDeleteProcessor chatRoomDeleteProcessor;
    private final SupervisorDeleteProcessor supervisorDeleteProcessor;
    private final UserService userService;

    @Override
    public void delete(Long id) {
        studentDeleteProcessor.deleteByUserId(id);
        teacherDeleteProcessor.deleteByUserId(id);
        headTeacherDeleteProcessor.deleteByUserId(id);
        supervisorDeleteProcessor.deleteByUserId(id);
        chatRoomDeleteProcessor.deleteUserChatRooms(id);
        userService.delete(id);
    }
}
