package com.raillylinker.jpa_beans.db1_main.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Entity
@Table(
        name = "BATCH_JOB_EXECUTION_CONTEXT",
        catalog = "batch_metadata"
)
@Comment("BATCH_JOB_EXECUTION_CONTEXT")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Db1_BatchMetadata_BATCH_JOB_EXECUTION_CONTEXT {
    // [기본 입력값이 존재하는 변수들]


    // ---------------------------------------------------------------------------------------------
    // [입력값 수동 입력 변수들]
    @Id
    @Column(name = "JOB_EXECUTION_ID", nullable = false, columnDefinition = "BIGINT")
    @Comment("JOB_EXECUTION_ID")
    private @NotNull Long jobExecutionId;

    @Column(name = "SHORT_CONTEXT", nullable = false, columnDefinition = "VARCHAR(2500)")
    @Comment("SHORT_CONTEXT")
    private @NotNull String shortContext;

    @Column(name = "SERIALIZED_CONTEXT", columnDefinition = "TEXT")
    @Comment("SERIALIZED_CONTEXT")
    private @Nullable String serializedContext;


    // ---------------------------------------------------------------------------------------------
    // [@OneToMany 변수들]
}