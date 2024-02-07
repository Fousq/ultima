package com.example.rbapp.head.teacher.service;

import com.example.rbapp.head.teacher.entity.HeadTeacher;
import com.example.rbapp.mail.MailContent;
import com.example.rbapp.mail.SimpleEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InviteHeadTeacherMailService {

    private final SimpleEmailService emailService;

    public void sendEmail(HeadTeacher headTeacher) {
        String text = String.format("Дорогой куратор. " +
                "Войдите на платформу по ссылке https://ultima.deutschkz.online/authorization \n" +
                "с использованием: \n" +
                "Почта: %s \n" +
                "Пароль: %s", headTeacher.getEmail(), headTeacher.getPhone());
        MailContent mailContent = new MailContent("Приглашение", headTeacher.getEmail(), text);
        emailService.sendEmail(mailContent);
    }
}
