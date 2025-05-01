package com.raillylinker.jpa_beans.db1_main.entities;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Comment;

@Entity
@Table(
        name = "just_boolean_test",
        catalog = "template"
)
@Comment("Boolean 값 반환 예시만을 위한 테이블")
public class Db1_Template_JustBooleanTest {
    public Db1_Template_JustBooleanTest() {
    }

    // [기본 입력값이 존재하는 변수들]
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    @Comment("행 고유값")
    public Long uid;


    // ---------------------------------------------------------------------------------------------
    // [입력값 수동 입력 변수들]
    public Db1_Template_JustBooleanTest(
            @Valid @NotNull @org.jetbrains.annotations.NotNull
            Boolean boolValue
    ) {
        this.boolValue = boolValue;
    }

    @Column(name = "bool_value", nullable = false, columnDefinition = "BIT(1)")
    @Comment("bool 값")
    public Boolean boolValue;


    // ---------------------------------------------------------------------------------------------
    // [@OneToMany 변수들]
}