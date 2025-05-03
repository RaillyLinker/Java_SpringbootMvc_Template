package com.raillylinker.services;

import com.raillylinker.configurations.SecurityConfig.AuthTokenFilterTotalAuth;
import com.raillylinker.configurations.jpa_configs.Db1MainConfig;
import com.raillylinker.jpa_beans.db1_main.entities.Db1_RaillyLinkerCompany_TotalAuthMember;
import com.raillylinker.jpa_beans.db1_main.repositories.Db1_RaillyLinkerCompany_TotalAuthMember_Repository;
import com.raillylinker.util_components.CustomUtil;
import com.raillylinker.util_components.JwtTokenUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class SecurityService {
    public SecurityService(
            // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
            @Value("${spring.profiles.active:default}")
            @NotNull String activeProfile,

            @NotNull CustomUtil customUtil,
            @NotNull JwtTokenUtil jwtTokenUtil,
            @NotNull AuthTokenFilterTotalAuth authTokenFilterTotalAuth,

            @NotNull Db1_RaillyLinkerCompany_TotalAuthMember_Repository db1RaillyLinkerCompanyTotalAuthMemberRepository
    ) {
        this.activeProfile = activeProfile;
        this.customUtil = customUtil;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authTokenFilterTotalAuth = authTokenFilterTotalAuth;
        this.db1RaillyLinkerCompanyTotalAuthMemberRepository = db1RaillyLinkerCompanyTotalAuthMemberRepository;
    }

    // <멤버 변수 공간>
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    private final @NotNull String activeProfile;

    // (스웨거 문서 공개 여부 설정)
    private final @NotNull CustomUtil customUtil;

    private final @NotNull Logger classLogger = LoggerFactory.getLogger(SecurityService.class);

    private final @NotNull JwtTokenUtil jwtTokenUtil;
    private final @NotNull AuthTokenFilterTotalAuth authTokenFilterTotalAuth;

    private final @NotNull Db1_RaillyLinkerCompany_TotalAuthMember_Repository db1RaillyLinkerCompanyTotalAuthMemberRepository;


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    // (비 로그인 접속 테스트)
    public @Nullable String noLoggedInAccessTest(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        httpServletResponse.setStatus(HttpStatus.OK.value());
        return "Access OK";
    }


    // ----
    // (로그인 진입 테스트 <>)
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
    public @Nullable String loggedInAccessTest(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull String authorization
    ) throws Exception {
        @NotNull Long memberUid = jwtTokenUtil.getMemberUid(
                authorization.split(" ")[1].trim(),
                authTokenFilterTotalAuth.authJwtClaimsAes256InitializationVector,
                authTokenFilterTotalAuth.authJwtClaimsAes256EncryptionKey
        );

        // 멤버 데이터 조회
        @NotNull Db1_RaillyLinkerCompany_TotalAuthMember memberEntity =
                Objects.requireNonNull(db1RaillyLinkerCompanyTotalAuthMemberRepository.findByUidAndRowDeleteDateStr(memberUid, "/"));

        classLogger.info("Member Id : {}", memberEntity.getAccountId());

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return "Member No.$memberUid : Test Success";
    }
}
