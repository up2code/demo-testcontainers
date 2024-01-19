package com.github.up2up.testcontainers.rabbitmq.consumer;


import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;


@Component
public class CloudStreamListener {

    // Listen rabbitMQ cloud stream
    @Bean
    Consumer<Message<String>> consumeCloud() {
        return message -> {
            System.out.println("Received " + message.getPayload());
        };
    }
}
