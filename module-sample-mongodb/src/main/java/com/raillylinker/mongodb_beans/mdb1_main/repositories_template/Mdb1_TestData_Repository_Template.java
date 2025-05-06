package com.raillylinker.mongodb_beans.mdb1_main.repositories_template;

import com.raillylinker.configurations.mongodb_configs.Mdb1MainConfig;
import com.raillylinker.mongodb_beans.mdb1_main.documents.Mdb1_TestData;
import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import org.jetbrains.annotations.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

@Repository
public class Mdb1_TestData_Repository_Template {
    public Mdb1_TestData_Repository_Template(
            @Qualifier(Mdb1MainConfig.MONGO_DB_DIRECTORY_NAME)
            @NotNull MongoTemplate mongoTemplate
    ) {
        this.mongoTemplate = mongoTemplate;
    }

    private final @NotNull MongoTemplate mongoTemplate;


    // ----
    public @NotNull List<FindAllFromTemplateTestDataByNotDeletedWithRandomNumDistanceOutputVo> findAllFromTemplateTestDataByNotDeletedWithRandomNumDistance(
            @NotNull Integer num
    ) {
        @NotNull Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("rowDeleteDateStr").is("/")),
                Aggregation.project(
                                "content",
                                "randomNum",
                                "testDatetime",
                                "nullableValue",
                                "uid",
                                "rowCreateDate",
                                "rowUpdateDate",
                                "rowDeleteDateStr"
                        )
                        .andExpression("abs(randomNum - " + num + ")").as("distance"),
                Aggregation.sort(Sort.by(Sort.Order.asc("distance")))
        );

        return mongoTemplate.aggregate(
                aggregation,
                Mdb1_TestData.class,
                FindAllFromTemplateTestDataByNotDeletedWithRandomNumDistanceOutputVo.class
        ).getMappedResults();
    }

    @Data
    public static class FindAllFromTemplateTestDataByNotDeletedWithRandomNumDistanceOutputVo {
        private final @NotNull String content;
        private final @NotNull Integer randomNum;
        private final @NotNull LocalDateTime testDatetime;
        private final @Nullable String nullableValue;
        private final @NotNull String uid;
        private final @NotNull LocalDateTime rowCreateDate;
        private final @NotNull LocalDateTime rowUpdateDate;
        private final @NotNull String rowDeleteDateStr;
        private final @NotNull Integer distance;
    }


    // ----
    public @NotNull List<FindAllFromTemplateTestDataByNotDeletedWithRowCreateDateDistanceOutputVo> findAllFromTemplateTestDataByNotDeletedWithRowCreateDateDistance(
            @NotNull LocalDateTime date
    ) {
        @NotNull Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("rowDeleteDateStr").is("/")),
                Aggregation.project(
                                "content",
                                "randomNum",
                                "testDatetime",
                                "nullableValue",
                                "uid",
                                "rowCreateDate",
                                "rowUpdateDate",
                                "rowDeleteDateStr"
                        )
                        .and(DateOperators.dateValue(
                                ArithmeticOperators.Subtract.valueOf("rowCreateDate")
                                        .subtract(date.toInstant(ZoneOffset.UTC).toEpochMilli())
                        ).millisecond()).as("timeDiffMicroSec"), // rowCreateDate와 입력된 date 간 차이 계산
                Aggregation.sort(Sort.by(Sort.Order.asc("timeDiffMicroSec"))) // 차이값을 기준으로 오름차순 정렬
        );

        return mongoTemplate.aggregate(
                aggregation,
                Mdb1_TestData.class,
                FindAllFromTemplateTestDataByNotDeletedWithRowCreateDateDistanceOutputVo.class
        ).getMappedResults();
    }

    @Data
    public static class FindAllFromTemplateTestDataByNotDeletedWithRowCreateDateDistanceOutputVo {
        private final @NotNull String content;
        private final @NotNull Integer randomNum;
        private final @NotNull LocalDateTime testDatetime;
        private final @Nullable String nullableValue;
        private final @NotNull String uid;
        private final @NotNull LocalDateTime rowCreateDate;
        private final @NotNull LocalDateTime rowUpdateDate;
        private final @NotNull String rowDeleteDateStr;
        private final @NotNull Long timeDiffMicroSec;
    }


    // ----
    public @Nullable FindFromTemplateTestDataByNotDeletedAndUidOutputVo findFromTemplateTestDataByNotDeletedAndUid(
            @NotNull String testTableUid
    ) {
        @NotNull Criteria criteria = Criteria.where("rowDeleteDateStr").is("/") // 삭제되지 않은 데이터 필터링
                .and("uid").is(testTableUid); // uid 필터링

        @NotNull Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.project(
                        "content",
                        "randomNum",
                        "testDatetime",
                        "nullableValue",
                        "uid",
                        "rowCreateDate",
                        "rowUpdateDate",
                        "rowDeleteDateStr"
                )
        );

        @NotNull List<FindFromTemplateTestDataByNotDeletedAndUidOutputVo> results =
                mongoTemplate.aggregate(
                        aggregation,
                        Mdb1_TestData.class,
                        FindFromTemplateTestDataByNotDeletedAndUidOutputVo.class
                ).getMappedResults();

        return results.isEmpty() ? null : results.getFirst();
    }

    @Data
    public static class FindFromTemplateTestDataByNotDeletedAndUidOutputVo {
        private final @NotNull String content;
        private final @NotNull Integer randomNum;
        private final @NotNull LocalDateTime testDatetime;
        private final @Nullable String nullableValue;
        private final @NotNull String uid;
        private final @NotNull LocalDateTime rowCreateDate;
        private final @NotNull LocalDateTime rowUpdateDate;
        private final @NotNull String rowDeleteDateStr;
    }


    // ----
    public @Nullable CountFromTemplateTestDataByNotDeletedOutputVo countFromTemplateTestDataByNotDeleted() {
        @NotNull Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("rowDeleteDateStr").is("/")), // 삭제되지 않은 데이터 필터링
                Aggregation.count().as("count") // 문서 개수 카운트
        );

        return mongoTemplate.aggregate(
                aggregation,
                Mdb1_TestData.class,
                CountFromTemplateTestDataByNotDeletedOutputVo.class
        ).getUniqueMappedResult();
    }

    @Data
    public static class CountFromTemplateTestDataByNotDeletedOutputVo {
        private final @NotNull Long count;
    }


    // ----
    public @NotNull Page<FindPageAllFromTemplateTestDataByNotDeletedWithRandomNumDistanceOutputVo> findPageAllFromTemplateTestDataByNotDeletedWithRandomNumDistance(
            @NotNull Integer num,
            @NotNull Pageable pageable
    ) {
        @NotNull Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("rowDeleteDateStr").is("/")),
                Aggregation.project(
                                "content",
                                "randomNum",
                                "testDatetime",
                                "nullableValue",
                                "uid",
                                "rowCreateDate",
                                "rowUpdateDate",
                                "rowDeleteDateStr"
                        )
                        .andExpression("abs(randomNum - " + num + ")").as("distance"),
                Aggregation.sort(Sort.by(Sort.Order.asc("distance"))),
                Aggregation.skip((long) pageable.getPageNumber() * pageable.getPageSize()),
                Aggregation.limit(pageable.getPageSize())
        );

        @NotNull List<FindPageAllFromTemplateTestDataByNotDeletedWithRandomNumDistanceOutputVo> results = mongoTemplate.aggregate(
                aggregation,
                Mdb1_TestData.class,
                FindPageAllFromTemplateTestDataByNotDeletedWithRandomNumDistanceOutputVo.class
        ).getMappedResults();

        long totalElements = countFromTemplateTestDataByNotDeleted() != null ? Objects.requireNonNull(countFromTemplateTestDataByNotDeleted()).count : 0;

        return new PageImpl<>(results, pageable, totalElements);
    }

    @Data
    public static class FindPageAllFromTemplateTestDataByNotDeletedWithRandomNumDistanceOutputVo {
        private final @NotNull String content;
        private final @NotNull Integer randomNum;
        private final @NotNull LocalDateTime testDatetime;
        private final @Nullable String nullableValue;
        private final @NotNull String uid;
        private final @NotNull LocalDateTime rowCreateDate;
        private final @NotNull LocalDateTime rowUpdateDate;
        private final @NotNull String rowDeleteDateStr;
        private final @NotNull Integer distance;
    }


    // ----
    public @NotNull Page<FindPageAllFromTemplateTestDataBySearchKeywordOutputVo> findPageAllFromTemplateTestDataBySearchKeyword(
            @NotNull String searchKeyword,
            @NotNull Pageable pageable
    ) {
        // 검색 키워드로 content 필드를 부분 일치 검색
        @NotNull Criteria criteria = Criteria.where("rowDeleteDateStr").is("/") // 삭제되지 않은 데이터 필터링
                .and("content").regex(".*" + searchKeyword + ".*", "i"); // content 필드에서 searchKeyword를 포함하는 문서 검색, 대소문자 구분 안함

        @NotNull Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.project(
                        "content",
                        "randomNum",
                        "testDatetime",
                        "nullableValue",
                        "uid",
                        "rowCreateDate",
                        "rowUpdateDate",
                        "rowDeleteDateStr"
                ),
                Aggregation.skip((long) pageable.getPageNumber() * pageable.getPageSize()),
                Aggregation.limit(pageable.getPageSize())
        );

        @NotNull List<FindPageAllFromTemplateTestDataBySearchKeywordOutputVo> results = mongoTemplate.aggregate(
                aggregation,
                Mdb1_TestData.class,
                FindPageAllFromTemplateTestDataBySearchKeywordOutputVo.class
        ).getMappedResults();

        @NotNull Aggregation countAggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.count().as("count")
        );

        @Nullable CountFromTemplateTestDataByNotDeletedOutputVo countResult = mongoTemplate.aggregate(
                countAggregation,
                Mdb1_TestData.class,
                CountFromTemplateTestDataByNotDeletedOutputVo.class
        ).getUniqueMappedResult();

        long totalElements = countResult != null ? countResult.count : 0;

        return new PageImpl<>(results, pageable, totalElements);
    }

    @Data
    public static class FindPageAllFromTemplateTestDataBySearchKeywordOutputVo {
        private final @NotNull String content;
        private final @NotNull Integer randomNum;
        private final @NotNull LocalDateTime testDatetime;
        private final @Nullable String nullableValue;
        private final @NotNull String uid;
        private final @NotNull LocalDateTime rowCreateDate;
        private final @NotNull LocalDateTime rowUpdateDate;
        private final @NotNull String rowDeleteDateStr;
    }
}
