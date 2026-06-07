package com.acharya.dikshanta.EcomMed.utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;

//@Component
public class KafkaTestRunner implements CommandLineRunner {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaTestRunner(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void run(String... args) {
        System.out.println("Sending Message");

        for (int i = 0; i < 100000; i++) {
            kafkaTemplate.send("demo-topic",
                    "orderId-101",
                    "Hello Kafka from Spring Boot: event-" + i);
        }

        System.out.println("Message sent!");
    }
}