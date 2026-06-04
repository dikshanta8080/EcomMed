package com.acharya.dikshanta.EcomMed.service.impl;

import com.acharya.dikshanta.EcomMed.events.UserRegisteredEvent;
import com.acharya.dikshanta.EcomMed.service.RegistrationNotificationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegistrationNotificationServiceImpl implements RegistrationNotificationService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${send.from}")
    private String sender;

    @Override
    public void sendSuccessfulRegistrationEmail(UserRegisteredEvent event) {
        try {
            Context context = new Context();
            context.setVariable("id", event.id());
            context.setVariable("name", event.name());
            context.setVariable("email", event.email());
            String html = templateEngine.process("email/welcome", context);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(event.email());
            helper.setSubject("Registration Successful!");
            helper.setFrom(sender);
            helper.setText(html, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            log.error("Fail to send an email {}", e.getMessage());
        }

    }
}
