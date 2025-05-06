package com.raillylinker.mongodb_beans.mdb1_main.documents;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "logical_delete_unique_data")
@CompoundIndexes({
        @CompoundIndex(
                name = "unique_value_row_delete_date_str_idx",
                def = "{'unique_value': 1, 'row_delete_date_str': 1}",
                unique = true
        )
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mdb1_LogicalDeleteUniqueData {
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

    // 논리적 삭제 유니크 제약 테스트 값
    @Field("unique_value")
    private @NotNull Integer uniqueValue;
}
