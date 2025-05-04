package com.raillylinker.jpa_beans.db1_main.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.math.BigDecimal;

@IdClass(Db1_QuartzMetadata_QRTZ_SIMPROP_TRIGGERS.QuartzSimpropTriggersId.class) // 복합 키 클래스 지정
@Entity
@Table(
        name = "QRTZ_SIMPROP_TRIGGERS",
        catalog = "quartz_metadata"
)
@Comment("QRTZ_SIMPROP_TRIGGERS")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Db1_QuartzMetadata_QRTZ_SIMPROP_TRIGGERS {
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

    @Column(name = "STR_PROP_1", columnDefinition = "VARCHAR(512)")
    @Comment("STR_PROP_1")
    private @Nullable String strProp1;

    @Column(name = "STR_PROP_2", columnDefinition = "VARCHAR(512)")
    @Comment("STR_PROP_2")
    private @Nullable String strProp2;

    @Column(name = "STR_PROP_3", columnDefinition = "VARCHAR(512)")
    @Comment("STR_PROP_3")
    private @Nullable String strProp3;

    @Column(name = "INT_PROP_1", columnDefinition = "INT")
    @Comment("INT_PROP_1")
    private @Nullable Integer intProp1;

    @Column(name = "INT_PROP_2", columnDefinition = "INT")
    @Comment("INT_PROP_2")
    private @Nullable Integer intProp2;

    @Column(name = "LONG_PROP_1", columnDefinition = "BIGINT")
    @Comment("LONG_PROP_1")
    private @Nullable Long longProp1;

    @Column(name = "LONG_PROP_2", columnDefinition = "BIGINT")
    @Comment("LONG_PROP_2")
    private @Nullable Long longProp2;

    @Column(name = "DEC_PROP_1", columnDefinition = "DECIMAL(13,4)")
    @Comment("DEC_PROP_1")
    private @Nullable BigDecimal decProp1;

    @Column(name = "DEC_PROP_2", columnDefinition = "DECIMAL(13,4)")
    @Comment("DEC_PROP_2")
    private @Nullable BigDecimal decProp2;

    @Column(name = "BOOL_PROP_1", columnDefinition = "VARCHAR(1)")
    @Comment("BOOL_PROP_1")
    private @Nullable String boolProp1;

    @Column(name = "BOOL_PROP_2", columnDefinition = "VARCHAR(1)")
    @Comment("BOOL_PROP_2")
    private @Nullable String boolProp2;


    // --------------------------------------------------------------------------
    // <중첩 클래스 공간>
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuartzSimpropTriggersId implements Serializable {
        private @NotNull String schedName;
        private @NotNull String triggerName;
        private @NotNull String triggerGroup;
    }
}
