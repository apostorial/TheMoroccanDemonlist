package com.apostorial.tmdlbackend.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class EmailServiceImpl {
    private final JavaMailSender mailSender;
    private final String fromEmail = System.getProperty("spring.mail.username");

    public void sendVerificationEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject("Email Verification for TheMoroccanDemonlist");
        message.setText("Please click the link below to verify your email:\n"
                + "http://localhost:8080/api/auth/verify-email?token=" + token);
        mailSender.send(message);
    }
}
