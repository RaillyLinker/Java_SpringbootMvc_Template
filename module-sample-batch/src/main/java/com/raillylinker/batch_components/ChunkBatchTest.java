package com.raillylinker.batch_components;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.raillylinker.configurations.jpa_configs.Db1MainConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;

@Component
public class ChunkBatchTest {
    public ChunkBatchTest(
            @NotNull JobRepository jobRepository,
            @Qualifier(Db1MainConfig.TRANSACTION_NAME)
            @NotNull PlatformTransactionManager transactionManager
    ) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;

        try {
            // 테스트용 데이터 파일이 없으면 생성
            Files.createDirectories(saveDirectoryPath);
            @NotNull List<Path> files;
            try (@NotNull Stream<Path> fileStream = Files.list(saveDirectoryPath)) {
                files = fileStream.toList();
            }
            if (files.isEmpty()) {
                @NotNull Random random = new Random();
                for (int index = 1; index <= 10; index++) {
                    @NotNull String fileName = "batch_test" + index + ".json";
                    @NotNull JsonObject jsonObject = new JsonObject();
                    @NotNull String randomName = IntStream.range(0, 6)
                            .mapToObj(i -> String.valueOf((char) ('A' + random.nextInt(26))))
                            .collect(Collectors.joining());
                    jsonObject.addProperty("name", randomName);
                    jsonObject.addProperty("num", random.nextInt(1000));
                    Files.writeString(saveDirectoryPath.resolve(fileName), gson.toJson(jsonObject));
                }
                classLogger.info("ChunkBatchTest : 파일 생성 완료");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize batch test directory", e);
        }
    }

    // 배치 Job 이름
    public static final @NotNull String BATCH_JOB_NAME = "ChunkBatchTest";

    private final @NotNull JobRepository jobRepository;
    private final @NotNull PlatformTransactionManager transactionManager;

    private final @NotNull Logger classLogger = LoggerFactory.getLogger(ChunkBatchTest.class);
    private final @NotNull Gson gson = new Gson();

    // JSON 파일 저장 위치
    private final @NotNull Path saveDirectoryPath = Paths.get("./by_product_files/sample_batch/batch_test").toAbsolutePath().normalize();


    // ---------------------------------------------------------------------------------------------
    // [Batch Job 및 하위 작업 작성]
    // BatchConfig 하나당 하나의 Job 을 가져야 합니다.

    // (Batch Job)
    @Bean(BATCH_JOB_NAME)
    public @NotNull Job batchJob() {
        return new JobBuilder(BATCH_JOB_NAME + "_batchJob", jobRepository)
                .start(chunkTestStep())
                .build();
    }

    @Bean
    public @NotNull Step chunkTestStep() {
        return new StepBuilder(BATCH_JOB_NAME + "_chunkTestStep", jobRepository)
                .<String, JsonObject>chunk(10, transactionManager)
                .reader(jsonFileReader())
                .processor(jsonProcessor())
                .writer(jsonWriter())
                .build();
    }

    @Bean
    public @NotNull ItemReader<String> jsonFileReader() {
        try {
            @NotNull List<Path> files;
            try (@NotNull Stream<Path> fileStream = Files.list(saveDirectoryPath)) {
                files = fileStream.toList();
            }
            @NotNull Iterator<String> iterator = files.stream()
                    .map(Path::toString)
                    .iterator();

            return () -> iterator.hasNext() ? iterator.next() : null;
        } catch (IOException e) {
            throw new RuntimeException("Failed to list files", e);
        }
    }

    @Bean
    public @NotNull ItemProcessor<String, JsonObject> jsonProcessor() {
        return filePath -> {
            Path path = Paths.get(filePath);
            @NotNull String jsonString = Files.readString(path);
            @NotNull JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
            @NotNull String randomName = IntStream.range(0, 6)
                    .mapToObj(i -> String.valueOf((char) ('A' + new Random().nextInt(26))))
                    .collect(Collectors.joining());
            jsonObject.addProperty("name", randomName);
            jsonObject.addProperty("num", jsonObject.get("num").getAsInt() + 1);
            jsonObject.addProperty("fileName", path.getFileName().toString());
            return jsonObject;
        };
    }

    @Bean
    public @NotNull ItemWriter<JsonObject> jsonWriter() {
        return items -> {
            for (@NotNull JsonObject jsonObject : items) {
                @NotNull String fileName = jsonObject.get("fileName").getAsString();
                jsonObject.remove("fileName");
                @NotNull Path filePath = saveDirectoryPath.resolve(fileName);
                Files.writeString(filePath, gson.toJson(jsonObject));
                classLogger.info("ChunkBatchTest : Updated {}", filePath);
            }
        };
    }
}
