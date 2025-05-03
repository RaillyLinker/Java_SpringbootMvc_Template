package com.raillylinker.kafka_components.consumers;

import com.google.gson.Gson;
import com.raillylinker.configurations.jpa_configs.Db1MainConfig;
import com.raillylinker.configurations.kafka_configs.Kafka1MainConfig;
import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    // (Auth 모듈의 통합 멤버 정보 삭제 이벤트에 대한 리스너)
    // 이와 연관된 데이터 삭제 및 기타 처리
    @KafkaListener(
            topics = {"auth_member-deleted"},
            groupId = CONSUMER_GROUP_ID,
            containerFactory = Kafka1MainConfig.CONSUMER_BEAN_NAME
    )
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME)
    public void fromAuthDbDeleteFromRaillyLinkerCompanyTotalAuthMemberListener(
            @NotNull ConsumerRecord<String, String> data
    ) {
        classLogger.info("KafkaConsumerLog>>\n{{\n\"data\": {{\n\"{}\"\n}}\n}}", data);

        // JSON 문자열을 객체로 변환
        @NotNull FromAuthDbDeleteFromRaillyLinkerCompanyTotalAuthMemberListenerInputVo inputVo =
                new Gson().fromJson(
                        data.value(),
                        FromAuthDbDeleteFromRaillyLinkerCompanyTotalAuthMemberListenerInputVo.class
                );
        classLogger.info(">> testTopic1Group0ListenerInputVo : {}", inputVo);

        // !!!멤버 테이블을 조회중인 테이블이 있을 경우 회원 탈퇴에 따른 처리를 이곳에 작성하세요.!!!
    }

    @Data
    public static class FromAuthDbDeleteFromRaillyLinkerCompanyTotalAuthMemberListenerInputVo {
        private final @NotNull Long deletedMemberUid;
    }
}
