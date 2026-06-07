package com.acharya.dikshanta.EcomMed.listeners;

import com.acharya.dikshanta.EcomMed.constrants.KafkaTopics;
import com.acharya.dikshanta.EcomMed.events.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCreatedEventListener {
    private final KafkaTemplate<String, Object> kafkaTemplate;


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void sendRegistrationEmail(UserRegisteredEvent event) {
        kafkaTemplate.send(KafkaTopics.USER_REGISTERED, event.id().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to send event for id={}", event.id(), ex);
                    } else {
                        log.info("Event sent successfully for id={}", event.id());
                    }
                });
    }
}
