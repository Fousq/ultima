package com.example.rbapp.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleEmailService {

    private final JavaMailSender javaMailSender;

    public void sendEmail(MailContent mailContent) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("mail@deutschkz.online");
        simpleMailMessage.setTo(mailContent.to());
        simpleMailMessage.setSubject(mailContent.subject());
        simpleMailMessage.setText(mailContent.content());
        log.debug("Sending email to {}", mailContent.to());
        javaMailSender.send(simpleMailMessage);
    }
}
