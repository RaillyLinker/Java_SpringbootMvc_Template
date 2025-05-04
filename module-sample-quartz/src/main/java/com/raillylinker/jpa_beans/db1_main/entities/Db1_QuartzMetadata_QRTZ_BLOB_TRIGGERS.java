package com.raillylinker.jpa_beans.db1_main.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

@IdClass(Db1_QuartzMetadata_QRTZ_BLOB_TRIGGERS.QuartzBlobTriggersId.class) // 복합 키 클래스 지정
@Entity
@Table(
        name = "QRTZ_BLOB_TRIGGERS",
        catalog = "quartz_metadata"
)
@Comment("QRTZ_BLOB_TRIGGERS")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Db1_QuartzMetadata_QRTZ_BLOB_TRIGGERS {
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

    @Column(name = "BLOB_DATA", columnDefinition = "BLOB")
    @Comment("BLOB_DATA")
    private @Nullable byte[] blobData;

    // --------------------------------------------------------------------------
    // <중첩 클래스 공간>
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuartzBlobTriggersId implements Serializable {
        private @NotNull String schedName;
        private @NotNull String triggerName;
        private @NotNull String triggerGroup;
    }
}
