package com.raillylinker.jpa_beans.db1_main.repositories;

import com.raillylinker.jpa_beans.db1_main.entities.Db1_Template_TestData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.jetbrains.annotations.*;

@Repository
public interface Db1_Template_TestData_Repository extends JpaRepository<Db1_Template_TestData, Long> {
    @Nullable
    Db1_Template_TestData findByUidAndRowDeleteDateStr(
            @NotNull Long uid,
            @NotNull String rowDeleteDateStr
    );

    @NotNull
    List<Db1_Template_TestData> findAllByRowDeleteDateStrOrderByRowCreateDate(
            @NotNull String rowDeleteDateStr
    );

    @NotNull
    List<Db1_Template_TestData> findAllByRowDeleteDateStrNotOrderByRowCreateDate(
            @NotNull String rowDeleteDateStr
    );

    @Query(
            nativeQuery = true,
            value = """
                    SELECT 
                    test_data.uid AS uid, 
                    test_data.row_create_date AS rowCreateDate, 
                    test_data.row_update_date AS rowUpdateDate, 
                    test_data.content AS content, 
                    test_data.random_num AS randomNum, 
                    test_data.test_datetime AS testDatetime, 
                    ABS(test_data.random_num-:num) AS distance 
                    FROM 
                    template.test_data AS test_data 
                    WHERE 
                    test_data.row_delete_date_str = '/' 
                    ORDER BY 
                    distance
                    """
    )
    @NotNull
    List<FindAllFromTemplateTestDataByNotDeletedWithRandomNumDistanceOutputVo> findAllFromTemplateTestDataByNotDeletedWithRandomNumDistance(
            @Param("num")
            @NotNull Integer num
    );

    interface FindAllFromTemplateTestDataByNotDeletedWithRandomNumDistanceOutputVo {
        @NotNull
        Long getUid();

        @NotNull
        LocalDateTime getRowCreateDate();

        @NotNull
        LocalDateTime getRowUpdateDate();

        @NotNull
        String getContent();

        @NotNull
        Integer getRandomNum();

        @NotNull
        LocalDateTime getTestDatetime();

        @NotNull
        Integer getDistance();
    }

    @Query(
            nativeQuery = true,
            value = """
                    SELECT 
                    test_data.uid AS uid, 
                    test_data.content AS content, 
                    test_data.random_num AS randomNum, 
                    test_data.test_datetime AS testDatetime, 
                    test_data.row_create_date AS rowCreateDate, 
                    test_data.row_update_date AS rowUpdateDate, 
                    ABS(TIMESTAMPDIFF(MICROSECOND, test_data.row_create_date, :date)) AS timeDiffMicroSec 
                    FROM 
                    template.test_data AS test_data 
                    WHERE 
                    test_data.row_delete_date_str = '/' 
                    ORDER BY 
                    timeDiffMicroSec
                    """
    )
    @NotNull
    List<FindAllFromTemplateTestDataByNotDeletedWithRowCreateDateDistanceOutputVo> findAllFromTemplateTestDataByNotDeletedWithRowCreateDateDistance(
            @Param("date")
            @NotNull LocalDateTime date
    );

    interface FindAllFromTemplateTestDataByNotDeletedWithRowCreateDateDistanceOutputVo {
        @NotNull
        Long getUid();

        @NotNull
        String getContent();

        @NotNull
        Integer getRandomNum();

        @NotNull
        LocalDateTime getTestDatetime();

        @NotNull
        LocalDateTime getRowCreateDate();

        @NotNull
        LocalDateTime getRowUpdateDate();

        @NotNull
        Long getTimeDiffMicroSec();
    }

    @NotNull
    Page<Db1_Template_TestData> findAllByRowDeleteDateStrOrderByRowCreateDate(
            @NotNull String rowDeleteDateStr,
            @NotNull Pageable pageable
    );

    @Query(
            nativeQuery = true,
            value = """
                    SELECT 
                    test_data.uid AS uid, 
                    test_data.row_create_date AS rowCreateDate, 
                    test_data.row_update_date AS rowUpdateDate, 
                    test_data.content AS content, 
                    test_data.random_num AS randomNum, 
                    test_data.test_datetime AS testDatetime, 
                    ABS(test_data.random_num-:num) AS distance 
                    FROM 
                    template.test_data AS test_data 
                    WHERE 
                    test_data.row_delete_date_str = '/' 
                    ORDER BY 
                    distance
                    """,
            countQuery = """
                    SELECT 
                    COUNT(*) 
                    FROM 
                    template.test_data AS test_data 
                    WHERE 
                    test_data.row_delete_date_str = '/'
                    """
    )
    @NotNull
    Page<FindPageAllFromTemplateTestDataByNotDeletedWithRandomNumDistanceOutputVo> findPageAllFromTemplateTestDataByNotDeletedWithRandomNumDistance(
            @Param("num")
            @NotNull Integer num,
            @NotNull Pageable pageable
    );

    interface FindPageAllFromTemplateTestDataByNotDeletedWithRandomNumDistanceOutputVo {
        @NotNull
        Long getUid();

        @NotNull
        LocalDateTime getRowCreateDate();

        @NotNull
        LocalDateTime getRowUpdateDate();

        @NotNull
        String getContent();

        @NotNull
        Integer getRandomNum();

        @NotNull
        LocalDateTime getTestDatetime();

        @NotNull
        Integer getDistance();
    }

    @Modifying
    @Query(
            nativeQuery = true, // Native Query 에서 Delete, Update 문은 이것을 붙여야함
            value = """
                    UPDATE 
                    template.test_data 
                    SET 
                    content = :content, 
                    test_datetime = :testDatetime 
                    WHERE 
                    uid = :uid
                    """
    )
    void updateToTemplateTestDataSetContentAndTestDateTimeByUid(
            @Param("uid")
            @NotNull Long uid,
            @Param("content")
            @NotNull String content,
            @Param("testDatetime")
            @NotNull LocalDateTime testDatetime
    );

//    @Valid
//    @NotNull
//    @org.jetbrains.annotations.NotNull
//    Long countByRowDeleteDateStr(
//            @Valid @NotNull @org.jetbrains.annotations.NotNull
//            String rowDeleteDateStr
//    );
//
//    @Valid
//    @NotNull
//    @org.jetbrains.annotations.NotNull
//    List<Db1_Template_TestData> findAllByContentOrderByRowCreateDate(
//            @Valid @NotNull @org.jetbrains.annotations.NotNull
//            String content
//    );
//
//    @Query("""
//                SELECT
//                template_test_data
//                FROM
//                Db1_Template_TestData AS template_test_data
//                WHERE
//                template_test_data.content = :content
//                ORDER BY
//                template_test_data.rowCreateDate DESC
//            """)
//    @Valid
//    @NotNull
//    @org.jetbrains.annotations.NotNull
//    List<Db1_Template_TestData> findAllByContentOrderByRowCreateDateJpql(
//            @Param("content")
//            @Valid @NotNull @org.jetbrains.annotations.NotNull
//            String content
//    );
}