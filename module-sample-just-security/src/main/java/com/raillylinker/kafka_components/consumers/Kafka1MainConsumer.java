package com.raillylinker.kafka_components.consumers;

import com.google.gson.Gson;
import com.raillylinker.configurations.kafka_configs.Kafka1MainConfig;
import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

// kafka 토픽은 Producer 에서 결정합니다.
// _ 로 구분하며, {모듈 고유값}_{Topic 고유값} 의 형태로 정합니다.
@Component
public class Kafka1MainConsumer {
    // <멤버 변수 공간>
    private final @NotNull Logger classLogger = LoggerFactory.getLogger(Kafka1MainConsumer.class);

    // !!!모듈 컨슈머 그룹 아이디!!!
    private static final String CONSUMER_GROUP_ID = "com.raillylinker.sample_kafka";

    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    // (testTopic1 에 대한 리스너)
    @KafkaListener(
            topics = {"sample-kafka_test-topic1"},
            groupId = CONSUMER_GROUP_ID,
            containerFactory = Kafka1MainConfig.CONSUMER_BEAN_NAME
    )
    public void testTopic1Group0Listener(
            @NotNull ConsumerRecord<String, String> data
    ) {
        classLogger.info("KafkaConsumerLog>>\n{{\n\"data\": {{\n\"{}\"\n}}\n}}", data);

        // JSON 문자열을 객체로 변환
        @NotNull TestTopic1Group0ListenerInputVo inputVo = new Gson().fromJson(data.value(), TestTopic1Group0ListenerInputVo.class);
        classLogger.info(">> testTopic1Group0ListenerInputVo : {}", inputVo);
    }

    @Data
    public static class TestTopic1Group0ListenerInputVo {
        private final @NotNull String test;
        private final @NotNull Integer test1;
    }


    // ----
    // (testTopic2 에 대한 리스너)
    @KafkaListener(
            topics = {"sample-kafka_test-topic2"},
            groupId = CONSUMER_GROUP_ID,
            containerFactory = Kafka1MainConfig.CONSUMER_BEAN_NAME
    )
    public void testTopic2Group0Listener(
            @NotNull ConsumerRecord<String, String> data
    ) {
        classLogger.info("KafkaConsumerLog>>\n{{\n\"data\": {{\n\"{}\"\n}}\n}}", data);
    }


    // ----
    // (testTopic2 에 대한 동일 그룹 테스트 리스너)
    // 동일 topic 에 동일 group 을 설정할 경우, 리스너는 한개만을 선택하고 다른 하나는 침묵합니다.
    @KafkaListener(
            topics = {"sample-kafka_test-topic2"},
            groupId = CONSUMER_GROUP_ID,
            containerFactory = Kafka1MainConfig.CONSUMER_BEAN_NAME
    )
    public void testTopic2Group0Listener2(
            @NotNull ConsumerRecord<String, String> data
    ) {
        classLogger.info("KafkaConsumerLog>>\n{{\n\"data\": {{\n\"{}\"\n}}\n}}", data);
    }


    // ----
    // (testTopic2 에 대한 리스너 - 그룹 변경)
    @KafkaListener(
            topics = {"sample-kafka_test-topic2"},
            groupId = CONSUMER_GROUP_ID + "_2",
            containerFactory = Kafka1MainConfig.CONSUMER_BEAN_NAME
    )
    public void testTopic2Group1Listener(
            @NotNull ConsumerRecord<String, String> data
    ) {
        classLogger.info("KafkaConsumerLog>>\n{{\n\"data\": {{\n\"{}\"\n}}\n}}", data);
    }
}
