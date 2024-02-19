package com.github.up2up.testcontainers.kafka.listener;

import kafka.controller.Startup;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.startupcheck.MinimumDurationRunningStartupCheckStrategy;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@ExtendWith(OutputCaptureExtension.class)
public class EventListenerTest {

    @Container
    static KafkaContainer container = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"))
            .withLogConsumer(outputFrame -> System.out.println("[Testcontainers:Kafka]" + outputFrame.getUtf8String()));

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", container::getBootstrapServers);
        registry.add("spring.kafka.consumer.group-id", () -> "sample-group");
    }

    @Test
    public void testKafkaListener(CapturedOutput output) throws ExecutionException, InterruptedException {
        String message = "Hello!";

        SendResult<String, String> result = kafkaTemplate.send("sample-topic", message).get();

        assertThat(result.getRecordMetadata().topic()).isEqualTo("sample-topic");

        Awaitility.await()
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
            assertThat(output.getOut()).contains("Hello!");
        });
    }

}
