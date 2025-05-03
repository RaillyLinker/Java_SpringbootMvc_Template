package com.raillylinker.configurations.kafka_configs;

import com.raillylinker.const_objects.ModuleConst;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

// [Kafka Consumer 설정]
// kafka_consumers 폴더 안의 Listeners 클래스 파일과 연계하여 사용하세요.
@EnableKafka
@Configuration
public class Kafka1MainConfig {
    public Kafka1MainConfig(
            @Value("${kafka-cluster." + KAFKA_CONFIG_NAME + ".uri}")
            @NotNull String uri,
            @Value("${kafka-cluster." + KAFKA_CONFIG_NAME + ".consumer.username}")
            @NotNull String userName,
            @Value("${kafka-cluster." + KAFKA_CONFIG_NAME + ".consumer.password}")
            @NotNull String password
    ) {
        this.uri = uri;
        this.userName = userName;
        this.password = password;
    }

    // !!!application.yml 의 kafka-cluster 안에 작성된 이름 할당하기!!!
    public static final @NotNull String KAFKA_CONFIG_NAME = "kafka1-main";

    public static final @NotNull String CONSUMER_BEAN_NAME = KAFKA_CONFIG_NAME + "_ConsumerFactory";

    public static final @NotNull String PRODUCER_BEAN_NAME = KAFKA_CONFIG_NAME + "_ProducerFactory";


    @Value("${kafka-cluster." + KAFKA_CONFIG_NAME + ".uri}")
    private final @NotNull String uri;

    @Value("${kafka-cluster." + KAFKA_CONFIG_NAME + ".consumer.username}")
    private final @NotNull String userName;

    @Value("${kafka-cluster." + KAFKA_CONFIG_NAME + ".consumer.password}")
    private final @NotNull String password;


    // ----
    @Bean(name = CONSUMER_BEAN_NAME)
    public @NotNull ConcurrentKafkaListenerContainerFactory<String, Object> kafkaConsumer() {
        @NotNull Map<String, Object> config = new HashMap<>();
        // Kafka 브로커에 연결하기 위한 주소를 설정합니다. 여러 개의 브로커가 있을 경우, 콤마로 구분하여 나열합니다.
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, uri);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // SASL/SCRAM 인증 설정
        config.put("security.protocol", "SASL_PLAINTEXT");
        config.put("sasl.mechanism", "PLAIN");
        config.put("sasl.jaas.config",
                String.format(
                        "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";",
                        userName, password
                )
        );

        // 서버별로 고유한 `groupId` 설정
        config.put(ConsumerConfig.GROUP_ID_CONFIG, ModuleConst.SERVER_UUID);

        @NotNull ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(config));

        return factory;
    }

    @Bean(name = PRODUCER_BEAN_NAME)
    public @NotNull KafkaTemplate<String, Object> kafkaProducer() {
        @NotNull Map<String, Object> config = new HashMap<>();
        // Kafka 브로커에 연결하기 위한 주소를 설정합니다. 여러 개의 브로커가 있을 경우, 콤마로 구분하여 나열합니다.
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, uri);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // SASL/SCRAM 인증 설정
        config.put("security.protocol", "SASL_PLAINTEXT");
        config.put("sasl.mechanism", "PLAIN");
        config.put("sasl.jaas.config",
                String.format(
                        "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";",
                        userName, password
                )
        );

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(config));
    }
}
