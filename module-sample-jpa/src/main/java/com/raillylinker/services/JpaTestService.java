package com.raillylinker.services;

import com.raillylinker.configurations.jpa_configs.Db1MainConfig;
import com.raillylinker.controllers.JpaTestController;
import com.raillylinker.jpa_beans.db1_main.entities.Db1_Template_FkTestManyToOneChild;
import com.raillylinker.jpa_beans.db1_main.entities.Db1_Template_FkTestParent;
import com.raillylinker.jpa_beans.db1_main.entities.Db1_Template_LogicalDeleteUniqueData;
import com.raillylinker.jpa_beans.db1_main.entities.Db1_Template_TestData;
import com.raillylinker.jpa_beans.db1_main.repositories.Db1_Template_FkTestManyToOneChild_Repository;
import com.raillylinker.jpa_beans.db1_main.repositories.Db1_Template_FkTestParent_Repository;
import com.raillylinker.jpa_beans.db1_main.repositories.Db1_Template_LogicalDeleteUniqueData_Repository;
import com.raillylinker.jpa_beans.db1_main.repositories.Db1_Template_TestData_Repository;
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
            @NotNull Db1_Template_FkTestManyToOneChild_Repository db1TemplateFkTestManyToOneChildRepository
    ) {
        this.activeProfile = activeProfile;
        this.customUtil = customUtil;
        this.db1TemplateTestDataRepository = db1TemplateTestDataRepository;
        this.db1TemplateLogicalDeleteUniqueDataRepository = db1TemplateLogicalDeleteUniqueDataRepository;
        this.db1TemplateFkTestParentRepository = db1TemplateFkTestParentRepository;
        this.db1TemplateFkTestManyToOneChildRepository = db1TemplateFkTestManyToOneChildRepository;
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
                                .withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
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
                            entity.uid,
                            entity.content,
                            entity.randomNum,
                            entity.testDatetime.atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            entity.rowCreateDate.atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            entity.rowUpdateDate.atZone(ZoneId.systemDefault())
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

        oldEntity.content = inputVo.getContent();
        oldEntity.testDatetime = ZonedDateTime.parse(inputVo.getDateString(), DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
                .withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();

        @NotNull Db1_Template_TestData result = db1TemplateTestDataRepository.save(oldEntity);

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.UpdateRowSampleOutputVo(
                result.uid,
                result.content,
                result.randomNum,
                result.testDatetime.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                result.rowCreateDate.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                result.rowUpdateDate.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
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
                new Db1_Template_TestData(
                        "error test",
                        (int) (Math.random() * 99999999), // Random number between 0 and 99999999
                        LocalDateTime.now()
                )
        );

        throw new RuntimeException("Transaction Rollback Test!");
    }


    // ----
    // (트랜젝션 비동작 테스트)
    public void nonTransactionTest(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        db1TemplateTestDataRepository.save(
                new Db1_Template_TestData(
                        "error test",
                        (int) (Math.random() * 99999999), // Random number between 0 and 99999999
                        LocalDateTime.now()
                )
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
                    new Db1_Template_TestData(
                            "error test",
                            (int) (Math.random() * 99999999), // Random number between 0 and 99999999
                            LocalDateTime.now()
                    )
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
                new Db1_Template_LogicalDeleteUniqueData(
                        inputVo.getUniqueValue()
                )
        );

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.InsertUniqueTestTableRowSampleOutputVo(
                result.uid,
                result.uniqueValue,
                result.rowCreateDate.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                result.rowUpdateDate.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                result.rowDeleteDateStr
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
                            resultEntity.uid,
                            resultEntity.uniqueValue,
                            resultEntity.rowCreateDate.atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            resultEntity.rowUpdateDate.atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            resultEntity.rowDeleteDateStr
                    )
            );
        }

        @NotNull List<Db1_Template_LogicalDeleteUniqueData> logicalDeleteEntityVoList =
                db1TemplateLogicalDeleteUniqueDataRepository.findAllByRowDeleteDateStrNotOrderByRowCreateDate("/");
        @NotNull List<JpaTestController.SelectUniqueTestTableRowsSampleOutputVo.TestEntityVo> logicalDeleteVoList = new ArrayList<>();
        for (@NotNull Db1_Template_LogicalDeleteUniqueData resultEntity : logicalDeleteEntityVoList) {
            logicalDeleteVoList.add(
                    new JpaTestController.SelectUniqueTestTableRowsSampleOutputVo.TestEntityVo(
                            resultEntity.uid,
                            resultEntity.uniqueValue,
                            resultEntity.rowCreateDate.atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            resultEntity.rowUpdateDate.atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            resultEntity.rowDeleteDateStr
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

        oldEntity.uniqueValue = inputVo.getUniqueValue();

        @NotNull Db1_Template_LogicalDeleteUniqueData result =
                db1TemplateLogicalDeleteUniqueDataRepository.save(oldEntity);

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.UpdateUniqueTestTableRowSampleOutputVo(
                result.uid,
                result.uniqueValue,
                result.rowCreateDate.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                result.rowUpdateDate.atZone(ZoneId.systemDefault())
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

        entity.rowDeleteDateStr =
                LocalDateTime.now().atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"));
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
                        new Db1_Template_FkTestParent(
                                inputVo.getFkParentName()
                        )
                );

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.InsertFkParentRowSampleOutputVo(
                result.uid,
                result.parentName,
                result.rowCreateDate.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                result.rowUpdateDate.atZone(ZoneId.systemDefault())
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
                        new Db1_Template_FkTestManyToOneChild(
                                inputVo.getFkChildName(),
                                parentEntity
                        )
                );

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new JpaTestController.InsertFkChildRowSampleOutputVo(
                result.uid,
                result.childName,
                result.fkTestParent.parentName,
                result.rowCreateDate.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                result.rowUpdateDate.atZone(ZoneId.systemDefault())
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
                                childEntity.uid,
                                childEntity.childName,
                                childEntity.rowCreateDate.atZone(ZoneId.systemDefault())
                                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                                childEntity.rowUpdateDate.atZone(ZoneId.systemDefault())
                                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
                        )
                );
            }

            entityVoList.add(
                    new JpaTestController.SelectFkTestTableRowsSampleOutputVo.ParentEntityVo(
                            resultEntity.uid,
                            resultEntity.parentName,
                            resultEntity.rowCreateDate.atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            resultEntity.rowUpdateDate.atZone(ZoneId.systemDefault())
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
        // todo
        return null;
    }


    // ----
    // (Native Query 반환값 테스트)
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME)
    public @Nullable JpaTestController.GetNativeQueryReturnValueTestOutputVo getNativeQueryReturnValueTest(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull Boolean inputVal
    ) {
        // todo
        return null;
    }


    // ----
    // (SQL Injection 테스트)
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
    public @Nullable JpaTestController.SqlInjectionTestOutputVo sqlInjectionTest(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull String searchKeyword
    ) {
        // todo
        return null;
    }


    // ----
    // (외래키 관련 테이블 Rows 조회 (네이티브 쿼리, 부모 테이블을 자식 테이블의 가장 최근 데이터만 Join))
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
    public @Nullable JpaTestController.SelectFkTableRowsWithLatestChildSampleOutputVo selectFkTableRowsWithLatestChildSample(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        // todo
        return null;
    }


    // ----
    // (외래키 관련 테이블 Rows 조회 (QueryDsl))
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
    public @Nullable JpaTestController.SelectFkTableRowsWithQueryDslOutputVo selectFkTableRowsWithQueryDsl(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        // todo
        return null;
    }


    // ----
    // (외래키 관련 테이블 Rows 조회 및 부모 테이블 이름으로 필터링 (QueryDsl))
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
    public @Nullable JpaTestController.SelectFkTableRowsByParentNameFilterWithQueryDslOutputVo selectFkTableRowsByParentNameFilterWithQueryDsl(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull String parentName
    ) {
        // todo
        return null;
    }


    // ----
    // (외래키 관련 테이블 부모 테이블 고유번호로 자식 테이블 리스트 검색 (QueryDsl))
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
    public @Nullable JpaTestController.SelectFkTableChildListWithQueryDslOutputVo selectFkTableChildListWithQueryDsl(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull Long parentUid
    ) {
        // todo
        return null;
    }


    // ----
    // (외래키 자식 테이블 Row 삭제 테스트)
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME)
    public void deleteFkChildRowSample(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull Long index
    ) {
        // todo
    }


    // ----
    // (외래키 부모 테이블 Row 삭제 테스트 (Cascade 기능 확인))
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME)
    public void deleteFkParentRowSample(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull Long index
    ) {
        // todo
    }


    // ----
    // (외래키 테이블 트랜젝션 동작 테스트)
    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME)
    public void fkTableTransactionTest(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        // todo
    }


    // ----
    // (외래키 테이블 트랜젝션 비동작 테스트)
    public void fkTableNonTransactionTest(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        // todo
    }
}
