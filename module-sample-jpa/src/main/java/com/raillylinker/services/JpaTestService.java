package com.raillylinker.services;

import com.raillylinker.configurations.jpa_configs.Db1MainConfig;
import com.raillylinker.controllers.JpaTestController;
import com.raillylinker.jpa_beans.db1_main.entities.Db1_Template_TestData;
import com.raillylinker.jpa_beans.db1_main.repositories.Db1_Template_TestData_Repository;
import com.raillylinker.util_components.CustomUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JpaTestService {
    public JpaTestService(
            // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
            @Value("${spring.profiles.active:default}")
            @NotNull String activeProfile,

            @NotNull CustomUtil customUtil,

            @NotNull Db1_Template_TestData_Repository db1TemplateTestDataRepository
    ) {
        this.activeProfile = activeProfile;
        this.customUtil = customUtil;
        this.db1TemplateTestDataRepository = db1TemplateTestDataRepository;
    }

    // <멤버 변수 공간>
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    private final @NotNull String activeProfile;

    // (스웨거 문서 공개 여부 설정)
    private final @NotNull CustomUtil customUtil;

    private final @NotNull Logger classLogger = LoggerFactory.getLogger(RootService.class);

    private final @NotNull Db1_Template_TestData_Repository db1TemplateTestDataRepository;


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    // (DB Row 입력 테스트 API)
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME)
    public @Nullable JpaTestController.InsertDataSampleOutputVo insertDataSample(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull JpaTestController.InsertDataSampleInputVo inputVo
    ) {
        @NotNull Db1_Template_TestData result = db1TemplateTestDataRepository.save(
                new Db1_Template_TestData(
                        inputVo.getContent(),
                        (int) (Math.random() * 99999999), // Random number between 0 and 99999999
                        ZonedDateTime.parse(inputVo.getDateString(), DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
                                .toLocalDateTime()
                )
        );

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.InsertDataSampleOutputVo(
                result.uid,
                result.content,
                result.randomNum,
                result.testDatetime.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                result.rowCreateDate.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                result.rowUpdateDate.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                result.rowDeleteDateStr
        );
    }


    // ----
    // (DB Rows 삭제 테스트 API)
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME)
    public void deleteRowsSample(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull Boolean deleteLogically
    ) {
        if (deleteLogically) {
            @NotNull List<Db1_Template_TestData> entityList = db1TemplateTestDataRepository.findAllByRowDeleteDateStrOrderByRowCreateDate("/");
            for (@NotNull Db1_Template_TestData entity : entityList) {
                entity.rowDeleteDateStr =
                        LocalDateTime.now().atZone(ZoneId.systemDefault())
                                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"));
                db1TemplateTestDataRepository.save(entity);
            }
        } else {
            db1TemplateTestDataRepository.deleteAll();
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());
    }


    // ----
    // (DB Row 삭제 테스트)
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME)
    public void deleteRowSample(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull Long index,
            @NotNull Boolean deleteLogically
    ) {
        @Nullable Db1_Template_TestData entity = db1TemplateTestDataRepository.findByUidAndRowDeleteDateStr(index, "/");

        if (entity == null) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return;
        }

        if (deleteLogically) {
            entity.rowDeleteDateStr =
                    LocalDateTime.now().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"));
            db1TemplateTestDataRepository.save(entity);
        } else {
            db1TemplateTestDataRepository.deleteById(index);
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());
    }


    ////
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
    public @Nullable JpaTestController.SelectRowsSampleOutputVo selectRowsSample(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        @NotNull List<Db1_Template_TestData> resultEntityList =
                db1TemplateTestDataRepository.findAllByRowDeleteDateStrOrderByRowCreateDate("/");
        @NotNull List<JpaTestController.SelectRowsSampleOutputVo.TestEntityVo> entityVoList = new ArrayList<>();
        for (@NotNull Db1_Template_TestData resultEntity : resultEntityList) {
            entityVoList.add(new JpaTestController.SelectRowsSampleOutputVo.TestEntityVo(
                    resultEntity.uid,
                    resultEntity.content,
                    resultEntity.randomNum,
                    resultEntity.testDatetime.atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.rowCreateDate.atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.rowUpdateDate.atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.rowDeleteDateStr
            ));
        }

        @NotNull List<Db1_Template_TestData> logicalDeleteEntityVoList =
                db1TemplateTestDataRepository.findAllByRowDeleteDateStrNotOrderByRowCreateDate("/");
        @NotNull List<JpaTestController.SelectRowsSampleOutputVo.TestEntityVo> logicalDeleteVoList = new ArrayList<>();
        for (@NotNull Db1_Template_TestData resultEntity : logicalDeleteEntityVoList) {
            logicalDeleteVoList.add(new JpaTestController.SelectRowsSampleOutputVo.TestEntityVo(
                    resultEntity.uid,
                    resultEntity.content,
                    resultEntity.randomNum,
                    resultEntity.testDatetime.atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.rowCreateDate.atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.rowUpdateDate.atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.rowDeleteDateStr
            ));
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.SelectRowsSampleOutputVo(
                entityVoList,
                logicalDeleteVoList
        );
    }
}
