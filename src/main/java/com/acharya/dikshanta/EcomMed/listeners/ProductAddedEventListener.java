package com.acharya.dikshanta.EcomMed.listeners;

import com.acharya.dikshanta.EcomMed.constrants.KafkaTopics;
import com.acharya.dikshanta.EcomMed.events.ProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Component
@Slf4j
public class ProductAddedEventListener {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @TransactionalEventListener
    public void handleAddProductEvent(ProductCreatedEvent event) {
        CompletableFuture<SendResult<String, Object>> send = kafkaTemplate.send(KafkaTopics.PRODUCT_ADDED,
                event.productId().toString(),
                event).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to publish an event {}", ex.getMessage(), ex);
            }
            log.info("event published successfully {}", event.productId());
        });
    }
}
