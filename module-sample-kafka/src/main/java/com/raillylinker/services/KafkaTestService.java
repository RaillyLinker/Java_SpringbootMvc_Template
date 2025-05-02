package com.raillylinker.services;

import com.raillylinker.controllers.KafkaTestController;
import com.raillylinker.kafka_components.producers.Kafka1MainProducer;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class KafkaTestService {
    public KafkaTestService(
            // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
            @Value("${spring.profiles.active:default}")
            @NotNull String activeProfile,
            @NotNull Kafka1MainProducer kafka1MainProducer
    ) {
        this.activeProfile = activeProfile;
        this.kafka1MainProducer = kafka1MainProducer;
    }

    // <멤버 변수 공간>
    private final @NotNull String activeProfile;

    private final @NotNull Kafka1MainProducer kafka1MainProducer;


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    // (Kafka 토픽 메세지 발행 테스트)
    public void sendKafkaTopicMessageTest(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull KafkaTestController.SendKafkaTopicMessageTestInputVo inputVo
    ) {
        // kafkaProducer1 에 토픽 메세지 발행
        kafka1MainProducer.sendMessageToTestTopic1(
                new Kafka1MainProducer.SendMessageToTestTopic1InputVo(
                        inputVo.getMessage(),
                        1
                )
        );

        httpServletResponse.setStatus(HttpStatus.OK.value());
    }
}
