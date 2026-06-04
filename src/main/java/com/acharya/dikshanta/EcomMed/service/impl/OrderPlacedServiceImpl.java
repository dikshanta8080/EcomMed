package com.acharya.dikshanta.EcomMed.service.impl;

import com.acharya.dikshanta.EcomMed.events.OrderPlacedEvent;
import com.acharya.dikshanta.EcomMed.service.OrderNotificationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderPlacedServiceImpl implements OrderNotificationService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    @Value("${send.from}")
    private String sender;

    @Override
    public void sendEmail(OrderPlacedEvent event) {
        try {
            Context context = new Context();
            context.setVariable("orderId", event.orderId());
            context.setVariable("orderStatus", event.orderStatus());
            context.setVariable("placedAt", event.placedAt());
            context.setVariable("orderItemEvents", event.orderItemEvents());
            String body = templateEngine.process("email/order-placed", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(sender);
            helper.setTo(event.email());
            helper.setText(body, true);
            helper.setSubject("Order placed Successfully");
            javaMailSender.send(mimeMessage);
            log.info("Email placed successfully");
        } catch (MessagingException e) {
            log.error("Failed to send an email");
        }
    }
}
