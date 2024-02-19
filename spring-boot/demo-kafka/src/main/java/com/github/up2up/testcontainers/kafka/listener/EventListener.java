package com.github.up2up.testcontainers.kafka.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EventListener {

    @KafkaListener(topics = "sample-topic", groupId = "sample-group")
    public void listen(String message) {
        System.out.println("Received Message in group sample-group: " + message);
    }
}
