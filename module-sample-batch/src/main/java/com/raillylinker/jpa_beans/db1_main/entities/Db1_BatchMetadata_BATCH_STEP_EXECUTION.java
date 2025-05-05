package com.raillylinker.jpa_beans.db1_main.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "BATCH_STEP_EXECUTION",
        catalog = "batch_metadata"
)
@Comment("BATCH_STEP_EXECUTION")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Db1_BatchMetadata_BATCH_STEP_EXECUTION {
    // [기본 입력값이 존재하는 변수들]
    @Column(name = "CREATE_TIME", nullable = false, columnDefinition = "DATETIME(6)")
    @CreationTimestamp
    @Comment("CREATE_TIME")
    private LocalDateTime createTime;


    // ---------------------------------------------------------------------------------------------
    // [입력값 수동 입력 변수들]
    @Id
    @Column(name = "STEP_EXECUTION_ID", nullable = false, columnDefinition = "BIGINT")
    @Comment("STEP_EXECUTION_ID")
    private @NotNull Long stepExecutionId;

    @Column(name = "VERSION", nullable = false, columnDefinition = "BIGINT")
    @Comment("VERSION")
    private @NotNull String version;

    @Column(name = "STEP_NAME", nullable = false, columnDefinition = "VARCHAR(100)")
    @Comment("STEP_NAME")
    private @NotNull String stepName;

    @ManyToOne
    @JoinColumn(name = "JOB_EXECUTION_ID", nullable = false)
    @Comment("JOB_EXECUTION_ID")
    private @NotNull Db1_BatchMetadata_BATCH_JOB_EXECUTION batchJobExecution;

    @Column(name = "START_TIME", columnDefinition = "DATETIME(6)")
    @Comment("START_TIME")
    private @Nullable LocalDateTime startTime;

    @Column(name = "END_TIME", columnDefinition = "DATETIME(6)")
    @Comment("END_TIME")
    private @Nullable LocalDateTime endTime;

    @Column(name = "STATUS", columnDefinition = "VARCHAR(10)")
    @Comment("STATUS")
    private @Nullable String status;

    @Column(name = "COMMIT_COUNT", columnDefinition = "BIGINT")
    @Comment("COMMIT_COUNT")
    private @Nullable Long commitCount;

    @Column(name = "READ_COUNT", columnDefinition = "BIGINT")
    @Comment("READ_COUNT")
    private @Nullable Long readCount;

    @Column(name = "FILTER_COUNT", columnDefinition = "BIGINT")
    @Comment("FILTER_COUNT")
    private @Nullable Long filterCount;

    @Column(name = "WRITE_COUNT", columnDefinition = "BIGINT")
    @Comment("WRITE_COUNT")
    private @Nullable Long writeCount;

    @Column(name = "READ_SKIP_COUNT", columnDefinition = "BIGINT")
    @Comment("READ_SKIP_COUNT")
    private @Nullable Long readSkipCount;

    @Column(name = "WRITE_SKIP_COUNT", columnDefinition = "BIGINT")
    @Comment("WRITE_SKIP_COUNT")
    private @Nullable Long writeSkipCount;

    @Column(name = "PROCESS_SKIP_COUNT", columnDefinition = "BIGINT")
    @Comment("PROCESS_SKIP_COUNT")
    private @Nullable Long processSkipCount;

    @Column(name = "ROLLBACK_COUNT", columnDefinition = "BIGINT")
    @Comment("ROLLBACK_COUNT")
    private @Nullable Long rollbackCount;

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
}