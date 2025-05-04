package com.raillylinker.jpa_beans.db1_main.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.List;

@IdClass(Db1_QuartzMetadata_QRTZ_JOB_DETAILS.QuartzJobDetailsId.class) // 복합 키 클래스 지정
@Entity
@Table(
        name = "QRTZ_JOB_DETAILS",
        catalog = "quartz_metadata"
)
@Comment("QRTZ_JOB_DETAILS")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Db1_QuartzMetadata_QRTZ_JOB_DETAILS {
    // [기본 입력값이 존재하는 변수들]

    // ---------------------------------------------------------------------------------------------
    // [입력값 수동 입력 변수들]
    @Id
    @Column(name = "SCHED_NAME", nullable = false, columnDefinition = "VARCHAR(120)")
    @Comment("SCHED_NAME")
    private @NotNull String schedName;

    @Id
    @Column(name = "JOB_NAME", nullable = false, columnDefinition = "VARCHAR(190)")
    @Comment("JOB_NAME")
    private @NotNull String jobName;

    @Id
    @Column(name = "JOB_GROUP", nullable = false, columnDefinition = "VARCHAR(190)")
    @Comment("JOB_GROUP")
    private @NotNull String jobGroup;

    @Column(name = "DESCRIPTION", columnDefinition = "VARCHAR(250)")
    @Comment("DESCRIPTION")
    private @Nullable String description;

    @Column(name = "JOB_CLASS_NAME", nullable = false, columnDefinition = "VARCHAR(250)")
    @Comment("JOB_CLASS_NAME")
    private @NotNull String jobClassName;

    @Column(name = "IS_DURABLE", nullable = false, columnDefinition = "VARCHAR(1)")
    @Comment("IS_DURABLE")
    private @NotNull String isDurable;

    @Column(name = "IS_NONCONCURRENT", nullable = false, columnDefinition = "VARCHAR(1)")
    @Comment("IS_NONCONCURRENT")
    private @NotNull String isNonConcurrent;

    @Column(name = "IS_UPDATE_DATA", nullable = false, columnDefinition = "VARCHAR(1)")
    @Comment("IS_UPDATE_DATA")
    private @NotNull String isUpdateData;

    @Column(name = "REQUESTS_RECOVERY", nullable = false, columnDefinition = "VARCHAR(1)")
    @Comment("REQUESTS_RECOVERY")
    private @NotNull String requestsRecovery;

    @Column(name = "JOB_DATA", columnDefinition = "BLOB")
    @Comment("JOB_DATA")
    private @Nullable byte[] jobData;


    // ---------------------------------------------------------------------------------------------
    // [@OneToMany 변수들]
    @OneToMany(
            mappedBy = "jobDetails",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL}
    )
    private List<Db1_QuartzMetadata_QRTZ_TRIGGERS> qrtzTriggersList;


    // --------------------------------------------------------------------------
    // <중첩 클래스 공간>
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuartzJobDetailsId implements Serializable {
        private @NotNull String schedName;
        private @NotNull String jobName;
        private @NotNull String jobGroup;
    }
}
