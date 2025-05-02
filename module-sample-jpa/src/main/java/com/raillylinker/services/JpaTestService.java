package com.raillylinker.services;

import com.raillylinker.configurations.jpa_configs.Db1MainConfig;
import com.raillylinker.controllers.JpaTestController;
import com.raillylinker.jpa_beans.db1_main.entities.*;
import com.raillylinker.jpa_beans.db1_main.repositories.*;
import com.raillylinker.jpa_beans.db1_main.repositories_dsl.Db1_Template_FkTestManyToOneChild_RepositoryDsl;
import com.raillylinker.jpa_beans.db1_main.repositories_dsl.Db1_Template_FkTestParent_RepositoryDsl;
import com.raillylinker.util_components.CustomUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class JpaTestService {
    public JpaTestService(
            // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
            @Value("${spring.profiles.active:default}")
            @NotNull String activeProfile,

            @NotNull CustomUtil customUtil,

            @NotNull Db1_Template_TestData_Repository db1TemplateTestDataRepository,
            @NotNull Db1_Template_LogicalDeleteUniqueData_Repository db1TemplateLogicalDeleteUniqueDataRepository,
            @NotNull Db1_Template_FkTestParent_Repository db1TemplateFkTestParentRepository,
            @NotNull Db1_Template_FkTestManyToOneChild_Repository db1TemplateFkTestManyToOneChildRepository,
            @NotNull Db1_Template_JustBooleanTest_Repository db1TemplateJustBooleanTestRepository,
            @NotNull Db1_Template_FkTestManyToOneChild_RepositoryDsl db1TemplateFkTestManyToOneChildRepositoryDsl,
            @NotNull Db1_Template_FkTestParent_RepositoryDsl db1TemplateFkTestParentRepositoryDsl
    ) {
        this.activeProfile = activeProfile;
        this.customUtil = customUtil;
        this.db1TemplateTestDataRepository = db1TemplateTestDataRepository;
        this.db1TemplateLogicalDeleteUniqueDataRepository = db1TemplateLogicalDeleteUniqueDataRepository;
        this.db1TemplateFkTestParentRepository = db1TemplateFkTestParentRepository;
        this.db1TemplateFkTestManyToOneChildRepository = db1TemplateFkTestManyToOneChildRepository;
        this.db1TemplateJustBooleanTestRepository = db1TemplateJustBooleanTestRepository;
        this.db1TemplateFkTestManyToOneChildRepositoryDsl = db1TemplateFkTestManyToOneChildRepositoryDsl;
        this.db1TemplateFkTestParentRepositoryDsl = db1TemplateFkTestParentRepositoryDsl;
    }

    // <멤버 변수 공간>
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    private final @NotNull String activeProfile;

    // (스웨거 문서 공개 여부 설정)
    private final @NotNull CustomUtil customUtil;

    private final @NotNull Logger classLogger = LoggerFactory.getLogger(RootService.class);

    private final @NotNull Db1_Template_TestData_Repository db1TemplateTestDataRepository;
    private final @NotNull Db1_Template_LogicalDeleteUniqueData_Repository db1TemplateLogicalDeleteUniqueDataRepository;
    private final @NotNull Db1_Template_FkTestParent_Repository db1TemplateFkTestParentRepository;
    private final @NotNull Db1_Template_FkTestManyToOneChild_Repository db1TemplateFkTestManyToOneChildRepository;
    private final @NotNull Db1_Template_JustBooleanTest_Repository db1TemplateJustBooleanTestRepository;
    private final @NotNull Db1_Template_FkTestManyToOneChild_RepositoryDsl db1TemplateFkTestManyToOneChildRepositoryDsl;
    private final @NotNull Db1_Template_FkTestParent_RepositoryDsl db1TemplateFkTestParentRepositoryDsl;


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    // (DB Row 입력 테스트 API)
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME)
    public @Nullable JpaTestController.InsertDataSampleOutputVo insertDataSample(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull JpaTestController.InsertDataSampleInputVo inputVo
    ) {
        @NotNull Db1_Template_TestData result = db1TemplateTestDataRepository.save(
                Db1_Template_TestData.builder()
                        .content(inputVo.getContent())
                        .randomNum((int) (Math.random() * 99999999))
                        .testDatetime(
                                ZonedDateTime.parse(inputVo.getDateString(), DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
                                        .withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
                        )
                        .rowDeleteDateStr("/")
                        .build()
        );

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.InsertDataSampleOutputVo(
                result.getUid(),
                result.getContent(),
                result.getRandomNum(),
                result.getTestDatetime().atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                result.getRowCreateDate().atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                result.getRowUpdateDate().atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                result.getRowDeleteDateStr()
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
                entity.setRowDeleteDateStr(
                        LocalDateTime.now().atZone(ZoneId.systemDefault())
                                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
                );
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
            entity.setRowDeleteDateStr(
                    LocalDateTime.now().atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
            );
            db1TemplateTestDataRepository.save(entity);
        } else {
            db1TemplateTestDataRepository.deleteById(index);
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());
    }


    // ----
    // (DB Rows 조회 테스트)
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
    public @Nullable JpaTestController.SelectRowsSampleOutputVo selectRowsSample(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        @NotNull List<Db1_Template_TestData> resultEntityList =
                db1TemplateTestDataRepository.findAllByRowDeleteDateStrOrderByRowCreateDate("/");
        @NotNull List<JpaTestController.SelectRowsSampleOutputVo.TestEntityVo> entityVoList = new ArrayList<>();
        for (@NotNull Db1_Template_TestData resultEntity : resultEntityList) {
            entityVoList.add(new JpaTestController.SelectRowsSampleOutputVo.TestEntityVo(
                    resultEntity.getUid(),
                    resultEntity.getContent(),
                    resultEntity.getRandomNum(),
                    resultEntity.getTestDatetime().atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.getRowCreateDate().atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.getRowUpdateDate().atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.getRowDeleteDateStr()
            ));
        }

        @NotNull List<Db1_Template_TestData> logicalDeleteEntityVoList =
                db1TemplateTestDataRepository.findAllByRowDeleteDateStrNotOrderByRowCreateDate("/");
        @NotNull List<JpaTestController.SelectRowsSampleOutputVo.TestEntityVo> logicalDeleteVoList = new ArrayList<>();
        for (@NotNull Db1_Template_TestData resultEntity : logicalDeleteEntityVoList) {
            logicalDeleteVoList.add(new JpaTestController.SelectRowsSampleOutputVo.TestEntityVo(
                    resultEntity.getUid(),
                    resultEntity.getContent(),
                    resultEntity.getRandomNum(),
                    resultEntity.getTestDatetime().atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.getRowCreateDate().atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.getRowUpdateDate().atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    resultEntity.getRowDeleteDateStr()
            ));
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.SelectRowsSampleOutputVo(
                entityVoList,
                logicalDeleteVoList
        );
    }


    // ----
    // (DB 테이블의 random_num 컬럼 근사치 기준으로 정렬한 리스트 조회 API)
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
    public @Nullable JpaTestController.SelectRowsOrderByRandomNumSampleOutputVo selectRowsOrderByRandomNumSample(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull Integer num
    ) {
        @NotNull List<Db1_Template_TestData_Repository.FindAllFromTemplateTestDataByNotDeletedWithRandomNumDistanceOutputVo> foundEntityList =
                db1TemplateTestDataRepository.findAllFromTemplateTestDataByNotDeletedWithRandomNumDistance(num);

        @NotNull List<JpaTestController.SelectRowsOrderByRandomNumSampleOutputVo.TestEntityVo> testEntityVoList =
                new ArrayList<>();

        for (@NotNull Db1_Template_TestData_Repository.FindAllFromTemplateTestDataByNotDeletedWithRandomNumDistanceOutputVo entity : foundEntityList) {
            testEntityVoList.add(
                    new JpaTestController.SelectRowsOrderByRandomNumSampleOutputVo.TestEntityVo(
                            entity.getUid(),
                            entity.getContent(),
                            entity.getRandomNum(),
                            entity.getTestDatetime().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            entity.getRowCreateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            entity.getRowUpdateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            entity.getDistance()
                    )
            );
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.SelectRowsOrderByRandomNumSampleOutputVo(testEntityVoList);
    }


    // ----
    // (DB 테이블의 row_create_date 컬럼 근사치 기준으로 정렬한 리스트 조회 API)
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
    public @Nullable JpaTestController.SelectRowsOrderByRowCreateDateSampleOutputVo selectRowsOrderByRowCreateDateSample(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull String dateString
    ) {
        @NotNull List<Db1_Template_TestData_Repository.FindAllFromTemplateTestDataByNotDeletedWithRowCreateDateDistanceOutputVo> foundEntityList =
                db1TemplateTestDataRepository.findAllFromTemplateTestDataByNotDeletedWithRowCreateDateDistance(
                        ZonedDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
                                .withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
                );

        @NotNull List<JpaTestController.SelectRowsOrderByRowCreateDateSampleOutputVo.TestEntityVo> testEntityVoList =
                new ArrayList<>();

        for (@NotNull Db1_Template_TestData_Repository.FindAllFromTemplateTestDataByNotDeletedWithRowCreateDateDistanceOutputVo entity : foundEntityList) {
            testEntityVoList.add(
                    new JpaTestController.SelectRowsOrderByRowCreateDateSampleOutputVo.TestEntityVo(
                            entity.getUid(),
                            entity.getContent(),
                            entity.getRandomNum(),
                            entity.getTestDatetime().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            entity.getRowCreateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            entity.getRowUpdateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            entity.getTimeDiffMicroSec()
                    )
            );
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.SelectRowsOrderByRowCreateDateSampleOutputVo(testEntityVoList);
    }


    // ----
    // (DB Rows 조회 테스트 (페이징))
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
    public @Nullable JpaTestController.SelectRowsPageSampleOutputVo selectRowsPageSample(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull Integer page,
            @NotNull Integer pageElementsCount
    ) {
        @NotNull Pageable pageable = PageRequest.of(page - 1, pageElementsCount);
        @NotNull Page<Db1_Template_TestData> entityList =
                db1TemplateTestDataRepository.findAllByRowDeleteDateStrOrderByRowCreateDate(
                        "/",
                        pageable
                );

        @NotNull List<JpaTestController.SelectRowsPageSampleOutputVo.TestEntityVo> testEntityVoList = new ArrayList<>();
        for (@NotNull Db1_Template_TestData entity : entityList) {
            testEntityVoList.add(
                    new JpaTestController.SelectRowsPageSampleOutputVo.TestEntityVo(
                            entity.getUid(),
                            entity.getContent(),
                            entity.getRandomNum(),
                            entity.getTestDatetime().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            entity.getRowCreateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            entity.getRowUpdateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
                    )
            );
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.SelectRowsPageSampleOutputVo(
                entityList.getTotalElements(),
                testEntityVoList
        );
    }


    // ----
    // (DB Rows 조회 테스트 (네이티브 쿼리 페이징))
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
    public @Nullable JpaTestController.SelectRowsNativeQueryPageSampleOutputVo selectRowsNativeQueryPageSample(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull Integer page,
            @NotNull Integer pageElementsCount,
            @NotNull Integer num
    ) {
        @NotNull Pageable pageable = PageRequest.of(page - 1, pageElementsCount);
        @NotNull Page<Db1_Template_TestData_Repository.FindPageAllFromTemplateTestDataByNotDeletedWithRandomNumDistanceOutputVo> voList =
                db1TemplateTestDataRepository.findPageAllFromTemplateTestDataByNotDeletedWithRandomNumDistance(
                        num,
                        pageable
                );

        @NotNull List<JpaTestController.SelectRowsNativeQueryPageSampleOutputVo.TestEntityVo> testEntityVoList = new ArrayList<>();
        for (@NotNull Db1_Template_TestData_Repository.FindPageAllFromTemplateTestDataByNotDeletedWithRandomNumDistanceOutputVo vo : voList) {
            testEntityVoList.add(new JpaTestController.SelectRowsNativeQueryPageSampleOutputVo.TestEntityVo(
                    vo.getUid(),
                    vo.getContent(),
                    vo.getRandomNum(),
                    vo.getTestDatetime().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    vo.getRowCreateDate().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    vo.getRowUpdateDate().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    vo.getDistance()
            ));
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.SelectRowsNativeQueryPageSampleOutputVo(
                voList.getTotalElements(),
                testEntityVoList
        );
    }


    // ----
    // (DB Row 수정 테스트)
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME)
    public @Nullable JpaTestController.UpdateRowSampleOutputVo updateRowSample(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull Long testTableUid,
            @NotNull JpaTestController.UpdateRowSampleInputVo inputVo
    ) {
        @Nullable Db1_Template_TestData oldEntity = db1TemplateTestDataRepository.findByUidAndRowDeleteDateStr(testTableUid, "/");

        if (oldEntity == null) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return null;
        }

        oldEntity.setContent(inputVo.getContent());
        oldEntity.setTestDatetime(
                ZonedDateTime.parse(inputVo.getDateString(), DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
                        .withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
        );

        @NotNull Db1_Template_TestData result = db1TemplateTestDataRepository.save(oldEntity);

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.UpdateRowSampleOutputVo(
                result.getUid(),
                result.getContent(),
                result.getRandomNum(),
                result.getTestDatetime().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                result.getRowCreateDate().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                result.getRowUpdateDate().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
        );
    }


    // ----
    // (DB Row 수정 테스트 (네이티브 쿼리))
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME)
    public void updateRowNativeQuerySample(
            HttpServletResponse httpServletResponse,
            Long testTableUid,
            JpaTestController.UpdateRowNativeQuerySampleInputVo inputVo
    ) {
        @Nullable Db1_Template_TestData oldEntity = db1TemplateTestDataRepository.findByUidAndRowDeleteDateStr(testTableUid, "/");

        if (oldEntity == null) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return;
        }

        db1TemplateTestDataRepository.updateToTemplateTestDataSetContentAndTestDateTimeByUid(
                testTableUid,
                inputVo.getContent(),
                ZonedDateTime.parse(inputVo.getDateString(), DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
                        .withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
        );

        httpServletResponse.setStatus(HttpStatus.OK.value());
    }


    // ----
    // (DB 정보 검색 테스트)
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
    public @Nullable JpaTestController.SelectRowWhereSearchingKeywordSampleOutputVo selectRowWhereSearchingKeywordSample(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull Integer page,
            @NotNull Integer pageElementsCount,
            @NotNull String searchKeyword
    ) {
        @NotNull Pageable pageable = PageRequest.of(page - 1, pageElementsCount);
        @NotNull Page<Db1_Template_TestData_Repository.FindPageAllFromTemplateTestDataBySearchKeywordOutputVo> voList =
                db1TemplateTestDataRepository.findPageAllFromTemplateTestDataBySearchKeyword(
                        searchKeyword,
                        pageable
                );

        @NotNull List<JpaTestController.SelectRowWhereSearchingKeywordSampleOutputVo.TestEntityVo> testEntityVoList = new ArrayList<>();
        for (@NotNull Db1_Template_TestData_Repository.FindPageAllFromTemplateTestDataBySearchKeywordOutputVo vo : voList) {
            testEntityVoList.add(
                    new JpaTestController.SelectRowWhereSearchingKeywordSampleOutputVo.TestEntityVo(
                            vo.getUid(),
                            vo.getContent(),
                            vo.getRandomNum(),
                            vo.getTestDatetime().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            vo.getRowCreateDate().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            vo.getRowUpdateDate().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
                    )
            );
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.SelectRowWhereSearchingKeywordSampleOutputVo(
                voList.getTotalElements(),
                testEntityVoList
        );
    }


    // ----
    // (트랜젝션 동작 테스트)
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME)
    public void transactionTest(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        db1TemplateTestDataRepository.save(
                Db1_Template_TestData.builder()
                        .content("error test")
                        .randomNum((int) (Math.random() * 99999999))
                        .testDatetime(LocalDateTime.now())
                        .rowDeleteDateStr("/")
                        .build()
        );

        throw new RuntimeException("Transaction Rollback Test!");
    }


    // ----
    // (트랜젝션 비동작 테스트)
    public void nonTransactionTest(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        db1TemplateTestDataRepository.save(
                Db1_Template_TestData.builder()
                        .content("error test")
                        .randomNum((int) (Math.random() * 99999999))
                        .testDatetime(LocalDateTime.now())
                        .rowDeleteDateStr("/")
                        .build()
        );

        throw new RuntimeException("No Transaction Exception Test!");
    }


    // ----
    // (트랜젝션 비동작 테스트(try-catch))
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME)
    public void tryCatchNonTransactionTest(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        try {
            db1TemplateTestDataRepository.save(
                    Db1_Template_TestData.builder()
                            .content("error test")
                            .randomNum((int) (Math.random() * 99999999))
                            .testDatetime(LocalDateTime.now())
                            .rowDeleteDateStr("/")
                            .build()
            );

            throw new Exception("Transaction Rollback Test!");
        } catch (Exception e) {
            classLogger.error("error ", e);
        }
    }


    // ----
    // (DB Rows 조회 테스트 (중복 없는 네이티브 쿼리 페이징))
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
    public @Nullable JpaTestController.SelectRowsNoDuplicatePagingSampleOutputVo selectRowsNoDuplicatePagingSample(
            @NotNull HttpServletResponse httpServletResponse,
            @Nullable Long lastItemUid,
            @NotNull Integer pageElementsCount
    ) {
        // 중복 없는 페이징 쿼리를 사용합니다.
        @NotNull List<Db1_Template_TestData_Repository.FindAllFromTemplateTestDataForNoDuplicatedPagingOutputVo> voList =
                db1TemplateTestDataRepository.findAllFromTemplateTestDataForNoDuplicatedPaging(
                        lastItemUid,
                        pageElementsCount
                );

        // 전체 개수 카운팅은 따로 해주어야 합니다.
        @NotNull Long count = db1TemplateTestDataRepository.countFromTemplateTestDataByNotDeleted();

        @NotNull List<JpaTestController.SelectRowsNoDuplicatePagingSampleOutputVo.TestEntityVo> testEntityVoList = new ArrayList<>();
        for (@NotNull Db1_Template_TestData_Repository.FindAllFromTemplateTestDataForNoDuplicatedPagingOutputVo vo : voList) {
            testEntityVoList.add(new JpaTestController.SelectRowsNoDuplicatePagingSampleOutputVo.TestEntityVo(
                    vo.getUid(),
                    vo.getContent(),
                    vo.getRandomNum(),
                    vo.getTestDatetime().atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    vo.getRowCreateDate().atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                    vo.getRowUpdateDate().atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
            ));
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.SelectRowsNoDuplicatePagingSampleOutputVo(
                count,
                testEntityVoList
        );
    }


    // ----
    // (DB Rows 조회 테스트 (카운팅))
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
    public @Nullable JpaTestController.SelectRowsCountSampleOutputVo selectRowsCountSample(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        @NotNull Long count = db1TemplateTestDataRepository.countByRowDeleteDateStr("/");

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.SelectRowsCountSampleOutputVo(count);
    }


    // ----
    // (DB Rows 조회 테스트 (네이티브 카운팅))
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
    public @Nullable JpaTestController.SelectRowsCountByNativeQuerySampleOutputVo selectRowsCountByNativeQuerySample(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        @NotNull Long count = db1TemplateTestDataRepository.countFromTemplateTestDataByNotDeleted();

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.SelectRowsCountByNativeQuerySampleOutputVo(count);
    }


    // ----
    // (DB Row 조회 테스트 (네이티브))
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
    public @Nullable JpaTestController.SelectRowByNativeQuerySampleOutputVo selectRowByNativeQuerySample(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull Long testTableUid
    ) {
        @Nullable Db1_Template_TestData_Repository.FindFromTemplateTestDataByNotDeletedAndUidOutputVo entity =
                db1TemplateTestDataRepository.findFromTemplateTestDataByNotDeletedAndUid(testTableUid);

        if (entity == null) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return null;
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.SelectRowByNativeQuerySampleOutputVo(
                entity.getUid(),
                entity.getContent(),
                entity.getRandomNum(),
                entity.getTestDatetime().atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                entity.getRowCreateDate().atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                entity.getRowUpdateDate().atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
        );
    }


    // ----
    // (유니크 테스트 테이블 Row 입력 API)
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME)
    public @Nullable JpaTestController.InsertUniqueTestTableRowSampleOutputVo insertUniqueTestTableRowSample(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull JpaTestController.InsertUniqueTestTableRowSampleInputVo inputVo
    ) {
        @NotNull Db1_Template_LogicalDeleteUniqueData result = db1TemplateLogicalDeleteUniqueDataRepository.save(
                Db1_Template_LogicalDeleteUniqueData.builder()
                        .uniqueValue(inputVo.getUniqueValue())
                        .rowDeleteDateStr("/")
                        .build()
        );

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.InsertUniqueTestTableRowSampleOutputVo(
                result.getUid(),
                result.getUniqueValue(),
                result.getRowCreateDate().atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                result.getRowUpdateDate().atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                result.getRowDeleteDateStr()
        );
    }


    // ----
    // (유니크 테스트 테이블 Rows 조회 테스트)
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
    public @Nullable JpaTestController.SelectUniqueTestTableRowsSampleOutputVo selectUniqueTestTableRowsSample(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        @NotNull List<Db1_Template_LogicalDeleteUniqueData> resultEntityList =
                db1TemplateLogicalDeleteUniqueDataRepository.findAllByRowDeleteDateStrOrderByRowCreateDate("/");
        @NotNull List<JpaTestController.SelectUniqueTestTableRowsSampleOutputVo.TestEntityVo> entityVoList = new ArrayList<>();
        for (@NotNull Db1_Template_LogicalDeleteUniqueData resultEntity : resultEntityList) {
            entityVoList.add(
                    new JpaTestController.SelectUniqueTestTableRowsSampleOutputVo.TestEntityVo(
                            resultEntity.getUid(),
                            resultEntity.getUniqueValue(),
                            resultEntity.getRowCreateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            resultEntity.getRowUpdateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            resultEntity.getRowDeleteDateStr()
                    )
            );
        }

        @NotNull List<Db1_Template_LogicalDeleteUniqueData> logicalDeleteEntityVoList =
                db1TemplateLogicalDeleteUniqueDataRepository.findAllByRowDeleteDateStrNotOrderByRowCreateDate("/");
        @NotNull List<JpaTestController.SelectUniqueTestTableRowsSampleOutputVo.TestEntityVo> logicalDeleteVoList = new ArrayList<>();
        for (@NotNull Db1_Template_LogicalDeleteUniqueData resultEntity : logicalDeleteEntityVoList) {
            logicalDeleteVoList.add(
                    new JpaTestController.SelectUniqueTestTableRowsSampleOutputVo.TestEntityVo(
                            resultEntity.getUid(),
                            resultEntity.getUniqueValue(),
                            resultEntity.getRowUpdateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            resultEntity.getRowUpdateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            resultEntity.getRowDeleteDateStr()
                    )
            );
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.SelectUniqueTestTableRowsSampleOutputVo(
                entityVoList,
                logicalDeleteVoList
        );
    }


    // ----
    // (유니크 테스트 테이블 Row 수정 테스트)
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME)
    public @Nullable JpaTestController.UpdateUniqueTestTableRowSampleOutputVo updateUniqueTestTableRowSample(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull Long uniqueTestTableUid,
            @NotNull JpaTestController.UpdateUniqueTestTableRowSampleInputVo inputVo
    ) {
        @Nullable Db1_Template_LogicalDeleteUniqueData oldEntity =
                db1TemplateLogicalDeleteUniqueDataRepository.findByUidAndRowDeleteDateStr(uniqueTestTableUid, "/");

        if (oldEntity == null) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return null;
        }

        @Nullable Db1_Template_LogicalDeleteUniqueData uniqueValueEntity =
                db1TemplateLogicalDeleteUniqueDataRepository.findByUniqueValueAndRowDeleteDateStr(
                        inputVo.getUniqueValue(),
                        "/"
                );

        if (uniqueValueEntity != null) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "2");
            return null;
        }

        oldEntity.setUniqueValue(inputVo.getUniqueValue());

        @NotNull Db1_Template_LogicalDeleteUniqueData result =
                db1TemplateLogicalDeleteUniqueDataRepository.save(oldEntity);

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.UpdateUniqueTestTableRowSampleOutputVo(
                result.getUid(),
                result.getUniqueValue(),
                result.getRowCreateDate().atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                result.getRowUpdateDate().atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
        );
    }


    // ----
    // (유니크 테스트 테이블 Row 삭제 테스트)
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME)
    public void deleteUniqueTestTableRowSample(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull Long index
    ) {
        @Nullable Db1_Template_LogicalDeleteUniqueData entity =
                db1TemplateLogicalDeleteUniqueDataRepository.findByUidAndRowDeleteDateStr(index, "/");

        if (entity == null) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return;
        }

        entity.setRowDeleteDateStr(
                LocalDateTime.now().atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
        );
        db1TemplateLogicalDeleteUniqueDataRepository.save(entity);

        httpServletResponse.setStatus(HttpStatus.OK.value());
    }


    // ----
    // (외래키 부모 테이블 Row 입력 API)
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME)
    public @Nullable JpaTestController.InsertFkParentRowSampleOutputVo insertFkParentRowSample(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull JpaTestController.InsertFkParentRowSampleInputVo inputVo
    ) {
        @NotNull Db1_Template_FkTestParent result =
                db1TemplateFkTestParentRepository.save(
                        Db1_Template_FkTestParent.builder()
                                .parentName(inputVo.getFkParentName())
                                .rowDeleteDateStr("/")
                                .build()
                );

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.InsertFkParentRowSampleOutputVo(
                result.getUid(),
                result.getParentName(),
                result.getRowCreateDate().atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                result.getRowUpdateDate().atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
        );
    }


    // ----
    // (외래키 부모 테이블 아래에 자식 테이블의 Row 입력 API)
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME)
    public @Nullable JpaTestController.InsertFkChildRowSampleOutputVo insertFkChildRowSample(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull Long parentUid,
            @NotNull JpaTestController.InsertFkChildRowSampleInputVo inputVo
    ) {
        @NotNull Optional<Db1_Template_FkTestParent> parentEntityOpt = db1TemplateFkTestParentRepository.findById(parentUid);

        if (parentEntityOpt.isEmpty()) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return null;
        }

        @NotNull Db1_Template_FkTestParent parentEntity = parentEntityOpt.get();

        @NotNull Db1_Template_FkTestManyToOneChild result =
                db1TemplateFkTestManyToOneChildRepository.save(
                        Db1_Template_FkTestManyToOneChild.builder()
                                .childName(inputVo.getFkChildName())
                                .fkTestParent(parentEntity)
                                .rowDeleteDateStr("/")
                                .build()
                );

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.InsertFkChildRowSampleOutputVo(
                result.getUid(),
                result.getChildName(),
                result.getFkTestParent().getParentName(),
                result.getRowCreateDate().atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                result.getRowUpdateDate().atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
        );
    }


    // ----
    // (외래키 관련 테이블 Rows 조회 테스트)
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
    public @Nullable JpaTestController.SelectFkTestTableRowsSampleOutputVo selectFkTestTableRowsSample(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        @NotNull List<Db1_Template_FkTestParent> resultEntityList =
                db1TemplateFkTestParentRepository.findAllByRowDeleteDateStrOrderByRowCreateDate("/");

        @NotNull List<JpaTestController.SelectFkTestTableRowsSampleOutputVo.ParentEntityVo> entityVoList =
                new ArrayList<>();
        for (@NotNull Db1_Template_FkTestParent resultEntity : resultEntityList) {
            @NotNull List<JpaTestController.SelectFkTestTableRowsSampleOutputVo.ParentEntityVo.ChildEntityVo> childEntityVoList =
                    new ArrayList<>();

            @NotNull List<Db1_Template_FkTestManyToOneChild> childList =
                    db1TemplateFkTestManyToOneChildRepository.findAllByFkTestParentAndRowDeleteDateStrOrderByRowCreateDate(
                            resultEntity,
                            "/"
                    );

            for (@NotNull Db1_Template_FkTestManyToOneChild childEntity : childList) {
                childEntityVoList.add(
                        new JpaTestController.SelectFkTestTableRowsSampleOutputVo.ParentEntityVo.ChildEntityVo(
                                childEntity.getUid(),
                                childEntity.getChildName(),
                                childEntity.getRowCreateDate().atZone(ZoneId.systemDefault())
                                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                                childEntity.getRowUpdateDate().atZone(ZoneId.systemDefault())
                                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
                        )
                );
            }

            entityVoList.add(
                    new JpaTestController.SelectFkTestTableRowsSampleOutputVo.ParentEntityVo(
                            resultEntity.getUid(),
                            resultEntity.getParentName(),
                            resultEntity.getRowCreateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            resultEntity.getRowUpdateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            childEntityVoList
                    )
            );
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.SelectFkTestTableRowsSampleOutputVo(
                entityVoList
        );
    }


    // ----
    // (외래키 관련 테이블 Rows 조회 테스트(Native Join))
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
    public @Nullable JpaTestController.SelectFkTestTableRowsByNativeQuerySampleDot1OutputVo selectFkTestTableRowsByNativeQuerySample(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        @NotNull List<Db1_Template_FkTestManyToOneChild_Repository.FindAllFromTemplateFkTestManyToOneChildInnerJoinParentByNotDeletedOutputVo> resultEntityList =
                db1TemplateFkTestManyToOneChildRepository.findAllFromTemplateFkTestManyToOneChildInnerJoinParentByNotDeleted();

        @NotNull List<JpaTestController.SelectFkTestTableRowsByNativeQuerySampleDot1OutputVo.ChildEntityVo> entityVoList =
                new ArrayList<>();
        for (@NotNull Db1_Template_FkTestManyToOneChild_Repository.FindAllFromTemplateFkTestManyToOneChildInnerJoinParentByNotDeletedOutputVo resultEntity : resultEntityList) {
            entityVoList.add(
                    new JpaTestController.SelectFkTestTableRowsByNativeQuerySampleDot1OutputVo.ChildEntityVo(
                            resultEntity.getChildUid(),
                            resultEntity.getChildName(),
                            resultEntity.getChildCreateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            resultEntity.getChildUpdateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            resultEntity.getParentUid(),
                            resultEntity.getParentName()
                    )
            );
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.SelectFkTestTableRowsByNativeQuerySampleDot1OutputVo(
                entityVoList
        );
    }


    // ----
    // (Native Query 반환값 테스트)
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME)
    public @Nullable JpaTestController.GetNativeQueryReturnValueTestOutputVo getNativeQueryReturnValueTest(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull Boolean inputVal
    ) {
        // boolean 값을 갖고오기 위한 테스트 테이블이 존재하지 않는다면 하나 생성하기
        @NotNull List<Db1_Template_JustBooleanTest> justBooleanEntity = db1TemplateJustBooleanTestRepository.findAll();
        if (justBooleanEntity.isEmpty()) {
            db1TemplateJustBooleanTestRepository.save(
                    Db1_Template_JustBooleanTest.builder()
                            .boolValue(true)
                            .build()
            );
        }

        var resultEntity = db1TemplateJustBooleanTestRepository.multiCaseBooleanReturnTest(inputVal);

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.GetNativeQueryReturnValueTestOutputVo(
                resultEntity.getNormalBoolValue() == 1L,
                resultEntity.getFuncBoolValue() == 1L,
                resultEntity.getIfBoolValue() == 1L,
                resultEntity.getCaseBoolValue() == 1L,
                resultEntity.getTableColumnBoolValue()
        );
    }


    // ----
    // (SQL Injection 테스트)
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
    public @Nullable JpaTestController.SqlInjectionTestOutputVo sqlInjectionTest(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull String searchKeyword
    ) {
        // jpaRepository : Injection Safe
        @NotNull List<Db1_Template_TestData> jpaRepositoryResultEntityList =
                db1TemplateTestDataRepository.findAllByContentOrderByRowCreateDate(
                        searchKeyword
                );

        @NotNull ArrayList<JpaTestController.SqlInjectionTestOutputVo.TestEntityVo> jpaRepositoryResultList =
                new ArrayList<>();
        for (@NotNull Db1_Template_TestData jpaRepositoryResultEntity : jpaRepositoryResultEntityList) {
            jpaRepositoryResultList.add(
                    new JpaTestController.SqlInjectionTestOutputVo.TestEntityVo(
                            jpaRepositoryResultEntity.getUid(),
                            jpaRepositoryResultEntity.getContent(),
                            jpaRepositoryResultEntity.getRandomNum(),
                            jpaRepositoryResultEntity.getTestDatetime().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            jpaRepositoryResultEntity.getRowCreateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            jpaRepositoryResultEntity.getRowUpdateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
                    )
            );
        }

        // JPQL : Injection Safe
        @NotNull List<Db1_Template_TestData> jpqlResultEntityList =
                db1TemplateTestDataRepository.findAllByContentOrderByRowCreateDateJpql(
                        searchKeyword
                );

        @NotNull ArrayList<JpaTestController.SqlInjectionTestOutputVo.TestEntityVo> jpqlResultList =
                new ArrayList<>();
        for (@NotNull Db1_Template_TestData jpqlEntity : jpqlResultEntityList) {
            jpqlResultList.add(
                    new JpaTestController.SqlInjectionTestOutputVo.TestEntityVo(
                            jpqlEntity.getUid(),
                            jpqlEntity.getContent(),
                            jpqlEntity.getRandomNum(),
                            jpqlEntity.getTestDatetime().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            jpqlEntity.getRowCreateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            jpqlEntity.getRowUpdateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
                    )
            );
        }

        // NativeQuery : Injection Safe
        @NotNull List<Db1_Template_TestData_Repository.FindAllFromTemplateTestDataByContentOutputVo> nativeQueryResultEntityList =
                db1TemplateTestDataRepository.findAllFromTemplateTestDataByContent(
                        searchKeyword
                );

        @NotNull ArrayList<JpaTestController.SqlInjectionTestOutputVo.TestEntityVo> nativeQueryResultList =
                new ArrayList<>();
        for (@NotNull Db1_Template_TestData_Repository.FindAllFromTemplateTestDataByContentOutputVo nativeQueryEntity : nativeQueryResultEntityList) {
            nativeQueryResultList.add(
                    new JpaTestController.SqlInjectionTestOutputVo.TestEntityVo(
                            nativeQueryEntity.getUid(),
                            nativeQueryEntity.getContent(),
                            nativeQueryEntity.getRandomNum(),
                            nativeQueryEntity.getTestDatetime().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            nativeQueryEntity.getRowCreateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            nativeQueryEntity.getRowUpdateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
                    )
            );
        }

        /*
            결론 : 위 세 방식은 모두 SQL Injection 공격에서 안전합니다.
                셋 모두 쿼리문에 직접 값을 입력하는 것이 아니며, 매개변수로 먼저 받아서 JPA 를 경유하여 입력되므로,
                라이브러리가 자동으로 인젝션 공격을 막아주게 됩니다.
         */

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.SqlInjectionTestOutputVo(
                jpaRepositoryResultList,
                jpqlResultList,
                nativeQueryResultList
        );
    }


    // ----
    // (외래키 관련 테이블 Rows 조회 (네이티브 쿼리, 부모 테이블을 자식 테이블의 가장 최근 데이터만 Join))
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
    public @Nullable JpaTestController.SelectFkTableRowsWithLatestChildSampleOutputVo selectFkTableRowsWithLatestChildSample(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        @NotNull List<Db1_Template_FkTestParent_Repository.FindAllFromTemplateFkTestParentWithNearestChildOnlyOutputVo> resultEntityList =
                db1TemplateFkTestParentRepository.findAllFromTemplateFkTestParentWithNearestChildOnly();

        @NotNull ArrayList<JpaTestController.SelectFkTableRowsWithLatestChildSampleOutputVo.ParentEntityVo> entityVoList =
                new ArrayList<>();
        for (@NotNull Db1_Template_FkTestParent_Repository.FindAllFromTemplateFkTestParentWithNearestChildOnlyOutputVo resultEntity : resultEntityList) {
            entityVoList.add(
                    new JpaTestController.SelectFkTableRowsWithLatestChildSampleOutputVo.ParentEntityVo(
                            resultEntity.getParentUid(),
                            resultEntity.getParentName(),
                            resultEntity.getParentCreateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            resultEntity.getParentUpdateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            resultEntity.getChildUid() == null ? null : new JpaTestController.SelectFkTableRowsWithLatestChildSampleOutputVo.ParentEntityVo.ChildEntityVo(
                                    resultEntity.getChildUid(),
                                    Objects.requireNonNull(resultEntity.getChildName()),
                                    Objects.requireNonNull(resultEntity.getChildCreateDate()).atZone(ZoneId.systemDefault())
                                            .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                                    Objects.requireNonNull(resultEntity.getChildUpdateDate()).atZone(ZoneId.systemDefault())
                                            .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
                            )
                    )
            );
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.SelectFkTableRowsWithLatestChildSampleOutputVo(
                entityVoList
        );
    }


    // ----
    // (외래키 관련 테이블 Rows 조회 (QueryDsl))
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
    public @Nullable JpaTestController.SelectFkTableRowsWithQueryDslOutputVo selectFkTableRowsWithQueryDsl(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        @NotNull List<Db1_Template_FkTestParent> resultEntityList =
                db1TemplateFkTestParentRepositoryDsl.findParentWithChildren();

        @NotNull ArrayList<JpaTestController.SelectFkTableRowsWithQueryDslOutputVo.ParentEntityVo> entityVoList =
                new ArrayList<>();

        for (@NotNull Db1_Template_FkTestParent resultEntity : resultEntityList) {
            @NotNull ArrayList<JpaTestController.SelectFkTableRowsWithQueryDslOutputVo.ParentEntityVo.ChildEntityVo> childEntityVoList =
                    new ArrayList<>();

            for (@NotNull Db1_Template_FkTestManyToOneChild childEntity : resultEntity.getFkTestManyToOneChildList()) {
                childEntityVoList.add(
                        new JpaTestController.SelectFkTableRowsWithQueryDslOutputVo.ParentEntityVo.ChildEntityVo(
                                childEntity.getUid(),
                                childEntity.getChildName(),
                                childEntity.getRowCreateDate().atZone(ZoneId.systemDefault())
                                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                                childEntity.getRowUpdateDate().atZone(ZoneId.systemDefault())
                                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
                        )
                );
            }


            entityVoList.add(
                    new JpaTestController.SelectFkTableRowsWithQueryDslOutputVo.ParentEntityVo(
                            resultEntity.getUid(),
                            resultEntity.getParentName(),
                            resultEntity.getRowCreateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            resultEntity.getRowUpdateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            childEntityVoList
                    )
            );
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.SelectFkTableRowsWithQueryDslOutputVo(
                entityVoList
        );
    }


    // ----
    // (외래키 관련 테이블 Rows 조회 및 부모 테이블 이름으로 필터링 (QueryDsl))
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
    public @Nullable JpaTestController.SelectFkTableRowsByParentNameFilterWithQueryDslOutputVo selectFkTableRowsByParentNameFilterWithQueryDsl(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull String parentName
    ) {
        @NotNull List<Db1_Template_FkTestParent> resultEntityList =
                db1TemplateFkTestParentRepositoryDsl.findParentWithChildrenByName(parentName);

        @NotNull ArrayList<JpaTestController.SelectFkTableRowsByParentNameFilterWithQueryDslOutputVo.ParentEntityVo> entityVoList =
                new ArrayList<>();

        for (var resultEntity : resultEntityList) {
            @NotNull ArrayList<JpaTestController.SelectFkTableRowsByParentNameFilterWithQueryDslOutputVo.ParentEntityVo.ChildEntityVo> childEntityVoList =
                    new ArrayList<>();

            for (@NotNull Db1_Template_FkTestManyToOneChild childEntity : resultEntity.getFkTestManyToOneChildList()) {
                childEntityVoList.add(
                        new JpaTestController.SelectFkTableRowsByParentNameFilterWithQueryDslOutputVo.ParentEntityVo.ChildEntityVo(
                                childEntity.getUid(),
                                childEntity.getChildName(),
                                childEntity.getRowCreateDate().atZone(ZoneId.systemDefault())
                                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                                childEntity.getRowUpdateDate().atZone(ZoneId.systemDefault())
                                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
                        )
                );
            }


            entityVoList.add(
                    new JpaTestController.SelectFkTableRowsByParentNameFilterWithQueryDslOutputVo.ParentEntityVo(
                            resultEntity.getUid(),
                            resultEntity.getParentName(),
                            resultEntity.getRowCreateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            resultEntity.getRowUpdateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            childEntityVoList
                    )
            );
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.SelectFkTableRowsByParentNameFilterWithQueryDslOutputVo(
                entityVoList
        );
    }


    // ----
    // (외래키 관련 테이블 부모 테이블 고유번호로 자식 테이블 리스트 검색 (QueryDsl))
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
    public @Nullable JpaTestController.SelectFkTableChildListWithQueryDslOutputVo selectFkTableChildListWithQueryDsl(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull Long parentUid
    ) {
        @NotNull List<Db1_Template_FkTestManyToOneChild> resultEntityList =
                db1TemplateFkTestManyToOneChildRepositoryDsl.findChildByParentId(parentUid);

        @NotNull ArrayList<JpaTestController.SelectFkTableChildListWithQueryDslOutputVo.ChildEntityVo> entityVoList =
                new ArrayList<>();

        for (@NotNull Db1_Template_FkTestManyToOneChild resultEntity : resultEntityList) {
            entityVoList.add(
                    new JpaTestController.SelectFkTableChildListWithQueryDslOutputVo.ChildEntityVo(
                            resultEntity.getUid(),
                            resultEntity.getChildName(),
                            resultEntity.getRowCreateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            resultEntity.getRowUpdateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
                    )
            );
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.SelectFkTableChildListWithQueryDslOutputVo(
                entityVoList
        );
    }


    // ----
    // (외래키 자식 테이블 Row 삭제 테스트)
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME)
    public void deleteFkChildRowSample(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull Long index
    ) {
        @NotNull Optional<Db1_Template_FkTestManyToOneChild> entityOpt =
                db1TemplateFkTestManyToOneChildRepository.findById(index);

        if (entityOpt.isEmpty()) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return;
        }

        db1TemplateFkTestManyToOneChildRepository.deleteById(index);

        httpServletResponse.setStatus(HttpStatus.OK.value());
    }


    // ----
    // (외래키 부모 테이블 Row 삭제 테스트 (Cascade 기능 확인))
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME)
    public void deleteFkParentRowSample(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull Long index
    ) {
        @NotNull Optional<Db1_Template_FkTestParent> entityOpt = db1TemplateFkTestParentRepository.findById(index);

        if (entityOpt.isEmpty()) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return;
        }

        db1TemplateFkTestParentRepository.deleteById(index);

        httpServletResponse.setStatus(HttpStatus.OK.value());
    }


    // ----
    // (외래키 테이블 트랜젝션 동작 테스트)
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME)
    public void fkTableTransactionTest(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        @NotNull Db1_Template_FkTestParent parentEntity = db1TemplateFkTestParentRepository.save(
                Db1_Template_FkTestParent.builder()
                        .parentName("transaction test")
                        .rowDeleteDateStr("/")
                        .build()
        );

        db1TemplateFkTestManyToOneChildRepository.save(
                Db1_Template_FkTestManyToOneChild.builder()
                        .childName("transaction test1")
                        .fkTestParent(parentEntity)
                        .rowDeleteDateStr("/")
                        .build()
        );

        db1TemplateFkTestManyToOneChildRepository.save(
                Db1_Template_FkTestManyToOneChild.builder()
                        .childName("transaction test2")
                        .fkTestParent(parentEntity)
                        .rowDeleteDateStr("/")
                        .build()
        );

        throw new RuntimeException("Transaction Rollback Test!");
    }


    // ----
    // (외래키 테이블 트랜젝션 비동작 테스트)
    public void fkTableNonTransactionTest(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        @NotNull Db1_Template_FkTestParent parentEntity = db1TemplateFkTestParentRepository.save(
                Db1_Template_FkTestParent.builder()
                        .parentName("transaction test")
                        .rowDeleteDateStr("/")
                        .build()
        );

        db1TemplateFkTestManyToOneChildRepository.save(
                Db1_Template_FkTestManyToOneChild.builder()
                        .childName("transaction test1")
                        .fkTestParent(parentEntity)
                        .rowDeleteDateStr("/")
                        .build()
        );

        db1TemplateFkTestManyToOneChildRepository.save(
                Db1_Template_FkTestManyToOneChild.builder()
                        .childName("transaction test2")
                        .fkTestParent(parentEntity)
                        .rowDeleteDateStr("/")
                        .build()
        );

        throw new RuntimeException("Transaction Rollback Test!");
    }
}
