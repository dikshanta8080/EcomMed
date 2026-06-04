package com.acharya.dikshanta.EcomMed.listeners;

import com.acharya.dikshanta.EcomMed.events.UserRegisteredEvent;
import com.acharya.dikshanta.EcomMed.service.impl.RegistrationNotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCreatedEventListener {
    private final RegistrationNotificationServiceImpl notificationService;

    @EventListener
    @Async
    public void sendRegistrationEmail(UserRegisteredEvent event) {
        notificationService.sendSuccessfulRegistrationEmail(event);

    }
}
