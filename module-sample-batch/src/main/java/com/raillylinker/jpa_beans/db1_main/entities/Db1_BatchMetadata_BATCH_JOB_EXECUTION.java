package com.raillylinker.jpa_beans.db1_main.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name = "BATCH_JOB_EXECUTION",
        catalog = "batch_metadata"
)
@Comment("BATCH_JOB_EXECUTION")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Db1_BatchMetadata_BATCH_JOB_EXECUTION {
    // [기본 입력값이 존재하는 변수들]


    // ---------------------------------------------------------------------------------------------
    // [입력값 수동 입력 변수들]
    @Id
    @Column(name = "JOB_EXECUTION_ID", nullable = false, columnDefinition = "BIGINT")
    @Comment("JOB_EXECUTION_ID")
    private @NotNull Long jobExecutionId;

    @Column(name = "VERSION", columnDefinition = "BIGINT")
    @Comment("VERSION")
    private @Nullable String version;

    @ManyToOne
    @JoinColumn(name = "JOB_INSTANCE_ID", nullable = false)
    @Comment("JOB_INSTANCE_ID")
    private @NotNull Db1_BatchMetadata_BATCH_JOB_INSTANCE batchJobInstance;

    @Column(name = "CREATE_TIME", nullable = false, columnDefinition = "DATETIME(6)")
    @Comment("CREATE_TIME")
    private @NotNull LocalDateTime createTime;

    @Column(name = "START_TIME", columnDefinition = "DATETIME(6)")
    @Comment("START_TIME")
    private @Nullable LocalDateTime startTime;

    @Column(name = "END_TIME", columnDefinition = "DATETIME(6)")
    @Comment("END_TIME")
    private @Nullable LocalDateTime endTime;

    @Column(name = "STATUS", columnDefinition = "VARCHAR(10)")
    @Comment("STATUS")
    private @Nullable String status;

    @Column(name = "EXIT_CODE", columnDefinition = "VARCHAR(2500)")
    @Comment("EXIT_CODE")
    private @Nullable String exitCode;

    @Column(name = "EXIT_MESSAGE", columnDefinition = "VARCHAR(2500)")
    @Comment("EXIT_MESSAGE")
    private @Nullable String exitMessage;

    @Column(name = "LAST_UPDATED", columnDefinition = "DATETIME(6)")
    @Comment("LAST_UPDATED")
    private @Nullable LocalDateTime lastUpdated;


    // ---------------------------------------------------------------------------------------------
    // [@OneToMany 변수들]
    @OneToMany(
            mappedBy = "batchJobExecution",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL}
    )
    private List<Db1_BatchMetadata_BATCH_JOB_EXECUTION_PARAMS> batchJobExecutionParamsList;

    @OneToMany(
            mappedBy = "batchJobExecution",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL}
    )
    private List<Db1_BatchMetadata_BATCH_STEP_EXECUTION> batchStepExecutionList;
}