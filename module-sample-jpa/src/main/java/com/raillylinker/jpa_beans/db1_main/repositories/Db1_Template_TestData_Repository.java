package com.raillylinker.jpa_beans.db1_main.repositories;

import com.raillylinker.jpa_beans.db1_main.entities.Db1_Template_TestData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Db1_Template_TestData_Repository extends JpaRepository<Db1_Template_TestData, Long> {
    @Valid
    @NotNull
    @org.jetbrains.annotations.NotNull
    Page<Db1_Template_TestData> findAllByRowDeleteDateStrOrderByRowCreateDate(
            @Valid @NotNull @org.jetbrains.annotations.NotNull
            String rowDeleteDateStr,
            @Valid @NotNull @org.jetbrains.annotations.NotNull
            Pageable pageable
    );

    @Valid
    @NotNull
    @org.jetbrains.annotations.NotNull
    Long countByRowDeleteDateStr(
            @Valid @NotNull @org.jetbrains.annotations.NotNull
            String rowDeleteDateStr
    );

    @Valid
    @NotNull
    @org.jetbrains.annotations.NotNull
    Optional<Db1_Template_TestData> findByUidAndRowDeleteDateStr(
            @Valid @NotNull @org.jetbrains.annotations.NotNull
            Long uid,
            @Valid @NotNull @org.jetbrains.annotations.NotNull
            String rowDeleteDateStr
    );

    @Valid
    @NotNull
    @org.jetbrains.annotations.NotNull
    List<Db1_Template_TestData> findAllByRowDeleteDateStrOrderByRowCreateDate(
            @Valid @NotNull @org.jetbrains.annotations.NotNull
            String rowDeleteDateStr
    );

    @Valid
    @NotNull
    @org.jetbrains.annotations.NotNull
    List<Db1_Template_TestData> findAllByRowDeleteDateStrNotOrderByRowCreateDate(
            @Valid @NotNull @org.jetbrains.annotations.NotNull
            String rowDeleteDateStr
    );

    @Valid
    @NotNull
    @org.jetbrains.annotations.NotNull
    List<Db1_Template_TestData> findAllByContentOrderByRowCreateDate(
            @Valid @NotNull @org.jetbrains.annotations.NotNull
            String content
    );

    @Query("""
                SELECT
                template_test_data
                FROM
                Db1_Template_TestData AS template_test_data
                WHERE
                template_test_data.content = :content
                ORDER BY
                template_test_data.rowCreateDate DESC
            """)
    @Valid
    @NotNull
    @org.jetbrains.annotations.NotNull
    List<Db1_Template_TestData> findAllByContentOrderByRowCreateDateJpql(
            @Param("content")
            @Valid @NotNull @org.jetbrains.annotations.NotNull
            String content
    );
}