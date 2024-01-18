package com.github.up2up.testcontainers.rabbitmq.consumer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@Testcontainers
@ExtendWith(OutputCaptureExtension.class)
public class MessageListenerTest {

    static String image = "rabbitmq:3-management-alpine";

    @Container
    static RabbitMQContainer container = new RabbitMQContainer(image)
//            .withQueue("test-queue") // Deprecated
            .withCopyFileToContainer(MountableFile.forClasspathResource("definitions.json"), "/tmp/rabbit.definitions.json")
            .withEnv("RABBITMQ_SERVER_ADDITIONAL_ERL_ARGS", "-rabbitmq_management load_definitions \"/tmp/rabbit.definitions.json\"")
            ;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", container::getHost);
        registry.add("spring.rabbitmq.port", container::getAmqpPort);
        registry.add("spring.rabbitmq.username", container::getAdminUsername);
        registry.add("spring.rabbitmq.password", container::getAdminPassword);
    }

    @Test
    void testReceiveMessage(CapturedOutput output) {
        String message = "test-something";
        String testQueue = "test-queue";

        rabbitTemplate.convertAndSend(testQueue, message);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            assertThat( output.getOut()).contains("test-something");
        });
    }
}
