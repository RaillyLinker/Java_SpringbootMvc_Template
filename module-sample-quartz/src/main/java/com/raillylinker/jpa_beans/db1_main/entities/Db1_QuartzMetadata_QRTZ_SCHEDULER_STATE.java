package com.raillylinker.jpa_beans.db1_main.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@IdClass(Db1_QuartzMetadata_QRTZ_SCHEDULER_STATE.QuartzSchedulerStateId.class) // 복합 키 클래스 지정
@Entity
@Table(
        name = "QRTZ_SCHEDULER_STATE",
        catalog = "quartz_metadata"
)
@Comment("QRTZ_SCHEDULER_STATE")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Db1_QuartzMetadata_QRTZ_SCHEDULER_STATE {
    // [기본 입력값이 존재하는 변수들]

    // ---------------------------------------------------------------------------------------------
    // [입력값 수동 입력 변수들]
    @Id
    @Column(name = "SCHED_NAME", nullable = false, columnDefinition = "VARCHAR(120)")
    @Comment("SCHED_NAME")
    private @NotNull String schedName;

    @Id
    @Column(name = "INSTANCE_NAME", nullable = false, columnDefinition = "VARCHAR(190)")
    @Comment("INSTANCE_NAME")
    private @NotNull String instanceName;

    @Column(name = "LAST_CHECKIN_TIME", nullable = false, columnDefinition = "BIGINT")
    @Comment("LAST_CHECKIN_TIME")
    private @NotNull Long lastCheckinTime;

    @Column(name = "CHECKIN_INTERVAL", nullable = false, columnDefinition = "BIGINT")
    @Comment("CHECKIN_INTERVAL")
    private @NotNull Long checkinInterval;

    // --------------------------------------------------------------------------
    // <중첩 클래스 공간>
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuartzSchedulerStateId implements Serializable {
        private @NotNull String schedName;
        private @NotNull String instanceName;
    }
}
