package com.example.rbapp.teacher.service.impl;

import com.example.rbapp.mail.MailContent;
import com.example.rbapp.mail.SimpleEmailService;
import com.example.rbapp.teacher.entity.Teacher;
import com.example.rbapp.teacher.service.TeacherMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InviteTeacherMailService implements TeacherMailService {

    private final SimpleEmailService emailService;

    @Override
    public void sendMail(Teacher teacher) {
        String text = String.format("Дорогой преподаватель. " +
                "Войдите на платформу по ссылке https://ultima.deutschkz.online/authorization \n" +
                "с использованием: \n" +
                "Почта: %s \n" +
                "Пароль: %s", teacher.getEmail(), teacher.getPhone());
        MailContent mailContent = new MailContent("Приглашние", teacher.getEmail(), text);
        emailService.sendEmail(mailContent);
    }
}
