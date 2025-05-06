package com.raillylinker.mongodb_beans.mdb1_main.documents;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "test_data")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mdb1_TestData {
    // [기본 입력값이 존재하는 변수들]
    // 행 고유값
    @Id
    private String uid;

    // 행 생성일
    @CreatedDate
    @Field("row_create_date")
    private LocalDateTime rowCreateDate;

    // 행 수정일
    @LastModifiedDate
    @Field("row_update_date")
    private LocalDateTime rowUpdateDate;


    // ---------------------------------------------------------------------------------------------
    // 행 삭제일 (yyyy_MM_dd_T_HH_mm_ss_SSS_z, 삭제되지 않았다면 "/")
    @Field("row_delete_date_str")
    private @NotNull String rowDeleteDateStr;

    // 테스트 본문
    @Field("content")
    private @NotNull String content;

    // 테스트 랜덤 번호
    @Field("random_num")
    private @NotNull Integer randomNum;

    // 테스트용 일시 데이터
    @Field("test_datetime")
    private @NotNull LocalDateTime testDatetime;

    // 테스트용 nullable 데이터
    @Field("nullable_value")
    private @Nullable String nullableValue;
}
