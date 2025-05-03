package com.raillylinker.kafka_components.producers;

import com.raillylinker.configurations.kafka_configs.Kafka1MainConfig;
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
}