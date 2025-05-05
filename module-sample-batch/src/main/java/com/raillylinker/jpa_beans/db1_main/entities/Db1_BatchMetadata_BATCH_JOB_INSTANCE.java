package com.raillylinker.jpa_beans.db1_main.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Entity
@Table(
        name = "BATCH_JOB_INSTANCE",
        catalog = "batch_metadata"
)
@Comment("BATCH_JOB_INSTANCE")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Db1_BatchMetadata_BATCH_JOB_INSTANCE {
    // [기본 입력값이 존재하는 변수들]


    // ---------------------------------------------------------------------------------------------
    // [입력값 수동 입력 변수들]
    @Id
    @Column(name = "JOB_INSTANCE_ID", nullable = false, columnDefinition = "BIGINT")
    @Comment("JOB_INSTANCE_ID")
    private @NotNull Long jobInstanceId;

    @Column(name = "VERSION", columnDefinition = "BIGINT")
    @Comment("VERSION")
    private @Nullable String version;

    @Column(name = "JOB_NAME", nullable = false, columnDefinition = "VARCHAR(100)")
    @Comment("JOB_NAME")
    private @NotNull String jobName;

    @Column(name = "JOB_KEY", nullable = false, columnDefinition = "VARCHAR(32)")
    @Comment("JOB_KEY")
    private @NotNull String jobKey;


    // ---------------------------------------------------------------------------------------------
    // [@OneToMany 변수들]
    @OneToMany(
            mappedBy = "batchJobInstance",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL}
    )
    private List<Db1_BatchMetadata_BATCH_JOB_EXECUTION> batchJobExecutionList;
}