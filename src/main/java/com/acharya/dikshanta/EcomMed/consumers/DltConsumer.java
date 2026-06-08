package com.acharya.dikshanta.EcomMed.consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class DltConsumer {
    @KafkaListener(topics = "central-dlt-topic", groupId = "dlt-consumer")
    public void listenDlt(@Payload Object event) {
        System.out.println("DLT message received!");
        System.out.println(event);
    }
}
