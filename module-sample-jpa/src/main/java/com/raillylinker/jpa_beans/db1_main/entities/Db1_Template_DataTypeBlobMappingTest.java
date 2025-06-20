package com.raillylinker.jpa_beans.db1_main.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "data_type_blob_mapping_test",
        catalog = "template"
)
@Comment("ORM 과 Database 간 Blob 데이터 타입 매핑을 위한 테이블")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Db1_Template_DataTypeBlobMappingTest {
    // [기본 입력값이 존재하는 변수들]
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    @Comment("행 고유값")
    private Long uid;

    @Column(name = "row_create_date", nullable = false, columnDefinition = "DATETIME(3)")
    @CreationTimestamp
    @Comment("행 생성일")
    private LocalDateTime rowCreateDate;

    @Column(name = "row_update_date", nullable = false, columnDefinition = "DATETIME(3)")
    @UpdateTimestamp
    @Comment("행 수정일")
    private LocalDateTime rowUpdateDate;


    // ---------------------------------------------------------------------------------------------
    // Blob 데이터
    // BLOB 타입 데이터는 주로 이미지, 음악 파일, 문서 파일 등 이진 데이터를 저장하는 데 활용됩니다.
    @Column(name = "sample_tiny_blob_file_name", nullable = false, columnDefinition = "VARCHAR(255)")
    @Comment("최대 255 바이트 이진 데이터 파일 이름")
    private @NotNull String sampleTinyBlobFileName;
    @Lob
    @Column(name = "sample_tiny_blob", nullable = false, columnDefinition = "TINYBLOB")
    @Comment("최대 255 바이트 이진 데이터")
    private @NotNull byte[] sampleTinyBlob;
    @Column(name = "sample_blob_file_name", nullable = false, columnDefinition = "VARCHAR(255)")
    @Comment("최대 65,535바이트 이진 데이터 파일 이름")
    private @NotNull String sampleBlobFileName;
    @Lob
    @Column(name = "sample_blob", nullable = false, columnDefinition = "BLOB")
    @Comment("최대 65,535바이트 이진 데이터")
    private @NotNull byte[] sampleBlob;
    @Column(name = "sample_medium_blob_file_name", nullable = false, columnDefinition = "VARCHAR(255)")
    @Comment("최대 16,777,215 바이트 이진 데이터 파일 이름")
    private @NotNull String sampleMediumBlobFileName;
    @Lob
    @Column(name = "sample_medium_blob", nullable = false, columnDefinition = "MEDIUMBLOB")
    @Comment("최대 16,777,215 바이트 이진 데이터")
    private @NotNull byte[] sampleMediumBlob;
    @Column(name = "sample_long_blob_file_name", nullable = false, columnDefinition = "VARCHAR(255)")
    @Comment("최대 4,294,967,295 바이트 이진 데이터 파일 이름")
    private @NotNull String sampleLongBlobFileName;
    @Lob
    @Column(name = "sample_long_blob", nullable = false, columnDefinition = "LONGBLOB")
    @Comment("최대 4,294,967,295 바이트 이진 데이터")
    private @NotNull byte[] sampleLongBlob;
}
