package com.github.up2up.testcontainers.rabbitmq.consumer;

import com.rabbitmq.client.ConnectionFactory;
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
            .withLogConsumer(frame -> System.out.print(new String(frame.getBytes()))) // Log output to console
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

    private ConnectionFactory createConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(container.getHost());
        factory.setPort(container.getAmqpPort());
        factory.setUsername(container.getAdminUsername());
        factory.setPassword(container.getAdminPassword());
        return factory;
    }

    @Test
    void testVerifyQueueExists() {
        ConnectionFactory factory = createConnectionFactory();

        await().atMost(5, SECONDS).untilAsserted(() -> {
            try(var connection = factory.newConnection()) {
                assertThat(connection.createChannel()
                        .queueDeclarePassive("test-queue")
                        .getQueue())
                        .isNotNull();
            }
        });
    }

    @Test
    void testVerifyExchangeExists() {
        ConnectionFactory factory = createConnectionFactory();

        await().atMost(5, SECONDS).untilAsserted(() -> {
            try(var connection = factory.newConnection()) {
                assertThat(connection.createChannel()
                        .exchangeDeclarePassive("test-exchange"))
                        .isNotNull();
            }
        });
    }

    @Test
    void testReceiveMessageDirectlyToQueue(CapturedOutput output) {
        String message = "test-something";
        String testQueue = "test-queue";

        rabbitTemplate.convertAndSend(testQueue, message);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            assertThat( output.getOut()).contains("test-something");
        });
    }

    @Test
    void testReceiveMessageWhenPublishToExchange(CapturedOutput output) {
        String message = "test-something";
        String exchange = "test-exchange";
        String rouutingKey = "x.y.z";

        rabbitTemplate.convertAndSend(exchange, rouutingKey, message);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            assertThat( output.getOut()).contains("test-something");
        });
    }
}
