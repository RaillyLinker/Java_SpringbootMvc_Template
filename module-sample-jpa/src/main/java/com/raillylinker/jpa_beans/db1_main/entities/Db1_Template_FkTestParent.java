package com.raillylinker.jpa_beans.db1_main.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jetbrains.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name = "fk_test_parent",
        catalog = "template"
)
@Comment("Foreign Key 테스트용 테이블 (부모 테이블)")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Db1_Template_FkTestParent {
    // [기본 입력값이 존재하는 변수들]
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    @Comment("행 고유값")
    private Long uid;

    @Column(name = "row_create_date", nullable = false, columnDefinition = "DATETIME(3)")
    @CreationTimestamp
    @Comment("행 생성일")
    private LocalDateTime rowCreateDate;

    @Column(name = "row_update_date", nullable = false, columnDefinition = "DATETIME(3)")
    @UpdateTimestamp
    @Comment("행 수정일")
    private LocalDateTime rowUpdateDate;


    // ---------------------------------------------------------------------------------------------
    // [입력값 수동 입력 변수들]
    @Column(name = "row_delete_date_str", nullable = false, columnDefinition = "VARCHAR(50)")
    @ColumnDefault("'/'")
    @Comment("행 삭제일(yyyy_MM_dd_T_HH_mm_ss_SSS_z, 삭제되지 않았다면 /)")
    private @NotNull String rowDeleteDateStr;

    @Column(name = "parent_name", nullable = false, columnDefinition = "VARCHAR(255)")
    @Comment("부모 테이블 이름")
    private @NotNull String parentName;


    // ---------------------------------------------------------------------------------------------
    // [@OneToMany 변수들]
    @OneToMany(
            // mappedBy 는 자식 테이블 클래스의 Join 정보를 나타내는 변수명을 적어주면 됩니다. (변수명이 다르면 에러가 납니다.)
            // Fk 제약은 mappedBy 를 한 대상 테이블에 생성됩니다.
            mappedBy = "fkTestParent",
            // 이것에 해당하는 정보는 아래 변수를 get 했을 시점에 데이터베이스에서 가져오도록 설정
            fetch = FetchType.LAZY,
            /*
                 본 부모 테이블이 삭제 등 변경 되었을 때 아래 자식 테이블들에 대한 처리 방침.
                 CascadeType.ALL 설정이 된 상태로 본 부모 테이블이 삭제되면 해당 설정이 달린 자식 테이블들이 모두 삭제됩니다.
                 외례키가 테이블 존재에 필수적인 경우는 cascade 설정을 해도 되지만,
                 아니라면 아래 설정을 적용하지 말고, 삭제시에는 수동으로 기본값으로 변경을 수행해주어야 합니다.
                 ex : 회원 테이블이 대표글 정보를 저장하기 위하여 글 테이블을 참조하고 있을 때,
                     cascade 설정이 되어있다면, 그저 글을 하나 지웠을 뿐인데 회원 정보가 날아가는 상황이 벌어집니다.
                 만약 cascade 설정이 안 된 상태로 자식 테이블이 존재하는 부모 테이블만 제거하려 할 때엔 무결성 에러가 발생합니다.
             */
            cascade = {CascadeType.ALL}
    )
    private List<Db1_Template_FkTestManyToOneChild> fkTestManyToOneChildList;
}