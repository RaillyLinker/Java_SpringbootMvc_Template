package com.raillylinker.jpa_beans.db1_main.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jetbrains.annotations.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "test_data",
        catalog = "template"
)
@Comment("테스트 정보 테이블(논리적 삭제 적용)")
@NoArgsConstructor
public class Db1_Template_TestData {
    // [기본 입력값이 존재하는 변수들]
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    @Comment("행 고유값")
    public @NotNull Long uid = 0L;

    @Column(name = "row_create_date", nullable = false, columnDefinition = "DATETIME(3)")
    @CreationTimestamp
    @Comment("행 생성일")
    public @NotNull LocalDateTime rowCreateDate = LocalDateTime.MIN;

    @Column(name = "row_update_date", nullable = false, columnDefinition = "DATETIME(3)")
    @UpdateTimestamp
    @Comment("행 수정일")
    public @NotNull LocalDateTime rowUpdateDate = LocalDateTime.MIN;

    @Column(name = "row_delete_date_str", nullable = false, columnDefinition = "VARCHAR(50)")
    @ColumnDefault("'/'")
    @Comment("행 삭제일(yyyy_MM_dd_T_HH_mm_ss_SSS_z, 삭제되지 않았다면 /)")
    public @NotNull String rowDeleteDateStr = "/";


    // ---------------------------------------------------------------------------------------------
    // [입력값 수동 입력 변수들]
    public Db1_Template_TestData(
            @NotNull String content,
            @NotNull Integer randomNum,
            @NotNull LocalDateTime testDatetime
    ) {
        this.content = content;
        this.randomNum = randomNum;
        this.testDatetime = testDatetime;
    }

    @Column(name = "content", nullable = false, columnDefinition = "VARCHAR(255)")
    @Comment("테스트 본문")
    public @NotNull String content = "";

    @Column(name = "random_num", nullable = false, columnDefinition = "INT")
    @Comment("테스트 랜덤 번호")
    public @NotNull Integer randomNum = 0;

    @Column(name = "test_datetime", nullable = false, columnDefinition = "DATETIME(3)")
    @Comment("테스트용 일시 데이터")
    public @NotNull LocalDateTime testDatetime = LocalDateTime.MIN;


    // ---------------------------------------------------------------------------------------------
    // [@OneToMany 변수들]
}