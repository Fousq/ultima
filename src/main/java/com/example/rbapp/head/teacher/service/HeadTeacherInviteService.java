package com.example.rbapp.head.teacher.service;

import com.example.rbapp.bitrix.service.BitrixService;
import com.example.rbapp.head.teacher.controller.api.HeadTeacherInviteRequest;
import com.example.rbapp.head.teacher.entity.HeadTeacher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeadTeacherInviteService {

    private final HeadTeacherUserService headTeacherUserService;
    private final BitrixService bitrixService;
    private final HeadTeacherService headTeacherService;
    private final InviteHeadTeacherMailService inviteHeadTeacherMailService;

    public void processInvite(HeadTeacherInviteRequest request) {
        HeadTeacher headTeacher = headTeacherUserService.create(request);
        Long bitrixId = bitrixService.createHeadTeacherContact(headTeacher);
        headTeacherService.updateBitrix(headTeacher, bitrixId);
        inviteHeadTeacherMailService.sendEmail(headTeacher);
    }
}
