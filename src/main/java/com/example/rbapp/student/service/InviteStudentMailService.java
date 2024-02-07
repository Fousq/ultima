package com.example.rbapp.student.service;

import com.example.rbapp.mail.MailContent;
import com.example.rbapp.mail.SimpleEmailService;
import com.example.rbapp.student.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InviteStudentMailService {

    private final SimpleEmailService emailService;

    public void sendEmail(Student student) {
        String text = String.format("Дорогой студент. " +
                "Войдите на платформу по ссылке https://ultima.deutschkz.online/authorization \n" +
                "с использованием: \n" +
                "Почта: %s \n" +
                "Пароль: %s", student.getEmail(), student.getPhone());
        MailContent mailContent = new MailContent("Приглашение", student.getEmail(), text);
        emailService.sendEmail(mailContent);
    }
}
