package com.raillylinker.jpa_beans.db1_main.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

@IdClass(Db1_QuartzMetadata_QRTZ_FIRED_TRIGGERS.QuartzFiredTriggersId.class) // 복합 키 클래스 지정
@Entity
@Table(
        name = "QRTZ_FIRED_TRIGGERS",
        catalog = "quartz_metadata"
)
@Comment("QRTZ_FIRED_TRIGGERS")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Db1_QuartzMetadata_QRTZ_FIRED_TRIGGERS {
    // [기본 입력값이 존재하는 변수들]

    // ---------------------------------------------------------------------------------------------
    // [입력값 수동 입력 변수들]
    @Id
    @Column(name = "SCHED_NAME", nullable = false, columnDefinition = "VARCHAR(120)")
    @Comment("SCHED_NAME")
    private @NotNull String schedName;

    @Id
    @Column(name = "ENTRY_ID", nullable = false, columnDefinition = "VARCHAR(95)")
    @Comment("ENTRY_ID")
    private @NotNull String entryId;

    @Column(name = "TRIGGER_NAME", nullable = false, columnDefinition = "VARCHAR(190)")
    @Comment("TRIGGER_NAME")
    private @NotNull String triggerName;

    @Column(name = "TRIGGER_GROUP", nullable = false, columnDefinition = "VARCHAR(190)")
    @Comment("TRIGGER_GROUP")
    private @NotNull String triggerGroup;

    @Column(name = "INSTANCE_NAME", nullable = false, columnDefinition = "VARCHAR(190)")
    @Comment("INSTANCE_NAME")
    private @NotNull String instanceName;

    @Column(name = "FIRED_TIME", nullable = false, columnDefinition = "BIGINT")
    @Comment("FIRED_TIME")
    private @NotNull Long firedTime;

    @Column(name = "PRIORITY", nullable = false, columnDefinition = "INT")
    @Comment("PRIORITY")
    private @NotNull Integer priority;

    @Column(name = "STATE", nullable = false, columnDefinition = "VARCHAR(16)")
    @Comment("STATE")
    private @NotNull String state;

    @Column(name = "JOB_NAME", columnDefinition = "VARCHAR(190)")
    @Comment("JOB_NAME")
    private @Nullable String jobName;

    @Column(name = "JOB_GROUP", columnDefinition = "VARCHAR(190)")
    @Comment("JOB_GROUP")
    private @Nullable String jobGroup;

    @Column(name = "IS_NONCONCURRENT", columnDefinition = "VARCHAR(1)")
    @Comment("IS_NONCONCURRENT")
    private @Nullable String isNonConcurrent;

    @Column(name = "REQUESTS_RECOVERY", columnDefinition = "VARCHAR(1)")
    @Comment("REQUESTS_RECOVERY")
    private @Nullable String requestsRecovery;

    // --------------------------------------------------------------------------
    // <중첩 클래스 공간>
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuartzFiredTriggersId implements Serializable {
        private @NotNull String schedName;
        private @NotNull String entryId;
    }
}
