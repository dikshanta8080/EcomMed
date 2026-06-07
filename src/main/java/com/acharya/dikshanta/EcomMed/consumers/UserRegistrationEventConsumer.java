package com.acharya.dikshanta.EcomMed.consumers;

import com.acharya.dikshanta.EcomMed.constrants.KafkaTopics;
import com.acharya.dikshanta.EcomMed.events.UserRegisteredEvent;
import com.acharya.dikshanta.EcomMed.service.RegistrationNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserRegistrationEventConsumer {
    private final RegistrationNotificationService registrationNotificationService;

    @KafkaListener(topics = KafkaTopics.USER_REGISTERED, groupId = "notification-group")
    public void sendNotification(UserRegisteredEvent event) {
        log.debug("sendNotification received an event {}", event.id());
        registrationNotificationService.sendSuccessfulRegistrationEmail(event);
    }
}
