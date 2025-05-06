package com.raillylinker.services;

import com.raillylinker.configurations.mongodb_configs.Mdb1MainConfig;
import com.raillylinker.controllers.MongoDbTestController;
import com.raillylinker.mongodb_beans.mdb1_main.documents.Mdb1_TestData;
import com.raillylinker.mongodb_beans.mdb1_main.repositories.Mdb1_LogicalDeleteUniqueData_Repository;
import com.raillylinker.mongodb_beans.mdb1_main.repositories.Mdb1_TestData_Repository;
import com.raillylinker.mongodb_beans.mdb1_main.repositories_template.Mdb1_TestData_Repository_Template;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
import java.util.Random;

@Service
public class MongoDbTestService {
    public MongoDbTestService(
            // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
            @Value("${spring.profiles.active:default}")
            @NotNull String activeProfile,
            @NotNull Mdb1_TestData_Repository mdb1TestDataRepository,
            @NotNull Mdb1_TestData_Repository_Template mdb1TestDataRepositoryTemplate,
            @NotNull Mdb1_LogicalDeleteUniqueData_Repository mbdMdb1LogicalDeleteUniqueDataRepository
    ) {
        this.activeProfile = activeProfile;
        this.mdb1TestDataRepository = mdb1TestDataRepository;
        this.mdb1TestDataRepositoryTemplate = mdb1TestDataRepositoryTemplate;
        this.mbdMdb1LogicalDeleteUniqueDataRepository = mbdMdb1LogicalDeleteUniqueDataRepository;
    }

    // <멤버 변수 공간>
    private final @NotNull String activeProfile;

    private final @NotNull Logger classLogger = LoggerFactory.getLogger(MongoDbTestService.class);

    private final Mdb1_TestData_Repository mdb1TestDataRepository;
    private final Mdb1_TestData_Repository_Template mdb1TestDataRepositoryTemplate;
    private final Mdb1_LogicalDeleteUniqueData_Repository mbdMdb1LogicalDeleteUniqueDataRepository;


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    // (DB document 입력 테스트 API)
    @Transactional(transactionManager = Mdb1MainConfig.TRANSACTION_NAME) // ReplicaSet 환경이 아니면 에러가 납니다.
    public @Nullable MongoDbTestController.InsertDataSampleOutputVo insertDataSample(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull MongoDbTestController.InsertDataSampleInputVo inputVo
    ) {
        @NotNull Mdb1_TestData savedEntity = mdb1TestDataRepository.save(
                Mdb1_TestData.builder()
                        .rowDeleteDateStr("/")
                        .content(inputVo.getContent())
                        .randomNum(new Random().nextInt(100_000_000))
                        .testDatetime(
                                ZonedDateTime.parse(inputVo.getDateString(), DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
                                        .withZoneSameInstant(ZoneId.systemDefault())
                                        .toLocalDateTime()
                        )
                        .nullableValue(inputVo.getNullableValue())
                        .build()
        );

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new MongoDbTestController.InsertDataSampleOutputVo(
                savedEntity.getUid(),
                savedEntity.getContent(),
                savedEntity.getRandomNum(),
                savedEntity.getTestDatetime().atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                savedEntity.getNullableValue(),
                savedEntity.getRowCreateDate().atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                savedEntity.getRowUpdateDate().atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                savedEntity.getRowDeleteDateStr()
        );
    }


    // ----
    // (DB Rows 삭제 테스트 API)
    @Transactional(transactionManager = Mdb1MainConfig.TRANSACTION_NAME) // ReplicaSet 환경이 아니면 에러가 납니다.
    public void deleteRowsSample(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull Boolean deleteLogically
    ) {
        if (deleteLogically) {
            @NotNull List<Mdb1_TestData> entities = mdb1TestDataRepository.findAllByRowDeleteDateStrOrderByRowCreateDate("/");
            for (@NotNull Mdb1_TestData entity : entities) {
                entity.setRowDeleteDateStr(
                        LocalDateTime.now().atZone(ZoneId.systemDefault())
                                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z"))
                );
                mdb1TestDataRepository.save(entity);
            }
        } else {
            mdb1TestDataRepository.deleteAll();
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());
    }


    // ----
    // (DB Row 삭제 테스트)
    @Transactional(transactionManager = Mdb1MainConfig.TRANSACTION_NAME) // ReplicaSet 환경이 아니면 에러가 납니다.
    public void deleteRowSample(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull String id,
            @NotNull Boolean deleteLogically
    ) {
        @Nullable Mdb1_TestData entity = mdb1TestDataRepository.findByUidAndRowDeleteDateStr(id, "/");

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
            mdb1TestDataRepository.save(entity);
        } else {
            mdb1TestDataRepository.deleteById(id);
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());
    }


    // ----
    // (DB Rows 조회 테스트)
    @Transactional(transactionManager = Mdb1MainConfig.TRANSACTION_NAME, readOnly = true) // ReplicaSet 환경이 아니면 에러가 납니다.
    public @Nullable MongoDbTestController.SelectRowsSampleOutputVo selectRowsSample(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        @NotNull List<Mdb1_TestData> activeEntities = mdb1TestDataRepository.findAllByRowDeleteDateStrOrderByRowCreateDate("/");
        @NotNull List<MongoDbTestController.SelectRowsSampleOutputVo.TestEntityVo> activeVoList = new ArrayList<>();
        for (@NotNull Mdb1_TestData entity : activeEntities) {
            activeVoList.add(
                    new MongoDbTestController.SelectRowsSampleOutputVo.TestEntityVo(
                            entity.getUid(),
                            entity.getContent(),
                            entity.getRandomNum(),
                            entity.getTestDatetime().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            entity.getNullableValue(),
                            entity.getRowCreateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            entity.getRowUpdateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            entity.getRowDeleteDateStr()
                    )
            );
        }

        @NotNull List<Mdb1_TestData> deletedEntities = mdb1TestDataRepository.findAllByRowDeleteDateStrNotOrderByRowCreateDate("/");
        @NotNull List<MongoDbTestController.SelectRowsSampleOutputVo.TestEntityVo> deletedVoList = new ArrayList<>();
        for (@NotNull Mdb1_TestData entity : deletedEntities) {
            deletedVoList.add(
                    new MongoDbTestController.SelectRowsSampleOutputVo.TestEntityVo(
                            entity.getUid(),
                            entity.getContent(),
                            entity.getRandomNum(),
                            entity.getTestDatetime().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            entity.getNullableValue(),
                            entity.getRowCreateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            entity.getRowUpdateDate().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")),
                            entity.getRowDeleteDateStr()
                    )
            );
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new MongoDbTestController.SelectRowsSampleOutputVo(
                activeVoList,
                deletedVoList
        );
    }
}
