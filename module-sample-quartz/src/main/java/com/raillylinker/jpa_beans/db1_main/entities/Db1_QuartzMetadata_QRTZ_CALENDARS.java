package com.raillylinker.jpa_beans.db1_main.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@IdClass(Db1_QuartzMetadata_QRTZ_CALENDARS.QuartzCalendarsTriggersId.class) // 복합 키 클래스 지정
@Entity
@Table(
        name = "QRTZ_CALENDARS",
        catalog = "quartz_metadata"
)
@Comment("QRTZ_CALENDARS")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Db1_QuartzMetadata_QRTZ_CALENDARS {
    // [기본 입력값이 존재하는 변수들]

    // ---------------------------------------------------------------------------------------------
    // [입력값 수동 입력 변수들]
    @Id
    @Column(name = "SCHED_NAME", nullable = false, columnDefinition = "VARCHAR(120)")
    @Comment("SCHED_NAME")
    private @NotNull String schedName;

    @Id
    @Column(name = "CALENDAR_NAME", nullable = false, columnDefinition = "VARCHAR(190)")
    @Comment("CALENDAR_NAME")
    private @NotNull String calendarName;

    @Column(name = "CALENDAR", nullable = false, columnDefinition = "BLOB")
    @Comment("CALENDAR")
    private @NotNull byte[] calendar;

    // --------------------------------------------------------------------------
    // <중첩 클래스 공간>
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuartzCalendarsTriggersId implements Serializable {
        private @NotNull String schedName;
        private @NotNull String calendarName;
    }
}
