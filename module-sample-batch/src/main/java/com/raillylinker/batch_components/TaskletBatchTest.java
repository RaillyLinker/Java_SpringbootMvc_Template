package com.raillylinker.batch_components;

import com.raillylinker.configurations.jpa_configs.Db1MainConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.jetbrains.annotations.NotNull;

@Component
public class TaskletBatchTest {
    public TaskletBatchTest(
            @NotNull JobRepository jobRepository,
            @Qualifier(Db1MainConfig.TRANSACTION_NAME)
            @NotNull PlatformTransactionManager transactionManager
    ) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    // 배치 Job 이름
    public static final @NotNull String BATCH_JOB_NAME = "TaskletBatchTest";

    private static final @NotNull Logger classLogger = LoggerFactory.getLogger(TaskletBatchTest.class);

    private final @NotNull JobRepository jobRepository;
    private final @NotNull PlatformTransactionManager transactionManager;


    // ---------------------------------------------------------------------------------------------
    // [Batch Job 및 하위 작업 작성]
    // BatchConfig 하나당 하나의 Job 을 가져야 합니다.

    // (Batch Job)
    @Bean(name = BATCH_JOB_NAME)
    public @NotNull Job batchJob() {
        return new JobBuilder(BATCH_JOB_NAME + "_batchJob", jobRepository)
                .start(taskletTestStep())
                .build();
    }

    // (Tasklet 테스트 Step)
    public @NotNull Step taskletTestStep() {
        return new StepBuilder(BATCH_JOB_NAME + "_taskletTestStep", jobRepository)
                .tasklet(justLoggingTasklet(), transactionManager)
                .build();
    }

    // (단순히 로깅하는 Tasklet)
    public @NotNull Tasklet justLoggingTasklet() {
        return (contribution, chunkContext) -> {
            classLogger.info("TaskletBatchTest : Tasklet Batch Test Complete!");
            return RepeatStatus.FINISHED;
        };
    }
}
