package com.raillylinker.quartz_components;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.jetbrains.annotations.NotNull;

@Component
public class Test2QuartzConfig {
    public static final @NotNull String QUARTZ_NAME = "Test2Quartz";

    @Bean(name = QUARTZ_NAME + "_Trigger")
    public @NotNull Trigger jobTrigger() {
        return TriggerBuilder
                .newTrigger()
                // 트리거 ID 설정 (같은 ID는 주기당 한 번씩만 동작함 = 클러스터 설정 시 클러스터당 한 번)
                .withIdentity(QUARTZ_NAME + "_Trigger")
                // Job Detail 설정
                .forJob(testJobDetail())
                // 실행 시간 설정 (어플리케이션 실행 시점에 실행)
                .startNow()
                // 반복 스케줄 설정
                // 4초마다
                .withSchedule(CronScheduleBuilder.cronSchedule("*/4 * * * * ?"))
                // 매일 0시 (주석 처리된 부분)
                // .withSchedule(CronScheduleBuilder.cronSchedule("0 * * * * ?"))
                .build();
    }

    @Bean(name = QUARTZ_NAME + "_Job")
    public @NotNull JobDetail testJobDetail() {
        return JobBuilder.newJob(TestQuartzJob.class)
                .withIdentity(QUARTZ_NAME + "_Job")
                .storeDurably()
                .build();
    }

    public static class TestQuartzJob implements Job {
        @Override
        public void execute(@NotNull JobExecutionContext context) {
            System.out.println("Run " + QUARTZ_NAME);
        }
    }
}
