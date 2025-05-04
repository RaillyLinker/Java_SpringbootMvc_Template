package com.raillylinker.jpa_beans.db1_main.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

@IdClass(Db1_QuartzMetadata_QRTZ_TRIGGERS.QuartzTriggersId.class) // 복합 키 클래스 지정
@Entity
@Table(
        name = "QRTZ_TRIGGERS",
        catalog = "quartz_metadata"
)
@Comment("QRTZ_TRIGGERS")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Db1_QuartzMetadata_QRTZ_TRIGGERS {
    // [기본 입력값이 존재하는 변수들]

    // ---------------------------------------------------------------------------------------------
    // [입력값 수동 입력 변수들]
    @Id
    @Column(name = "SCHED_NAME", nullable = false, columnDefinition = "VARCHAR(120)")
    @Comment("SCHED_NAME")
    private @NotNull String schedName;

    @Id
    @Column(name = "TRIGGER_NAME", nullable = false, columnDefinition = "VARCHAR(190)")
    @Comment("TRIGGER_NAME")
    private @NotNull String triggerName;

    @Id
    @Column(name = "TRIGGER_GROUP", nullable = false, columnDefinition = "VARCHAR(190)")
    @Comment("TRIGGER_GROUP")
    private @NotNull String triggerGroup;

    @ManyToOne
    @JoinColumns(
            {
                    @JoinColumn(name = "JOB_NAME", referencedColumnName = "JOB_NAME"),
                    @JoinColumn(name = "JOB_GROUP", referencedColumnName = "JOB_GROUP")
            }
    )
    @Comment("JOB_NAME, JOB_GROUP")
    private @NotNull Db1_QuartzMetadata_QRTZ_JOB_DETAILS jobDetails;

    @Column(name = "DESCRIPTION", columnDefinition = "VARCHAR(250)")
    @Comment("DESCRIPTION")
    private @Nullable String description;

    @Column(name = "NEXT_FIRE_TIME", columnDefinition = "BIGINT")
    @Comment("NEXT_FIRE_TIME")
    private @Nullable Long nextFireTime;

    @Column(name = "PREV_FIRE_TIME", columnDefinition = "BIGINT")
    @Comment("PREV_FIRE_TIME")
    private @Nullable Long prevFireTime;

    @Column(name = "PRIORITY", columnDefinition = "INT")
    @Comment("PRIORITY")
    private @Nullable Integer priority;

    @Column(name = "TRIGGER_STATE", nullable = false, columnDefinition = "VARCHAR(16)")
    @Comment("TRIGGER_STATE")
    private @NotNull String triggerState;

    @Column(name = "TRIGGER_TYPE", nullable = false, columnDefinition = "VARCHAR(8)")
    @Comment("TRIGGER_TYPE")
    private @NotNull String triggerType;

    @Column(name = "START_TIME", nullable = false, columnDefinition = "BIGINT")
    @Comment("START_TIME")
    private @NotNull Long startTime;

    @Column(name = "END_TIME", columnDefinition = "BIGINT")
    @Comment("END_TIME")
    private @Nullable Long endTime;

    @Column(name = "CALENDAR_NAME", columnDefinition = "VARCHAR(190)")
    @Comment("CALENDAR_NAME")
    private @Nullable String calendarName;

    @Column(name = "MISFIRE_INSTR", columnDefinition = "SMALLINT")
    @Comment("MISFIRE_INSTR")
    private @Nullable Short misfireInstr;

    @Column(name = "JOB_DATA", columnDefinition = "BLOB")
    @Comment("JOB_DATA")
    private @Nullable byte[] jobData;


    // --------------------------------------------------------------------------
    // <중첩 클래스 공간>
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuartzTriggersId implements Serializable {
        private @NotNull String schedName;
        private @NotNull String triggerName;
        private @NotNull String triggerGroup;
    }
}
