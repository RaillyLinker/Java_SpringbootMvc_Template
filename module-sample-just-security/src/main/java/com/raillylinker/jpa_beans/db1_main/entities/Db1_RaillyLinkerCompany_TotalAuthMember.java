package com.raillylinker.jpa_beans.db1_main.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name = "total_auth_member",
        catalog = "railly_linker_company",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"account_id", "row_delete_date_str"})
        }
)
@Comment("통합 로그인 계정 회원 정보 테이블")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Db1_RaillyLinkerCompany_TotalAuthMember {
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

    @Column(name = "account_id", nullable = false, columnDefinition = "VARCHAR(100)")
    @Comment("계정 아이디")
    private @NotNull String accountId;

    @Column(name = "account_password", nullable = true, columnDefinition = "VARCHAR(100)")
    @Comment("계정 로그인시 사용하는 비밀번호 (계정 아이디, 이메일, 전화번호 로그인에 모두 사용됨. OAuth2 만 등록했다면 null)")
    private @Nullable String accountPassword;


    // ---------------------------------------------------------------------------------------------
    // [@OneToMany 변수들]
    // Row 삭제시 삭제 처리
    @OneToMany(
            mappedBy = "totalAuthMember",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL}
    )
    private List<Db1_RaillyLinkerCompany_TotalAuthMemberRole> totalAuthMemberRoleList;

    // Row 삭제시 삭제 처리
    @OneToMany(
            mappedBy = "totalAuthMember",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL}
    )
    private List<Db1_RaillyLinkerCompany_TotalAuthMemberProfile> totalAuthMemberProfileList;

    // Row 삭제시 삭제 처리
    @OneToMany(
            mappedBy = "totalAuthMember",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL}
    )
    private List<Db1_RaillyLinkerCompany_TotalAuthMemberPhone> totalAuthMemberPhoneList;

    // Row 삭제시 삭제 처리
    @OneToMany(
            mappedBy = "totalAuthMember",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL}
    )
    private List<Db1_RaillyLinkerCompany_TotalAuthMemberEmail> totalAuthMemberEmailList;
}