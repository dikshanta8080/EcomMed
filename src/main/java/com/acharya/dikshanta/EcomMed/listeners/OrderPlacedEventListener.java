package com.acharya.dikshanta.EcomMed.listeners;

import com.acharya.dikshanta.EcomMed.constrants.KafkaTopics;
import com.acharya.dikshanta.EcomMed.events.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Component
public class OrderPlacedEventListener {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Async
    @TransactionalEventListener
    public void handleOrderPLacedEvent(OrderPlacedEvent event) {
        CompletableFuture<SendResult<String, Object>> send = kafkaTemplate.send(KafkaTopics.ORDER_PLACED,
                event.orderId().toString(),
                event).whenComplete((result, ex) -> {
            if (ex != null) {
                System.out.println("Failed to publish an event");
            } else {
                System.out.println("Event published successfully");
            }
        });
    }


}

