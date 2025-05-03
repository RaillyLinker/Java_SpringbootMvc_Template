package com.raillylinker.kafka_components.producers;

import com.google.gson.Gson;
import com.raillylinker.configurations.kafka_configs.Kafka1MainConfig;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class Kafka1MainProducer {
    public Kafka1MainProducer(
            @Qualifier(Kafka1MainConfig.PRODUCER_BEAN_NAME)
            @NotNull KafkaTemplate<String, Object> kafka1MainProducerTemplate
    ) {
        this.kafka1MainProducerTemplate = kafka1MainProducerTemplate;
    }

    // <멤버 변수 공간>
    private final @NotNull KafkaTemplate<String, Object> kafka1MainProducerTemplate;

    private final @NotNull Logger classLogger = LoggerFactory.getLogger(Kafka1MainProducer.class);

    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    // (testTopic1 에 메시지 발송)
    public void sendMessageToTestTopic1(
            @NotNull SendMessageToTestTopic1InputVo message
    ) {
        // kafkaProducer1 에 토픽 메세지 발행
        kafka1MainProducerTemplate.send("sample-kafka_test-topic1", new Gson().toJson(message));
    }

    @Data
    public static class SendMessageToTestTopic1InputVo {
        private final @NotNull String test;
        private final @NotNull Integer test1;
    }
}