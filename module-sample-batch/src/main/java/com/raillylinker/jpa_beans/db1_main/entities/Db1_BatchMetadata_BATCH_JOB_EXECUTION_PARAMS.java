package com.raillylinker.jpa_beans.db1_main.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Entity
@Table(
        name = "BATCH_JOB_EXECUTION_PARAMS",
        catalog = "batch_metadata"
)
@Comment("BATCH_JOB_EXECUTION_PARAMS")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Db1_BatchMetadata_BATCH_JOB_EXECUTION_PARAMS {
    // [기본 입력값이 존재하는 변수들]
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    @Comment("행 고유값")
    private Long uid;


    // ---------------------------------------------------------------------------------------------
    // [입력값 수동 입력 변수들]
    @ManyToOne
    @JoinColumn(name = "JOB_EXECUTION_ID", nullable = false)
    @Comment("JOB_EXECUTION_ID")
    private @NotNull Db1_BatchMetadata_BATCH_JOB_EXECUTION batchJobExecution;

    @Column(name = "PARAMETER_NAME", nullable = false, columnDefinition = "VARCHAR(100)")
    @Comment("PARAMETER_NAME")
    private @NotNull String parameterName;

    @Column(name = "PARAMETER_TYPE", nullable = false, columnDefinition = "VARCHAR(100)")
    @Comment("PARAMETER_TYPE")
    private @NotNull String parameterType;

    @Column(name = "PARAMETER_VALUE", columnDefinition = "VARCHAR(2500)")
    @Comment("PARAMETER_VALUE")
    private @Nullable String parameterValue;

    @Column(name = "IDENTIFYING", nullable = false, columnDefinition = "CHAR(1)")
    @Comment("IDENTIFYING")
    private @NotNull Character identifying;


    // ---------------------------------------------------------------------------------------------
    // [@OneToMany 변수들]
}