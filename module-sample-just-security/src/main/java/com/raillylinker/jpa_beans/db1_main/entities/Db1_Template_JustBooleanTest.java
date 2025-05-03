package com.raillylinker.jpa_beans.db1_main.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(
        name = "just_boolean_test",
        catalog = "template"
)
@Comment("Boolean 값 반환 예시만을 위한 테이블")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Db1_Template_JustBooleanTest {
    // [기본 입력값이 존재하는 변수들]
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    @Comment("행 고유값")
    private Long uid;


    // ---------------------------------------------------------------------------------------------
    // [입력값 수동 입력 변수들]
    @Column(name = "bool_value", nullable = false, columnDefinition = "BIT(1)")
    @Comment("bool 값")
    private @NotNull Boolean boolValue;


    // ---------------------------------------------------------------------------------------------
    // [@OneToMany 변수들]
}