package com.github.up2up.testcontainers.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

public class KafkaTopicConfig {

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("sample-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
