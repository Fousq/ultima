package com.example.rbapp.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/api/test/mail")
@RequiredArgsConstructor
public class TestMailController {

    private final SimpleEmailService service;

  //  @PostMapping
    public void sendTestMail() {
        MailContent mailContent = new MailContent("Test mail", "fous363@gmail.com", "test mail content");
        service.sendEmail(mailContent);
    }
}
