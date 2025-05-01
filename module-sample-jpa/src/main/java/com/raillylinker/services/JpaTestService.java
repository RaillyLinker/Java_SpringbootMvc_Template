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
                        ZonedDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")).toLocalDateTime()
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


//    // ----
//    // (DB Rows 조회 테스트 (페이징))
//    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
//    public @Nullable JpaTestController.SelectRowsPageSampleOutputVo selectRowsPageSample(
//            @NotNull HttpServletResponse httpServletResponse,
//            @NotNull Integer page,
//            @NotNull Integer pageElementsCount
//    ) {
//        Pageable pageable = PageRequest.of(page - 1, pageElementsCount);
//        Page<Db1_Template_TestData> entityList = db1TemplateTestsRepository.findAllByRowDeleteDateStrOrderByRowCreateDate("/", pageable);
//
//        List<MyServiceTkSampleDatabaseTestController.SelectRowsPageSampleOutputVo.TestEntityVo> testEntityVoList = new ArrayList<>();
//        for (Db1_Template_TestData entity : entityList) {
//            testEntityVoList.add(new MyServiceTkSampleDatabaseTestController.SelectRowsPageSampleOutputVo.TestEntityVo(
//                    entity.uid,
//                    entity.content,
//                    entity.randomNum,
//                    entity.testDatetime.atZone(ZoneId.systemDefault())
//                            .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
//                    entity.rowCreateDate.atZone(ZoneId.systemDefault())
//                            .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
//                    entity.rowUpdateDate.atZone(ZoneId.systemDefault())
//                            .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
//            ));
//        }
//
//        httpServletResponse.setStatus(HttpStatus.OK.value());
//        return new MyServiceTkSampleDatabaseTestController.SelectRowsPageSampleOutputVo(
//                entityList.getTotalElements(), testEntityVoList
//        );
//    }
//
//
//    // ----
//    // (DB Rows 조회 테스트 (네이티브 쿼리 페이징))
//    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
//    public @Nullable JpaTestController.SelectRowsNativeQueryPageSampleOutputVo selectRowsNativeQueryPageSample(
//            @NotNull HttpServletResponse httpServletResponse,
//            @NotNull Integer page,
//            @NotNull Integer pageElementsCount,
//            @NotNull Integer num
//    ) {
//        Pageable pageable = PageRequest.of(page - 1, pageElementsCount);
//        Page<Db1_Native_Repository.FindPageAllFromTemplateTestDataByNotDeletedWithRandomNumDistanceOutputVo> voList = db1NativeRepository.findPageAllFromTemplateTestDataByNotDeletedWithRandomNumDistance(num, pageable);
//
//        List<MyServiceTkSampleDatabaseTestController.SelectRowsNativeQueryPageSampleOutputVo.TestEntityVo> testEntityVoList = new ArrayList<>();
//        for (Db1_Native_Repository.FindPageAllFromTemplateTestDataByNotDeletedWithRandomNumDistanceOutputVo vo : voList) {
//            testEntityVoList.add(new MyServiceTkSampleDatabaseTestController.SelectRowsNativeQueryPageSampleOutputVo.TestEntityVo(
//                    vo.getUid(),
//                    vo.getContent(),
//                    vo.getRandomNum(),
//                    vo.getTestDatetime().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
//                    vo.getRowCreateDate().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
//                    vo.getRowUpdateDate().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
//                    vo.getDistance()
//            ));
//        }
//
//        httpServletResponse.setStatus(HttpStatus.OK.value());
//        return new MyServiceTkSampleDatabaseTestController.SelectRowsNativeQueryPageSampleOutputVo(
//                voList.getTotalElements(), testEntityVoList
//        );
//    }
//
//
//    // ----
//    // (DB Row 수정 테스트)
//    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME)
//    public @Nullable JpaTestController.UpdateRowSampleOutputVo updateRowSample(
//            @NotNull HttpServletResponse httpServletResponse,
//            @NotNull Long testTableUid,
//            @NotNull JpaTestController.UpdateRowSampleInputVo inputVo
//    ) {
//        Optional<Db1_Template_TestData> oldEntityOpt = db1TemplateTestsRepository.findByUidAndRowDeleteDateStr(testTableUid, "/");
//
//        if (oldEntityOpt.isEmpty() || !"/".equals(oldEntityOpt.get().rowDeleteDateStr)) {
//            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
//            httpServletResponse.setHeader("api-result-code", "1");
//            return null;
//        }
//
//        Db1_Template_TestData oldEntity = oldEntityOpt.get();
//
//        oldEntity.content = inputVo.content();
//        oldEntity.testDatetime = ZonedDateTime.parse(inputVo.dateString(), DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")).toLocalDateTime();
//
//        Db1_Template_TestData result = db1TemplateTestsRepository.save(oldEntity);
//
//        httpServletResponse.setStatus(HttpStatus.OK.value());
//        return new MyServiceTkSampleDatabaseTestController.UpdateRowSampleOutputVo(
//                result.uid,
//                result.content,
//                result.randomNum,
//                result.testDatetime.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
//                result.rowCreateDate.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
//                result.rowUpdateDate.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
//        );
//    }
//
//
//    // ----
//    // (DB Row 수정 테스트 (네이티브 쿼리))
//    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME)
//    public void updateRowNativeQuerySample(
//            HttpServletResponse httpServletResponse,
//            Long testTableUid,
//            JpaTestController.UpdateRowNativeQuerySampleInputVo inputVo
//    ) {
//        Optional<Db1_Template_TestData> testEntityOpt = db1TemplateTestsRepository.findByUidAndRowDeleteDateStr(testTableUid, "/");
//
//        if (testEntityOpt.isEmpty() || !"/".equals(testEntityOpt.get().rowDeleteDateStr)) {
//            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
//            httpServletResponse.setHeader("api-result-code", "1");
//            return;
//        }
//
//        Db1_Template_TestData testEntity = testEntityOpt.get();
//
//        db1NativeRepository.updateToTemplateTestDataSetContentAndTestDateTimeByUid(
//                testTableUid,
//                inputVo.content(),
//                ZonedDateTime.parse(inputVo.dateString(), DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")).toLocalDateTime()
//        );
//
//        httpServletResponse.setStatus(HttpStatus.OK.value());
//    }
//
//
//    // ----
//    // (DB 정보 검색 테스트)
//    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
//    public @Nullable JpaTestController.SelectRowWhereSearchingKeywordSampleOutputVo selectRowWhereSearchingKeywordSample(
//            @NotNull HttpServletResponse httpServletResponse,
//            @NotNull Integer page,
//            @NotNull Integer pageElementsCount,
//            @NotNull String searchKeyword
//    ) {
//        Pageable pageable = PageRequest.of(page - 1, pageElementsCount);
//        Page<Db1_Native_Repository.FindPageAllFromTemplateTestDataBySearchKeywordOutputVo> voList = db1NativeRepository.findPageAllFromTemplateTestDataBySearchKeyword(searchKeyword, pageable);
//
//        List<MyServiceTkSampleDatabaseTestController.SelectRowWhereSearchingKeywordSampleOutputVo.TestEntityVo> testEntityVoList = new ArrayList<>();
//        for (Db1_Native_Repository.FindPageAllFromTemplateTestDataBySearchKeywordOutputVo vo : voList) {
//            testEntityVoList.add(new MyServiceTkSampleDatabaseTestController.SelectRowWhereSearchingKeywordSampleOutputVo.TestEntityVo(
//                    vo.getUid(),
//                    vo.getContent(),
//                    vo.getRandomNum(),
//                    vo.getTestDatetime().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
//                    vo.getRowCreateDate().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
//                    vo.getRowUpdateDate().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
//            ));
//        }
//
//        httpServletResponse.setStatus(HttpStatus.OK.value());
//        return new MyServiceTkSampleDatabaseTestController.SelectRowWhereSearchingKeywordSampleOutputVo(
//                voList.getTotalElements(), testEntityVoList
//        );
//    }
//
//
//    // ----
//    // (트랜젝션 동작 테스트)
//    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME)
//    public void transactionTest(
//            @NotNull HttpServletResponse httpServletResponse
//    ) {
//        db1TemplateTestsRepository.save(new Db1_Template_TestData(
//                "error test",
//                new Random().nextInt(100000000),
//                LocalDateTime.now()
//        ));
//
//        throw new RuntimeException("Transaction Rollback Test!");
//    }
//
//
//    // ----
//    // (트랜젝션 비동작 테스트)
//    public void nonTransactionTest(
//            @NotNull HttpServletResponse httpServletResponse
//    ) {
//        db1TemplateTestsRepository.save(new Db1_Template_TestData(
//                "error test",
//                new Random().nextInt(100000000),
//                LocalDateTime.now()
//        ));
//
//        throw new RuntimeException("No Transaction Exception Test!");
//    }
//
//
//    // ----
//    // (트랜젝션 비동작 테스트(try-catch))
//    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME)
//    public void tryCatchNonTransactionTest(
//            @NotNull HttpServletResponse httpServletResponse
//    ) {
//        try {
//            db1TemplateTestsRepository.save(new Db1_Template_TestData(
//                    "error test",
//                    new Random().nextInt(100000000),
//                    LocalDateTime.now()
//            ));
//
//            throw new Exception("Transaction Rollback Test!");
//        } catch (Exception e) {
//            classLogger.error("error ", e);
//        }
//    }
//
//
//    // ----
//    // (DB Rows 조회 테스트 (중복 없는 네이티브 쿼리 페이징))
//    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
//    public @Nullable JpaTestController.SelectRowsNoDuplicatePagingSampleOutputVo selectRowsNoDuplicatePagingSample(
//            @NotNull HttpServletResponse httpServletResponse,
//            @Nullable Long lastItemUid,
//            @NotNull Integer pageElementsCount
//    ) {
//        List<Db1_Native_Repository.FindAllFromTemplateTestDataForNoDuplicatedPagingOutputVo> voList = db1NativeRepository.findAllFromTemplateTestDataForNoDuplicatedPaging(lastItemUid, pageElementsCount);
//        Long count = db1NativeRepository.countFromTemplateTestDataByNotDeleted();
//
//        List<MyServiceTkSampleDatabaseTestController.SelectRowsNoDuplicatePagingSampleOutputVo.TestEntityVo> testEntityVoList = new ArrayList<>();
//        for (Db1_Native_Repository.FindAllFromTemplateTestDataForNoDuplicatedPagingOutputVo vo : voList) {
//            testEntityVoList.add(new MyServiceTkSampleDatabaseTestController.SelectRowsNoDuplicatePagingSampleOutputVo.TestEntityVo(
//                    vo.getUid(),
//                    vo.getContent(),
//                    vo.getRandomNum(),
//                    vo.getTestDatetime().atZone(ZoneId.systemDefault())
//                            .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
//                    vo.getRowCreateDate().atZone(ZoneId.systemDefault())
//                            .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
//                    vo.getRowUpdateDate().atZone(ZoneId.systemDefault())
//                            .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
//            ));
//        }
//
//        httpServletResponse.setStatus(HttpStatus.OK.value());
//        return new MyServiceTkSampleDatabaseTestController.SelectRowsNoDuplicatePagingSampleOutputVo(count, testEntityVoList);
//    }
//
//
//    // ----
//    // (DB Rows 조회 테스트 (카운팅))
//    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
//    public @Nullable JpaTestController.SelectRowsCountSampleOutputVo selectRowsCountSample(
//            @NotNull HttpServletResponse httpServletResponse
//    ) {
//        Long count = db1TemplateTestsRepository.countByRowDeleteDateStr("/");
//
//        httpServletResponse.setStatus(HttpStatus.OK.value());
//        return new MyServiceTkSampleDatabaseTestController.SelectRowsCountSampleOutputVo(count);
//    }
//
//
//    // ----
//    // (DB Rows 조회 테스트 (네이티브 카운팅))
//    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
//    public @Nullable JpaTestController.SelectRowsCountByNativeQuerySampleOutputVo selectRowsCountByNativeQuerySample(
//            @NotNull HttpServletResponse httpServletResponse
//    ) {
//        Long count = db1NativeRepository.countFromTemplateTestDataByNotDeleted();
//
//        httpServletResponse.setStatus(HttpStatus.OK.value());
//        return new MyServiceTkSampleDatabaseTestController.SelectRowsCountByNativeQuerySampleOutputVo(count);
//    }
//
//
//    // ----
//    // (DB Row 조회 테스트 (네이티브))
//    @Transactional(transactionManager = Db1MainConfig.TRANSACTION_NAME, readOnly = true)
//    public @Nullable JpaTestController.SelectRowByNativeQuerySampleOutputVo selectRowByNativeQuerySample(
//            @NotNull HttpServletResponse httpServletResponse,
//            @NotNull Long testTableUid
//    ) {
//        Optional<Db1_Native_Repository.FindFromTemplateTestDataByNotDeletedAndUidOutputVo> entityOpt = db1NativeRepository.findFromTemplateTestDataByNotDeletedAndUid(testTableUid);
//
//        if (entityOpt.isEmpty()) {
//            httpServletResponse.setStatus(HttpStatus.OK.value());
//            httpServletResponse.setHeader("api-result-code", "1");
//            return null;
//        }
//
//        Db1_Native_Repository.FindFromTemplateTestDataByNotDeletedAndUidOutputVo entity = entityOpt.get();
//
//        httpServletResponse.setStatus(HttpStatus.OK.value());
//        return new MyServiceTkSampleDatabaseTestController.SelectRowByNativeQuerySampleOutputVo(
//                entity.getUid(),
//                entity.getContent(),
//                entity.getRandomNum(),
//                entity.getTestDatetime().atZone(ZoneId.systemDefault())
//                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
//                entity.getRowCreateDate().atZone(ZoneId.systemDefault())
//                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
//                entity.getRowUpdateDate().atZone(ZoneId.systemDefault())
//                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
//        );
//    }
}
