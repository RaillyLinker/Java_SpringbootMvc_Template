package com.raillylinker.jpa_beans.db1_main.entities;

import com.raillylinker.converters.JsonMapConverter;
import com.raillylinker.converters.MySqlSetConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Set;

@Entity
@Table(
        name = "data_type_mapping_test",
        catalog = "template"
)
@Comment("ORM 과 Database 간 데이터 타입 매핑을 위한 테이블")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Db1_Template_DataTypeMappingTest {
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
    // [입력값 수동 입력 변수들]
    // 숫자 데이터
    @Column(name = "sample_tiny_int", nullable = false, columnDefinition = "TINYINT")
    @Comment("-128 ~ 127 정수 (1Byte)")
    private @NotNull Byte sampleTinyInt;
    @Column(name = "sample_tiny_int_unsigned", nullable = false, columnDefinition = "TINYINT UNSIGNED")
    @Comment("0 ~ 255 정수 (1Byte)")
    private @NotNull Short sampleTinyIntUnsigned;
    @Column(name = "sample_small_int", nullable = false, columnDefinition = "SMALLINT")
    @Comment("-32,768 ~ 32,767 정수 (2Byte)")
    private @NotNull Short sampleSmallInt;
    @Column(name = "sample_small_int_unsigned", nullable = false, columnDefinition = "SMALLINT UNSIGNED")
    @Comment("0 ~ 65,535 정수 (2Byte)")
    private @NotNull Integer sampleSmallIntUnsigned;
    @Column(name = "sample_medium_int", nullable = false, columnDefinition = "MEDIUMINT")
    @Comment("-8,388,608 ~ 8,388,607 정수 (3Byte)")
    private @NotNull Integer sampleMediumInt;
    @Column(name = "sample_medium_int_unsigned", nullable = false, columnDefinition = "MEDIUMINT UNSIGNED")
    @Comment("0 ~ 16,777,215 정수 (3Byte)")
    private @NotNull Integer sampleMediumIntUnsigned;
    @Column(name = "sample_int", nullable = false, columnDefinition = "INT")
    @Comment("-2,147,483,648 ~ 2,147,483,647 정수 (4Byte)")
    private @NotNull Integer sampleInt;
    @Column(name = "sample_int_unsigned", nullable = false, columnDefinition = "INT UNSIGNED")
    @Comment("0 ~ 4,294,967,295 정수 (4Byte)")
    private @NotNull Long sampleIntUnsigned;
    @Column(name = "sample_big_int", nullable = false, columnDefinition = "BIGINT")
    @Comment("-2^63 ~ 2^63-1 정수 (8Byte)")
    private @NotNull Long sampleBigInt;
    @Column(name = "sample_big_int_unsigned", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    @Comment("0 ~ 2^64-1 정수 (8Byte)")
    private @NotNull BigInteger sampleBigIntUnsigned;
    @Column(name = "sample_float", nullable = false, columnDefinition = "FLOAT")
    @Comment("-3.4E38 ~ 3.4E38 단정밀도 부동소수점 (4Byte)")
    private @NotNull Float sampleFloat;
    @Column(name = "sample_float_unsigned", nullable = false, columnDefinition = "FLOAT UNSIGNED")
    @Comment("0 ~ 3.402823466E+38 단정밀도 부동소수점 (4Byte)")
    private @NotNull Float sampleFloatUnsigned;
    @Column(name = "sample_double", nullable = false, columnDefinition = "DOUBLE")
    @Comment("-1.7E308 ~ 1.7E308 배정밀도 부동소수점 (8Byte)")
    private @NotNull Double sampleDouble;
    @Column(name = "sample_double_unsigned", nullable = false, columnDefinition = "DOUBLE UNSIGNED")
    @Comment("0 ~ 1.7976931348623157E+308 배정밀도 부동소수점 (8Byte)")
    private @NotNull Double sampleDoubleUnsigned;
    @Column(name = "sample_decimal_p65_s10", nullable = false, columnDefinition = "DECIMAL(65, 10)")
    @Comment("p(전체 자릿수, 최대 65), s(소수점 아래 자릿수, p 보다 작거나 같아야 함) 설정 가능 고정 소수점 숫자")
    private @NotNull BigDecimal sampleDecimalP65S10;

    // 시간 데이터
    @Column(name = "sample_date", nullable = false, columnDefinition = "DATE")
    @Comment("1000-01-01 ~ 9999-12-31 날짜 데이터")
    private @NotNull LocalDate sampleDate;
    @Column(name = "sample_datetime", nullable = false, columnDefinition = "DATETIME(3)")
    @Comment("1000-01-01 00:00:00 ~ 9999-12-31 23:59:59 날짜 데이터")
    private @NotNull LocalDateTime sampleDateTime;
    @Column(name = "sample_time", nullable = false, columnDefinition = "TIME(3)")
    @Comment("-838:59:59 ~ 838:59:59 시간 데이터")
    private @NotNull LocalTime sampleTime;
    @Column(name = "sample_timestamp", nullable = false, columnDefinition = "TIMESTAMP(3)")
    @Comment("1970-01-01 00:00:01 ~ 2038-01-19 03:14:07 날짜 데이터 저장시 UTC 기준으로 저장되고, 조회시 시스템 설정에 맞게 반환")
    private @NotNull LocalDateTime sampleTimestamp;
    @Column(name = "sample_year", nullable = false, columnDefinition = "YEAR")
    @Comment("1901 ~ 2155 년도")
    private @NotNull Integer sampleYear;

    // 문자 데이터
    /*
        문자 관련 데이터는 영문, 숫자를 기준으로 1 바이트 1 문자,
        그외 문자는 그 이상으로, 인커딩에 따라 달라집니다.
        UTF-8 에서 한글은 3 바이트, 특수문자는 4 바이트입니다.
     */
    @Column(name = "sample_char12", nullable = false, columnDefinition = "CHAR(12)")
    @Comment("고정 길이 문자열 (최대 255 Byte), CHAR 타입은 항상 지정된 길이만큼 공간을 차지하며, 실제 저장되는 문자열이 그보다 짧으면 빈 공간으로 패딩하여 저장합니다.")
    private @NotNull String sampleChar12;
    @Column(name = "sample_varchar12", nullable = false, columnDefinition = "VARCHAR(12)")
    @Comment("가변 길이 문자열 (최대 65,535 Byte), CHAR 과 달리 저장되는 데이터의 길이에 따라 실제 저장되는 공간이 달라집니다. CHAR 에 비해 저장 공간 활용에 강점이 있고 성능에 미비한 약점이 있습니다.")
    private @NotNull String sampleVarchar12;
    @Column(name = "sample_tiny_text", nullable = false, columnDefinition = "TINYTEXT")
    @Comment("가변 길이 문자열 최대 255 Byte")
    private @NotNull String sampleTinyText;
    @Column(name = "sample_text", nullable = false, columnDefinition = "TEXT")
    @Comment("가변 길이 문자열 최대 65,535 Byte")
    private @NotNull String sampleText;
    @Column(name = "sample_medium_text", nullable = false, columnDefinition = "MEDIUMTEXT")
    @Comment("가변 길이 문자열 최대 16,777,215 Byte")
    private @NotNull String sampleMediumText;
    @Column(name = "sample_long_text", nullable = false, columnDefinition = "LONGTEXT")
    @Comment("가변 길이 문자열 최대 4,294,967,295 Byte")
    private @NotNull String sampleLongText;

    // Bit 데이터
    @Column(name = "sample_one_bit", nullable = false, columnDefinition = "BIT(1)")
    @Comment("1 bit 값 (Boolean 으로 사용할 수 있습니다. (1 : 참, 0 : 거짓))")
    private @NotNull Boolean sampleOneBit;
    @Column(name = "sample_6_bit", nullable = false, columnDefinition = "BIT(6)")
    @Comment("n bit 값 (bit 사이즈에 따라 변수 사이즈를 맞춰 매핑)")
    private @NotNull Byte sample6Bit;

    // 컬렉션 데이터
    @Column(name = "sample_json", columnDefinition = "JSON")
    @Convert(converter = JsonMapConverter.class)
    @Comment("JSON 타입")
    private @Nullable Map<String, Object> sampleJson;
    @Column(name = "sample_enum_abc", nullable = false, columnDefinition = "ENUM('A', 'B', 'C')")
    @Enumerated(EnumType.STRING)
    @Comment("A, B, C 중 하나")
    private @NotNull EnumAbc sampleEnumAbc;
    @Column(name = "sample_set_abc", columnDefinition = "SET('A', 'B', 'C')")
    @Convert(converter = MySqlSetConverter.class)
    @Comment("A, B, C Set 컬렉션")
    private @Nullable Set<EnumAbc> sampleSetAbc;

    // 공간 데이터
    @Column(name = "sample_geometry", nullable = false, columnDefinition = "GEOMETRY")
    @Comment("GEOMETRY 타입(Point, Line, Polygon 데이터 중 어느것이라도 하나를 넣을 수 있습니다.)")
    private @NotNull Geometry sampleGeometry;
    @Column(name = "sample_point", nullable = false, columnDefinition = "POINT")
    @Comment("(X, Y) 공간 좌표")
    private @NotNull Point samplePoint;
    @Column(name = "sample_linestring", nullable = false, columnDefinition = "LINESTRING")
    @Comment("직선의 시퀀스")
    private @NotNull LineString sampleLinestring;
    @Column(name = "sample_polygon", nullable = false, columnDefinition = "POLYGON")
    @Comment("다각형")
    private @NotNull Polygon samplePolygon;

    // Binary 데이터
    @Column(name = "sample_binary2", nullable = false, columnDefinition = "BINARY(2)")
    @Comment("고정 길이 이진 데이터 (최대 65535 바이트), 암호화된 값, UUID, 고정 길이 해시값 등을 저장하는 역할")
    private @NotNull byte[] sampleBinary2;
    @Column(name = "sample_varbinary2", nullable = false, columnDefinition = "VARBINARY(2)")
    @Comment("가변 길이 이진 데이터 (최대 65535 바이트), 동적 크기의 바이너리 데이터, 이미지 등을 저장하는 역할")
    private @NotNull byte[] sampleVarbinary2;

    public enum EnumAbc {
        A, B, C
    }
}
