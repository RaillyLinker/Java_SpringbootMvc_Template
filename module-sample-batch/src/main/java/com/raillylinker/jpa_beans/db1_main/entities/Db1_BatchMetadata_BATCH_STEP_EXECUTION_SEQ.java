package com.raillylinker.jpa_beans.db1_main.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(
        name = "BATCH_STEP_EXECUTION_SEQ",
        catalog = "batch_metadata"
)
@Comment("BATCH_STEP_EXECUTION_SEQ")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Db1_BatchMetadata_BATCH_STEP_EXECUTION_SEQ {
    // [기본 입력값이 존재하는 변수들]
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    @Comment("행 고유값")
    private Long uid;


    // ---------------------------------------------------------------------------------------------
    // [입력값 수동 입력 변수들]
    @Column(name = "ID", nullable = false, columnDefinition = "BIGINT")
    @Comment("ID")
    private @NotNull Long id;

    @Column(name = "UNIQUE_KEY", nullable = false, columnDefinition = "CHAR(1)", unique = true)
    @Comment("UNIQUE_KEY")
    private @NotNull Character uniqueKey;


    // ---------------------------------------------------------------------------------------------
    // [@OneToMany 변수들]
}