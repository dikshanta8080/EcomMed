package com.acharya.dikshanta.EcomMed.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic orderTopic() {
        return TopicBuilder
                .name("order-topic")
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic registrationTopic() {
        return TopicBuilder.name("registration-topic")
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic demoTopic() {
        return TopicBuilder.name("demo-topic")
                .partitions(2)
                .replicas(1)
                .build();
    }
}
