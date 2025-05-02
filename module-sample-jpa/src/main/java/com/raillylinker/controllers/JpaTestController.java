package com.raillylinker.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.raillylinker.jpa_beans.db1_main.entities.Db1_Template_DataTypeMappingTest;
import com.raillylinker.services.JpaTestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.jetbrains.annotations.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

@Tag(name = "/jpa-test APIs", description = "JPA 테스트 API 컨트롤러")
@Controller
@RequestMapping("/jpa-test")
@Validated
public class JpaTestController {
    public JpaTestController(
            @NotNull JpaTestService service
    ) {
        this.service = service;
    }

    // <멤버 변수 공간>
    private final @NotNull JpaTestService service;


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @Operation(
            summary = "DB Row 입력 테스트 API",
            description = "테스트 테이블에 Row 를 입력합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "정상 동작"
            )
    })
    @PostMapping(
            path = {"/row"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable InsertDataSampleOutputVo insertDataSample(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @RequestBody
            @NotNull InsertDataSampleInputVo inputVo
    ) {
        return service.insertDataSample(httpServletResponse, inputVo);
    }

    @Data
    public static class InsertDataSampleInputVo {
        @Schema(
                description = "글 본문",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "테스트 텍스트입니다."
        )
        @JsonProperty("content")
        private final @NotNull String content;
        @Schema(
                description = "원하는 날짜(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "2024_05_02_T_15_14_49_552_KST"
        )
        @JsonProperty("dateString")
        private final @NotNull String dateString;
    }

    @Data
    public static class InsertDataSampleOutputVo {
        @Schema(
                description = "글 고유번호",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "1234"
        )
        @JsonProperty("uid")
        private final @NotNull Long uid;
        @Schema(
                description = "글 본문",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "테스트 텍스트입니다."
        )
        @JsonProperty("content")
        private final @NotNull String content;
        @Schema(
                description = "자동 생성 숫자",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "21345"
        )
        @JsonProperty("randomNum")
        private final @NotNull Integer randomNum;
        @Schema(
                description = "테스트용 일시 데이터(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "2024_05_02_T_15_14_49_552_KST"
        )
        @JsonProperty("testDatetime")
        private final @NotNull String testDatetime;
        @Schema(
                description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "2024_05_02_T_15_14_49_552_KST"
        )
        @JsonProperty("createDate")
        private final @NotNull String createDate;
        @Schema(
                description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "2024_05_02_T_15_14_49_552_KST"
        )
        @JsonProperty("updateDate")
        private final @NotNull String updateDate;
        @Schema(
                description = "글 삭제일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z, Null 이면 /)",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "/"
        )
        @JsonProperty("deleteDate")
        private final @NotNull String deleteDate;
    }


    ////
    @Operation(
            summary = "DB Rows 삭제 테스트 API",
            description = "테스트 테이블의 모든 Row 를 모두 삭제합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "정상 동작"
            )
    })
    @DeleteMapping(
            path = {"/rows"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.ALL_VALUE
    )
    @ResponseBody
    public void deleteRowsSample(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(name = "deleteLogically", description = "논리적 삭제 여부", example = "true")
            @RequestParam(value = "deleteLogically")
            @NotNull Boolean deleteLogically
    ) {
        service.deleteRowsSample(httpServletResponse, deleteLogically);
    }


    ////
    @Operation(
            summary = "DB Row 삭제 테스트",
            description = "테스트 테이블의 Row 하나를 삭제합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 동작"),
            @ApiResponse(
                    responseCode = "204",
                    content = {@Content},
                    description = "Response Body 가 없습니다.<br>" +
                            "Response Headers 를 확인하세요.",
                    headers = {
                            @Header(
                                    name = "api-result-code",
                                    description = "(Response Code 반환 원인) - Required<br>" +
                                            "1 : index 에 해당하는 데이터가 데이터베이스에 존재하지 않습니다.",
                                    schema = @Schema(type = "string")
                            )
                    }
            )
    })
    @DeleteMapping(
            path = {"/row/{index}"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.ALL_VALUE
    )
    @ResponseBody
    public void deleteRowSample(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(name = "index", description = "글 인덱스", example = "1")
            @PathVariable(value = "index")
            @NotNull Long index,
            @Parameter(name = "deleteLogically", description = "논리적 삭제 여부", example = "true")
            @RequestParam(value = "deleteLogically")
            @NotNull Boolean deleteLogically
    ) {
        service.deleteRowSample(httpServletResponse, index, deleteLogically);
    }


    ////
    @Operation(
            summary = "DB Rows 조회 테스트",
            description = "테스트 테이블의 모든 Rows 를 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "정상 동작"
            )
    })
    @GetMapping(
            path = {"/rows"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable SelectRowsSampleOutputVo selectRowsSample(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse
    ) {
        return service.selectRowsSample(httpServletResponse);
    }

    @Data
    public static class SelectRowsSampleOutputVo {
        @Schema(description = "아이템 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("testEntityVoList")
        private final @NotNull List<TestEntityVo> testEntityVoList;
        @Schema(description = "논리적으로 제거된 아이템 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("logicalDeleteEntityVoList")
        private final @NotNull List<TestEntityVo> logicalDeleteEntityVoList;

        @Schema(description = "아이템")
        @Data
        public static class TestEntityVo {
            @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
            @JsonProperty("uid")
            private final @NotNull Long uid;
            @Schema(description = "글 본문", requiredMode = Schema.RequiredMode.REQUIRED, example = "테스트 텍스트입니다.")
            @JsonProperty("content")
            private final @NotNull String content;
            @Schema(description = "자동 생성 숫자", requiredMode = Schema.RequiredMode.REQUIRED, example = "21345")
            @JsonProperty("randomNum")
            private final @NotNull Integer randomNum;
            @Schema(description = "테스트용 일시 데이터(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("testDatetime")
            private final @NotNull String testDatetime;
            @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("createDate")
            private final @NotNull String createDate;
            @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("updateDate")
            private final @NotNull String updateDate;
            @Schema(description = "글 삭제일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z, Null 이면 /)", requiredMode = Schema.RequiredMode.REQUIRED, example = "/")
            @JsonProperty("deleteDate")
            private final @NotNull String deleteDate;
        }
    }


    ////
    @Operation(
            summary = "DB 테이블의 random_num 컬럼 근사치 기준으로 정렬한 리스트 조회 API",
            description = "테이블의 row 중 random_num 컬럼과 num 파라미터의 값의 근사치로 정렬한 리스트 반환"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "정상 동작"
            )
    })
    @GetMapping(
            path = {"/rows/order-by-random-num-nearest"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable SelectRowsOrderByRandomNumSampleOutputVo selectRowsOrderByRandomNumSample(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(name = "num", description = "근사값 정렬의 기준", example = "1")
            @RequestParam(value = "num")
            @NotNull Integer num
    ) {
        return service.selectRowsOrderByRandomNumSample(httpServletResponse, num);
    }

    @Data
    public static class SelectRowsOrderByRandomNumSampleOutputVo {
        @Schema(description = "아이템 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("testEntityVoList")
        private final @NotNull List<TestEntityVo> testEntityVoList;

        @Schema(description = "아이템")
        @Data
        public static class TestEntityVo {
            @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
            @JsonProperty("uid")
            private final @NotNull Long uid;
            @Schema(description = "글 본문", requiredMode = Schema.RequiredMode.REQUIRED, example = "테스트 텍스트입니다.")
            @JsonProperty("content")
            private final @NotNull String content;
            @Schema(description = "자동 생성 숫자", requiredMode = Schema.RequiredMode.REQUIRED, example = "21345")
            @JsonProperty("randomNum")
            private final @NotNull Integer randomNum;
            @Schema(description = "테스트용 일시 데이터(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("testDatetime")
            private final @NotNull String testDatetime;
            @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("createDate")
            private final @NotNull String createDate;
            @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("updateDate")
            private final @NotNull String updateDate;
            @Schema(description = "기준과의 절대거리", requiredMode = Schema.RequiredMode.REQUIRED, example = "34")
            @JsonProperty("distance")
            private final @NotNull Integer distance;
        }
    }


    ////
    @Operation(
            summary = "DB 테이블의 row_create_date 컬럼 근사치 기준으로 정렬한 리스트 조회 API",
            description = "테이블의 row 중 row_create_date 컬럼과 dateString 파라미터의 값의 근사치로 정렬한 리스트 반환"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    )
            }
    )
    @GetMapping(
            path = {"/rows/order-by-create-date-nearest"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable SelectRowsOrderByRowCreateDateSampleOutputVo selectRowsOrderByRowCreateDateSample(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(
                    name = "dateString",
                    description = "원하는 날짜(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)",
                    example = "2024_05_02_T_15_14_49_552_KST"
            )
            @RequestParam(value = "dateString")
            @NotNull String dateString
    ) {
        return service.selectRowsOrderByRowCreateDateSample(httpServletResponse, dateString);
    }

    @Data
    public static class SelectRowsOrderByRowCreateDateSampleOutputVo {
        @JsonProperty("testEntityVoList")
        @Schema(description = "아이템 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
        private final @NotNull List<TestEntityVo> testEntityVoList;

        @Schema(description = "아이템")
        @Data
        public static class TestEntityVo {
            @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
            @JsonProperty("uid")
            private final @NotNull Long uid;
            @Schema(description = "글 본문", requiredMode = Schema.RequiredMode.REQUIRED, example = "테스트 텍스트입니다.")
            @JsonProperty("content")
            private final @NotNull String content;
            @Schema(description = "자동 생성 숫자", requiredMode = Schema.RequiredMode.REQUIRED, example = "21345")
            @JsonProperty("randomNum")
            private final @NotNull Integer randomNum;
            @Schema(description = "테스트용 일시 데이터(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("testDatetime")
            private final @NotNull String testDatetime;
            @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("createDate")
            private final @NotNull String createDate;
            @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("updateDate")
            private final @NotNull String updateDate;
            @Schema(description = "기준과의 절대차이(마이크로 초)", requiredMode = Schema.RequiredMode.REQUIRED, example = "34")
            @JsonProperty("timeDiffMicroSec")
            private final @NotNull Long timeDiffMicroSec;
        }
    }


    ////
    @Operation(
            summary = "DB Rows 조회 테스트 (페이징)",
            description = "테스트 테이블의 Rows 를 페이징하여 반환합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    )
            }
    )
    @GetMapping(
            path = {"/rows/paging"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable SelectRowsPageSampleOutputVo selectRowsPageSample(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(name = "page", description = "원하는 페이지(1 부터 시작)", example = "1")
            @RequestParam(value = "page")
            @NotNull Integer page,
            @Parameter(name = "pageElementsCount", description = "페이지 아이템 개수", example = "10")
            @RequestParam(value = "pageElementsCount")
            @NotNull Integer pageElementsCount
    ) {
        return service.selectRowsPageSample(httpServletResponse, page, pageElementsCount);
    }

    @Data
    public static class SelectRowsPageSampleOutputVo {
        @Schema(description = "아이템 전체 개수", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
        @JsonProperty("totalElements")
        private final @NotNull Long totalElements;
        @Schema(description = "아이템 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("testEntityVoList")
        private final @NotNull List<TestEntityVo> testEntityVoList;

        @Schema(description = "아이템")
        @Data
        public static class TestEntityVo {
            @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
            @JsonProperty("uid")
            private final @NotNull Long uid;
            @Schema(description = "글 본문", requiredMode = Schema.RequiredMode.REQUIRED, example = "테스트 텍스트입니다.")
            @JsonProperty("content")
            private final @NotNull String content;
            @Schema(description = "자동 생성 숫자", requiredMode = Schema.RequiredMode.REQUIRED, example = "23456")
            @JsonProperty("randomNum")
            private final @NotNull Integer randomNum;
            @Schema(description = "테스트용 일시 데이터(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("testDatetime")
            private final @NotNull String testDatetime;
            @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("createDate")
            private final @NotNull String createDate;
            @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("updateDate")
            private final @NotNull String updateDate;
        }
    }


    ////
    @Operation(
            summary = "DB Rows 조회 테스트 (네이티브 쿼리 페이징)",
            description = "테스트 테이블의 Rows 를 네이티브 쿼리로 페이징하여 반환합니다.<br>" +
                    "num 을 기준으로 근사치 정렬도 수행합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    )
            }
    )
    @GetMapping(
            path = {"/rows/native-paging"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable SelectRowsNativeQueryPageSampleOutputVo selectRowsNativeQueryPageSample(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(name = "page", description = "원하는 페이지(1 부터 시작)", example = "1")
            @RequestParam(value = "page")
            @NotNull Integer page,
            @Parameter(name = "pageElementsCount", description = "페이지 아이템 개수", example = "10")
            @RequestParam(value = "pageElementsCount")
            @NotNull Integer pageElementsCount,
            @Parameter(name = "num", description = "근사값의 기준", example = "1")
            @RequestParam(value = "num")
            @NotNull Integer num
    ) {
        return service.selectRowsNativeQueryPageSample(httpServletResponse, page, pageElementsCount, num);
    }

    @Data
    public static class SelectRowsNativeQueryPageSampleOutputVo {
        @Schema(description = "아이템 전체 개수", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
        @JsonProperty("totalElements")
        private final @NotNull Long totalElements;
        @Schema(description = "아이템 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("testEntityVoList")
        private final @NotNull List<TestEntityVo> testEntityVoList;

        @Schema(description = "아이템")
        @Data
        public static class TestEntityVo {
            @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
            @JsonProperty("uid")
            private final @NotNull Long uid;
            @Schema(description = "글 본문", requiredMode = Schema.RequiredMode.REQUIRED, example = "테스트 텍스트입니다.")
            @JsonProperty("content")
            private final @NotNull String content;
            @Schema(description = "자동 생성 숫자", requiredMode = Schema.RequiredMode.REQUIRED, example = "21345")
            @JsonProperty("randomNum")
            private final @NotNull Integer randomNum;
            @Schema(description = "테스트용 일시 데이터(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("testDatetime")
            private final @NotNull String testDatetime;
            @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("createDate")
            private final @NotNull String createDate;
            @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("updateDate")
            private final @NotNull String updateDate;
            @Schema(description = "기준과의 절대거리", requiredMode = Schema.RequiredMode.REQUIRED, example = "34")
            @JsonProperty("distance")
            private final @NotNull Integer distance;
        }
    }


    ////
    @Operation(
            summary = "DB Row 수정 테스트",
            description = "테스트 테이블의 Row 하나를 수정합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            content = {@Content},
                            description = "Response Body 가 없습니다.<br>" +
                                    "Response Headers 를 확인하세요.",
                            headers = {
                                    @Header(
                                            name = "api-result-code",
                                            description = "(Response Code 반환 원인) - Required<br>" +
                                                    "1 : testTableUid 에 해당하는 정보가 데이터베이스에 존재하지 않습니다.",
                                            schema = @Schema(type = "string")
                                    )
                            }
                    )
            }
    )
    @PatchMapping(
            path = {"/row/{testTableUid}"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable UpdateRowSampleOutputVo updateRowSample(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(name = "testTableUid", description = "test 테이블의 uid", example = "1")
            @PathVariable(value = "testTableUid")
            @NotNull Long testTableUid,
            @RequestBody
            @NotNull UpdateRowSampleInputVo inputVo
    ) {
        return service.updateRowSample(httpServletResponse, testTableUid, inputVo);
    }

    @Data
    public static class UpdateRowSampleInputVo {
        @Schema(description = "글 본문", requiredMode = Schema.RequiredMode.REQUIRED, example = "테스트 텍스트 수정글입니다.")
        @JsonProperty("content")
        private final @NotNull String content;
        @Schema(description = "원하는 날짜(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
        @JsonProperty("dateString")
        private final @NotNull String dateString;
    }

    @Data
    public static class UpdateRowSampleOutputVo {
        @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
        @JsonProperty("uid")
        private final @NotNull Long uid;
        @Schema(description = "글 본문", requiredMode = Schema.RequiredMode.REQUIRED, example = "테스트 텍스트입니다.")
        @JsonProperty("content")
        private final @NotNull String content;
        @Schema(description = "자동 생성 숫자", requiredMode = Schema.RequiredMode.REQUIRED, example = "21345")
        @JsonProperty("randomNum")
        private final @NotNull Integer randomNum;
        @Schema(description = "테스트용 일시 데이터(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
        @JsonProperty("testDatetime")
        private final @NotNull String testDatetime;
        @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
        @JsonProperty("createDate")
        private final @NotNull String createDate;
        @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
        @JsonProperty("updateDate")
        private final @NotNull String updateDate;
    }


    ////
    @Operation(
            summary = "DB Row 수정 테스트 (네이티브 쿼리)",
            description = "테스트 테이블의 Row 하나를 네이티브 쿼리로 수정합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            content = {@Content},
                            description = "Response Body 가 없습니다.<br>" +
                                    "Response Headers 를 확인하세요.",
                            headers = {
                                    @Header(
                                            name = "api-result-code",
                                            description = "(Response Code 반환 원인) - Required<br>" +
                                                    "1 : testTableUid 에 해당하는 정보가 데이터베이스에 존재하지 않습니다.",
                                            schema = @Schema(type = "string")
                                    )
                            }
                    )
            }
    )
    @PatchMapping(
            path = {"/row/{testTableUid}/native-query"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public void updateRowNativeQuerySample(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(name = "testTableUid", description = "test 테이블의 uid", example = "1")
            @PathVariable(value = "testTableUid")
            @NotNull Long testTableUid,
            @RequestBody
            @NotNull UpdateRowNativeQuerySampleInputVo inputVo
    ) {
        service.updateRowNativeQuerySample(httpServletResponse, testTableUid, inputVo);
    }

    @Data
    public static class UpdateRowNativeQuerySampleInputVo {
        @Schema(description = "글 본문", requiredMode = Schema.RequiredMode.REQUIRED, example = "테스트 텍스트 수정글입니다.")
        @JsonProperty("content")
        private final @NotNull String content;
        @Schema(description = "원하는 날짜(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
        @JsonProperty("dateString")
        private final @NotNull String dateString;
    }


    ////
    @Operation(
            summary = "DB 정보 검색 테스트",
            description = "글 본문 내용중 searchKeyword 가 포함된 rows 를 검색하여 반환합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    )
            }
    )
    @GetMapping(
            path = {"/search-content"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable SelectRowWhereSearchingKeywordSampleOutputVo selectRowWhereSearchingKeywordSample(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(name = "page", description = "원하는 페이지(1 부터 시작)", example = "1")
            @RequestParam(value = "page")
            @NotNull Integer page,
            @Parameter(name = "pageElementsCount", description = "페이지 아이템 개수", example = "10")
            @RequestParam(value = "pageElementsCount")
            @NotNull Integer pageElementsCount,
            @Parameter(name = "searchKeyword", description = "검색어", example = "테스트")
            @RequestParam(value = "searchKeyword")
            @NotNull String searchKeyword
    ) {
        return service.selectRowWhereSearchingKeywordSample(httpServletResponse, page, pageElementsCount, searchKeyword);
    }

    @Data
    public static class SelectRowWhereSearchingKeywordSampleOutputVo {
        @Schema(description = "아이템 전체 개수", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
        @JsonProperty("totalElements")
        private final @NotNull Long totalElements;
        @Schema(description = "아이템 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("testEntityVoList")
        private final @NotNull List<TestEntityVo> testEntityVoList;

        @Schema(description = "아이템")
        @Data
        public static class TestEntityVo {
            @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
            @JsonProperty("uid")
            private final @NotNull Long uid;
            @Schema(description = "글 본문", requiredMode = Schema.RequiredMode.REQUIRED, example = "테스트 텍스트입니다.")
            @JsonProperty("content")
            private final @NotNull String content;
            @Schema(description = "자동 생성 숫자", requiredMode = Schema.RequiredMode.REQUIRED, example = "21345")
            @JsonProperty("randomNum")
            private final @NotNull Integer randomNum;
            @Schema(description = "테스트용 일시 데이터(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("testDatetime")
            private final @NotNull String testDatetime;
            @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("createDate")
            private final @NotNull String createDate;
            @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("updateDate")
            private final @NotNull String updateDate;
        }
    }


    ////
    @Operation(
            summary = "트랜젝션 동작 테스트",
            description = "정보 입력 후 Exception 이 발생했을 때 롤백되어 데이터가 저장되지 않는지를 테스트하는 API"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    )
            }
    )
    @PostMapping(
            path = {"/transaction-rollback-sample"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.ALL_VALUE
    )
    @ResponseBody
    public void transactionTest(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse
    ) {
        service.transactionTest(httpServletResponse);
    }


    ////
    @Operation(
            summary = "트랜젝션 비동작 테스트",
            description = "트랜젝션 처리를 하지 않았을 때, DB 정보 입력 후 Exception 이 발생 했을 때 의 테스트 API"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    )
            }
    )
    @PostMapping(
            path = {"/no-transaction-exception-sample"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.ALL_VALUE
    )
    @ResponseBody
    public void nonTransactionTest(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse
    ) {
        service.nonTransactionTest(httpServletResponse);
    }


    ////
    @Operation(
            summary = "트랜젝션 비동작 테스트(try-catch)",
            description = "에러 발생문이 try-catch 문 안에 있을 때, DB 정보 입력 후 Exception 이 발생 해도 트랜젝션이 동작하지 않는지에 대한 테스트 API"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    )
            }
    )
    @PostMapping(
            path = {"/try-catch-no-transaction-exception-sample"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.ALL_VALUE
    )
    @ResponseBody
    public void tryCatchNonTransactionTest(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse
    ) {
        service.tryCatchNonTransactionTest(httpServletResponse);
    }


    ////
    @Operation(
            summary = "DB Rows 조회 테스트 (중복 없는 네이티브 쿼리 페이징)",
            description = "테스트 테이블의 Rows 를 네이티브 쿼리로 중복없이 페이징하여 반환합니다.<br>" +
                    "num 을 기준으로 근사치 정렬도 수행합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    )
            }
    )
    @GetMapping(
            path = {"/rows/native-paging-no-duplication"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable SelectRowsNoDuplicatePagingSampleOutputVo selectRowsNoDuplicatePagingSample(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(name = "lastItemUid", description = "이전 페이지에서 받은 마지막 아이템의 Uid (첫 요청이면 null)", example = "1")
            @RequestParam(value = "lastItemUid", required = false)
            @Nullable Long lastItemUid,
            @Parameter(name = "pageElementsCount", description = "페이지 아이템 개수", example = "10")
            @RequestParam(value = "pageElementsCount")
            @NotNull Integer pageElementsCount
    ) {
        return service.selectRowsNoDuplicatePagingSample(httpServletResponse, lastItemUid, pageElementsCount);
    }

    @Data
    public static class SelectRowsNoDuplicatePagingSampleOutputVo {
        @Schema(description = "아이템 전체 개수", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
        @JsonProperty("totalElements")
        private final @NotNull Long totalElements;
        @Schema(description = "아이템 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("testEntityVoList")
        private final @NotNull List<TestEntityVo> testEntityVoList;

        @Schema(description = "아이템")
        @Data
        public static class TestEntityVo {
            @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
            @JsonProperty("uid")
            private final @NotNull Long uid;
            @Schema(description = "글 본문", requiredMode = Schema.RequiredMode.REQUIRED, example = "테스트 텍스트입니다.")
            @JsonProperty("content")
            private final @NotNull String content;
            @Schema(description = "자동 생성 숫자", requiredMode = Schema.RequiredMode.REQUIRED, example = "21345")
            @JsonProperty("randomNum")
            private final @NotNull Integer randomNum;
            @Schema(description = "테스트용 일시 데이터(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("testDatetime")
            private final @NotNull String testDatetime;
            @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("createDate")
            private final @NotNull String createDate;
            @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("updateDate")
            private final @NotNull String updateDate;
        }
    }


    ////
    @Operation(
            summary = "DB Rows 조회 테스트 (카운팅)",
            description = "테스트 테이블의 Rows 를 카운팅하여 반환합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    )
            }
    )
    @GetMapping(
            path = {"/rows/counting"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable SelectRowsCountSampleOutputVo selectRowsCountSample(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse
    ) {
        return service.selectRowsCountSample(httpServletResponse);
    }

    @Data
    public static class SelectRowsCountSampleOutputVo {
        @Schema(description = "아이템 전체 개수", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
        @JsonProperty("totalElements")
        private final @NotNull Long totalElements;
    }


    ////
    @Operation(
            summary = "DB Rows 조회 테스트 (네이티브 카운팅)",
            description = "테스트 테이블의 Rows 를 네이티브 쿼리로 카운팅하여 반환합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    )
            }
    )
    @GetMapping(
            path = {"/rows/native-counting"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable SelectRowsCountByNativeQuerySampleOutputVo selectRowsCountByNativeQuerySample(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse
    ) {
        return service.selectRowsCountByNativeQuerySample(httpServletResponse);
    }

    @Data
    public static class SelectRowsCountByNativeQuerySampleOutputVo {
        @Schema(description = "아이템 전체 개수", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
        @JsonProperty("totalElements")
        private final @NotNull Long totalElements;
    }


    ////
    @Operation(
            summary = "DB Row 조회 테스트 (네이티브)",
            description = "테스트 테이블의 Row 하나를 네이티브 쿼리로 반환합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            content = {@Content},
                            description = "Response Body 가 없습니다.<br>" +
                                    "Response Headers 를 확인하세요.",
                            headers = {
                                    @Header(
                                            name = "api-result-code",
                                            description = "(Response Code 반환 원인) - Required<br>" +
                                                    "1 : testTableUid 에 해당하는 정보가 데이터베이스에 존재하지 않습니다.",
                                            schema = @Schema(type = "string")
                                    )
                            }
                    )
            }
    )
    @GetMapping(
            path = {"/row/native/{testTableUid}"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable SelectRowByNativeQuerySampleOutputVo selectRowByNativeQuerySample(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(name = "testTableUid", description = "test 테이블의 uid", example = "1")
            @PathVariable(value = "testTableUid")
            @NotNull Long testTableUid
    ) {
        return service.selectRowByNativeQuerySample(httpServletResponse, testTableUid);
    }

    @Data
    public static class SelectRowByNativeQuerySampleOutputVo {
        @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
        @JsonProperty("uid")
        private final @NotNull Long uid;
        @Schema(description = "글 본문", requiredMode = Schema.RequiredMode.REQUIRED, example = "테스트 텍스트입니다.")
        @JsonProperty("content")
        private final @NotNull String content;
        @Schema(description = "자동 생성 숫자", requiredMode = Schema.RequiredMode.REQUIRED, example = "21345")
        @JsonProperty("randomNum")
        private final @NotNull Integer randomNum;
        @Schema(description = "테스트용 일시 데이터(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
        @JsonProperty("testDatetime")
        private final @NotNull String testDatetime;
        @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
        @JsonProperty("createDate")
        private final @NotNull String createDate;
        @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
        @JsonProperty("updateDate")
        private final @NotNull String updateDate;
    }


    ////
    @Operation(
            summary = "유니크 테스트 테이블 Row 입력 API",
            description = "유니크 테스트 테이블에 Row 를 입력합니다.<br>" +
                    "논리적 삭제를 적용한 본 테이블에서 유니크 값은, 유니크 값 컬럼과 행 삭제일 데이터와의 혼합입니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    )
            }
    )
    @PostMapping(
            path = {"/unique-test-table"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable InsertUniqueTestTableRowSampleOutputVo insertUniqueTestTableRowSample(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @RequestBody
            @NotNull InsertUniqueTestTableRowSampleInputVo inputVo
    ) {
        return service.insertUniqueTestTableRowSample(httpServletResponse, inputVo);
    }

    @Data
    public static class InsertUniqueTestTableRowSampleInputVo {
        @Schema(description = "유니크 값", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("uniqueValue")
        private final @NotNull Integer uniqueValue;
    }

    @Data
    public static class InsertUniqueTestTableRowSampleOutputVo {
        @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
        @JsonProperty("uid")
        private final @NotNull Long uid;
        @Schema(description = "유니크 값", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("uniqueValue")
        private final @NotNull Integer uniqueValue;
        @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
        @JsonProperty("createDate")
        private final @NotNull String createDate;
        @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
        @JsonProperty("updateDate")
        private final @NotNull String updateDate;
        @Schema(description = "글 삭제일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z, Null 이면 /)", requiredMode = Schema.RequiredMode.REQUIRED, example = "/")
        @JsonProperty("deleteDate")
        private final @NotNull String deleteDate;
    }


    ////
    @Operation(
            summary = "유니크 테스트 테이블 Rows 조회 테스트",
            description = "유니크 테스트 테이블의 모든 Rows 를 반환합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    )
            }
    )
    @GetMapping(
            path = {"/unique-test-table/all"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable SelectUniqueTestTableRowsSampleOutputVo selectUniqueTestTableRowsSample(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse
    ) {
        return service.selectUniqueTestTableRowsSample(httpServletResponse);
    }

    @Data
    public static class SelectUniqueTestTableRowsSampleOutputVo {
        @Schema(description = "아이템 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("testEntityVoList")
        private final @NotNull List<TestEntityVo> testEntityVoList;

        @Schema(description = "논리적으로 제거된 아이템 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("logicalDeleteEntityVoList")
        private final @NotNull List<TestEntityVo> logicalDeleteEntityVoList;

        @Data
        public static class TestEntityVo {
            @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
            @JsonProperty("uid")
            private final @NotNull Long uid;
            @Schema(description = "유니크 값", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
            @JsonProperty("uniqueValue")
            private final @NotNull Integer uniqueValue;
            @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("createDate")
            private final @NotNull String createDate;
            @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("updateDate")
            private final @NotNull String updateDate;
            @Schema(description = "글 삭제일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z, Null 이면 /)", requiredMode = Schema.RequiredMode.REQUIRED, example = "/")
            @JsonProperty("deleteDate")
            private final @NotNull String deleteDate;
        }
    }


    ////
    @Operation(
            summary = "유니크 테스트 테이블 Row 수정 테스트",
            description = "유니크 테스트 테이블의 Row 하나를 수정합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            content = {@Content},
                            description = "Response Body 가 없습니다.<br>" +
                                    "Response Headers 를 확인하세요.",
                            headers = {
                                    @Header(
                                            name = "api-result-code",
                                            description = "(Response Code 반환 원인) - Required<br>" +
                                                    "1 : uniqueTestTableUid 에 해당하는 정보가 데이터베이스에 존재하지 않습니다.<br>" +
                                                    "2 : uniqueValue 와 일치하는 정보가 이미 데이터베이스에 존재합니다.",
                                            schema = @Schema(type = "string")
                                    )
                            }
                    )
            }
    )
    @PatchMapping(
            path = {"/unique-test-table/{uniqueTestTableUid}"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable UpdateUniqueTestTableRowSampleOutputVo updateUniqueTestTableRowSample(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(name = "uniqueTestTableUid", description = "unique test 테이블의 uid", example = "1")
            @PathVariable(value = "uniqueTestTableUid")
            @NotNull Long uniqueTestTableUid,
            @RequestBody
            @NotNull UpdateUniqueTestTableRowSampleInputVo inputVo
    ) {
        return service.updateUniqueTestTableRowSample(httpServletResponse, uniqueTestTableUid, inputVo);
    }

    @Data
    public static class UpdateUniqueTestTableRowSampleInputVo {
        @Schema(description = "유니크 값", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("uniqueValue")
        private final @NotNull Integer uniqueValue;
    }

    @Data
    public static class UpdateUniqueTestTableRowSampleOutputVo {
        @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
        @JsonProperty("uid")
        private final @NotNull Long uid;
        @Schema(description = "유니크 값", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("uniqueValue")
        private final @NotNull Integer uniqueValue;
        @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
        @JsonProperty("createDate")
        private final @NotNull String createDate;
        @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
        @JsonProperty("updateDate")
        private final @NotNull String updateDate;
    }


    ////
    @Operation(
            summary = "유니크 테스트 테이블 Row 삭제 테스트",
            description = "유니크 테스트 테이블의 Row 하나를 삭제합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            content = {@Content},
                            description = "Response Body 가 없습니다.<br>" +
                                    "Response Headers 를 확인하세요.",
                            headers = {
                                    @Header(
                                            name = "api-result-code",
                                            description = "(Response Code 반환 원인) - Required<br>" +
                                                    "1 : index 에 해당하는 데이터가 데이터베이스에 존재하지 않습니다.",
                                            schema = @Schema(type = "string")
                                    )
                            }
                    )
            }
    )
    @DeleteMapping(
            path = {"/unique-test-table/{index}"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.ALL_VALUE
    )
    @ResponseBody
    public void deleteUniqueTestTableRowSample(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(name = "index", description = "글 인덱스", example = "1")
            @PathVariable(value = "index")
            @NotNull Long index
    ) {
        service.deleteUniqueTestTableRowSample(httpServletResponse, index);
    }


    ////
    @Operation(
            summary = "외래키 부모 테이블 Row 입력 API",
            description = "외래키 부모 테이블에 Row 를 입력합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    )
            }
    )
    @PostMapping(
            path = {"/fk-parent"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable InsertFkParentRowSampleOutputVo insertFkParentRowSample(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @RequestBody
            @NotNull InsertFkParentRowSampleInputVo inputVo
    ) {
        return service.insertFkParentRowSample(httpServletResponse, inputVo);
    }

    @Data
    public static class InsertFkParentRowSampleInputVo {
        @Schema(description = "외래키 테이블 부모 이름", requiredMode = Schema.RequiredMode.REQUIRED, example = "홍길동")
        @JsonProperty("fkParentName")
        private final @NotNull String fkParentName;
    }

    @Data
    public static class InsertFkParentRowSampleOutputVo {
        @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
        @JsonProperty("uid")
        private final @NotNull Long uid;
        @Schema(description = "외래키 테이블 부모 이름", requiredMode = Schema.RequiredMode.REQUIRED, example = "홍길동")
        @JsonProperty("fkParentName")
        private final @NotNull String fkParentName;
        @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
        @JsonProperty("createDate")
        private final @NotNull String createDate;
        @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
        @JsonProperty("updateDate")
        private final @NotNull String updateDate;
    }


    ////
    @Operation(
            summary = "외래키 부모 테이블 아래에 자식 테이블의 Row 입력 API",
            description = "외래키 부모 테이블의 아래에 자식 테이블의 Row 를 입력합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            content = {@Content},
                            description = "Response Body 가 없습니다.<br>" +
                                    "Response Headers 를 확인하세요.",
                            headers = {
                                    @Header(
                                            name = "api-result-code",
                                            description = "(Response Code 반환 원인) - Required<br>" +
                                                    "1 : parentUid 에 해당하는 데이터가 존재하지 않습니다.",
                                            schema = @Schema(type = "string")
                                    )
                            }
                    )
            }
    )
    @PostMapping(
            path = {"/fk-parent/{parentUid}"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable InsertFkChildRowSampleOutputVo insertFkChildRowSample(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(name = "parentUid", description = "외래키 부모 테이블 고유번호", example = "1")
            @PathVariable(value = "parentUid")
            @NotNull Long parentUid,
            @RequestBody
            @NotNull InsertFkChildRowSampleInputVo inputVo) {
        return service.insertFkChildRowSample(httpServletResponse, parentUid, inputVo);
    }

    @Data
    public static class InsertFkChildRowSampleInputVo {
        @Schema(description = "외래키 테이블 자식 이름", requiredMode = Schema.RequiredMode.REQUIRED, example = "홍길동")
        @JsonProperty("fkChildName")
        private final @NotNull String fkChildName;
    }

    @Data
    public static class InsertFkChildRowSampleOutputVo {
        @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
        @JsonProperty("uid")
        private final @NotNull Long uid;
        @Schema(description = "외래키 테이블 부모 이름", requiredMode = Schema.RequiredMode.REQUIRED, example = "홍길동")
        @JsonProperty("fkParentName")
        private final @NotNull String fkParentName;
        @Schema(description = "외래키 테이블 자식 이름", requiredMode = Schema.RequiredMode.REQUIRED, example = "홍길동")
        @JsonProperty("fkChildName")
        private final @NotNull String fkChildName;
        @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
        @JsonProperty("createDate")
        private final @NotNull String createDate;
        @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
        @JsonProperty("updateDate")
        private final @NotNull String updateDate;
    }


    ////
    @Operation(
            summary = "외래키 관련 테이블 Rows 조회 테스트",
            description = "외래키 관련 테이블의 모든 Rows 를 반환합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    )
            }
    )
    @GetMapping(
            path = {"/fk-table/all"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable SelectFkTestTableRowsSampleOutputVo selectFkTestTableRowsSample(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse
    ) {
        return service.selectFkTestTableRowsSample(httpServletResponse);
    }

    @Data
    public static class SelectFkTestTableRowsSampleOutputVo {
        @Schema(description = "부모 아이템 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("parentEntityVoList")
        private final @NotNull List<ParentEntityVo> parentEntityVoList;

        @Schema(description = "부모 아이템")
        @Data
        public static class ParentEntityVo {
            @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
            @JsonProperty("uid")
            private final @NotNull Long uid;
            @Schema(description = "부모 테이블 이름", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
            @JsonProperty("parentName")
            private final @NotNull String parentName;
            @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("createDate")
            private final @NotNull String createDate;
            @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("updateDate")
            private final @NotNull String updateDate;
            @Schema(description = "부모 테이블에 속하는 자식 테이블 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
            @JsonProperty("childEntityList")
            private final @NotNull List<ChildEntityVo> childEntityList;

            @Schema(description = "자식 아이템")
            @Data
            public static class ChildEntityVo {
                @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
                @JsonProperty("uid")
                private final @NotNull Long uid;
                @Schema(description = "자식 테이블 이름", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
                @JsonProperty("childName")
                private final @NotNull String childName;
                @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
                @JsonProperty("createDate")
                private final @NotNull String createDate;
                @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
                @JsonProperty("updateDate")
                private final @NotNull String updateDate;
            }
        }
    }


    ////
    @Operation(
            summary = "외래키 관련 테이블 Rows 조회 테스트(Native Join)",
            description = "외래키 관련 테이블의 모든 Rows 를 Native Query 로 Join 하여 반환합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    )
            }
    )
    @GetMapping(
            path = {"/fk-table-native-join"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable SelectFkTestTableRowsByNativeQuerySampleDot1OutputVo selectFkTestTableRowsByNativeQuerySample(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse
    ) {
        return service.selectFkTestTableRowsByNativeQuerySample(httpServletResponse);
    }

    @Data
    public static class SelectFkTestTableRowsByNativeQuerySampleDot1OutputVo {
        @Schema(description = "자식 아이템 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("childEntityVoList")
        private final @NotNull List<ChildEntityVo> childEntityVoList;

        @Schema(description = "자식 아이템")
        @Data
        public static class ChildEntityVo {
            @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
            @JsonProperty("uid")
            private final @NotNull Long uid;
            @Schema(description = "자식 테이블 이름", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
            @JsonProperty("childName")
            private final @NotNull String childName;
            @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("createDate")
            private final @NotNull String createDate;
            @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("updateDate")
            private final @NotNull String updateDate;
            @Schema(description = "부모 테이블 고유번호", requiredMode = Schema.RequiredMode.REQUIRED)
            @JsonProperty("parentUid")
            private final @NotNull Long parentUid;
            @Schema(description = "부모 테이블 이름", requiredMode = Schema.RequiredMode.REQUIRED)
            @JsonProperty("parentName")
            private final @NotNull String parentName;
        }
    }


    ////
    @Operation(
            summary = "Native Query 반환값 테스트",
            description = "Native Query Select 문에서 IF, CASE 등의 문구에서 반환되는 값들을 받는 예시"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    )
            }
    )
    @GetMapping(
            path = {"/native-query-return"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable GetNativeQueryReturnValueTestOutputVo getNativeQueryReturnValueTest(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(name = "inputVal", description = "Native Query 비교문에 사용되는 파라미터", example = "true")
            @RequestParam(value = "inputVal")
            @NotNull Boolean inputVal
    ) {
        return service.getNativeQueryReturnValueTest(httpServletResponse, inputVal);
    }

    @Data
    public static class GetNativeQueryReturnValueTestOutputVo {
        @Schema(description = "Select 문에서 직접적으로 true 를 반환한 예시", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
        @JsonProperty("normalBoolValue")
        private final @NotNull Boolean normalBoolValue;
        @Schema(description = "Select 문에서 (1=1) 과 같이 비교한 결과를 반환한 예시", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
        @JsonProperty("funcBoolValue")
        private final @NotNull Boolean funcBoolValue;
        @Schema(description = "Select 문에서 if 문의 결과를 반환한 예시", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
        @JsonProperty("ifBoolValue")
        private final @NotNull Boolean ifBoolValue;
        @Schema(description = "Select 문에서 case 문의 결과를 반환한 예시", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
        @JsonProperty("caseBoolValue")
        private final @NotNull Boolean caseBoolValue;
        @Schema(description = "Select 문에서 테이블의 Boolean 컬럼의 결과를 반환한 예시", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
        @JsonProperty("tableColumnBoolValue")
        private final @NotNull Boolean tableColumnBoolValue;
    }


    ////
    @Operation(
            summary = "SQL Injection 테스트",
            description = "각 상황에서 SQL Injection 공격이 유효한지 확인하기 위한 테스트<br>" +
                    "SELECT 문에서, WHERE 에, content = :searchKeyword 를 하여,<br>" +
                    " 인젝션이 일어나는 키워드를 입력시 인젝션이 먹히는지를 확인할 것입니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    )
            }
    )
    @GetMapping(
            path = {"/sql-injection-test"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable SqlInjectionTestOutputVo sqlInjectionTest(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(name = "searchKeyword", description = "Select 문 검색에 사용되는 키워드", example = "test OR 1 = 1")
            @RequestParam(value = "searchKeyword")
            @NotNull String searchKeyword
    ) {
        return service.sqlInjectionTest(httpServletResponse, searchKeyword);
    }

    @Data
    public static class SqlInjectionTestOutputVo {
        @Schema(description = "JpaRepository 로 조회했을 때의 아이템 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("jpaRepositoryResultList")
        private final @NotNull List<TestEntityVo> jpaRepositoryResultList;

        @Schema(description = "JPQL 로 조회했을 때의 아이템 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("jpqlResultList")
        private final @NotNull List<TestEntityVo> jpqlResultList;

        @Schema(description = "Native Query 로 조회했을 때의 아이템 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("nativeQueryResultList")
        private final @NotNull List<TestEntityVo> nativeQueryResultList;

        @Schema(description = "아이템")
        @Data
        public static class TestEntityVo {
            @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
            @JsonProperty("uid")
            private final @NotNull Long uid;
            @Schema(description = "글 본문", requiredMode = Schema.RequiredMode.REQUIRED, example = "테스트 텍스트입니다.")
            @JsonProperty("content")
            private final @NotNull String content;
            @Schema(description = "자동 생성 숫자", requiredMode = Schema.RequiredMode.REQUIRED, example = "21345")
            @JsonProperty("randomNum")
            private final @NotNull Integer randomNum;
            @Schema(description = "테스트용 일시 데이터(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("testDatetime")
            private final @NotNull String testDatetime;
            @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("createDate")
            private final @NotNull String createDate;
            @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("updateDate")
            private final @NotNull String updateDate;
        }
    }


    ////
    @Operation(
            summary = "외래키 관련 테이블 Rows 조회 (네이티브 쿼리, 부모 테이블을 자식 테이블의 가장 최근 데이터만 Join)",
            description = "외래키 관련 테이블의 모든 Rows 를 반환합니다.<br>" +
                    "부모 테이블을 Native Query 로 조회할 때, 부모 테이블을 가리키는 자식 테이블들 중 가장 최신 데이터만 Join 하는 예시입니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    )
            }
    )
    @GetMapping(
            path = {"/fk-table-latest-join"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable SelectFkTableRowsWithLatestChildSampleOutputVo selectFkTableRowsWithLatestChildSample(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse
    ) {
        return service.selectFkTableRowsWithLatestChildSample(httpServletResponse);
    }

    @Data
    public static class SelectFkTableRowsWithLatestChildSampleOutputVo {
        @Schema(description = "부모 아이템 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("parentEntityVoList")
        private final @NotNull List<ParentEntityVo> parentEntityVoList;

        @Schema(description = "부모 아이템")
        @Data
        public static class ParentEntityVo {
            @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
            @JsonProperty("uid")
            private final @NotNull Long uid;
            @Schema(description = "부모 테이블 이름", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
            @JsonProperty("parentName")
            private final @NotNull String parentName;
            @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("createDate")
            private final @NotNull String createDate;
            @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("updateDate")
            private final @NotNull String updateDate;
            @Schema(description = "부모 테이블에 속하는 자식 테이블들 중 가장 최신 데이터")
            @JsonProperty("latestChildEntity")
            private final @Nullable ChildEntityVo latestChildEntity;

            @Schema(description = "자식 아이템")
            @Data
            public static class ChildEntityVo {
                @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
                @JsonProperty("uid")
                private final @NotNull Long uid;
                @Schema(description = "자식 테이블 이름", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
                @JsonProperty("childName")
                private final @NotNull String childName;
                @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
                @JsonProperty("createDate")
                private final @NotNull String createDate;
                @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
                @JsonProperty("updateDate")
                private final @NotNull String updateDate;
            }
        }
    }


    ////
    @Operation(
            summary = "외래키 관련 테이블 Rows 조회 (QueryDsl)",
            description = "QueryDsl 을 사용하여 외래키 관련 테이블의 모든 Rows 를 반환합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    )
            }
    )
    @GetMapping(
            path = {"/fk-table-dsl"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable SelectFkTableRowsWithQueryDslOutputVo selectFkTableRowsWithQueryDsl(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse
    ) {
        return service.selectFkTableRowsWithQueryDsl(httpServletResponse);
    }

    @Data
    public static class SelectFkTableRowsWithQueryDslOutputVo {
        @Schema(description = "부모 아이템 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("parentEntityVoList")
        private final @NotNull List<ParentEntityVo> parentEntityVoList;

        @Schema(description = "부모 아이템")
        @Data
        public static class ParentEntityVo {
            @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
            @JsonProperty("uid")
            private final @NotNull Long uid;
            @Schema(description = "부모 테이블 이름", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
            @JsonProperty("parentName")
            private final @NotNull String parentName;
            @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("createDate")
            private final @NotNull String createDate;
            @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("updateDate")
            private final @NotNull String updateDate;
            @Schema(description = "부모 테이블에 속하는 자식 테이블들", requiredMode = Schema.RequiredMode.REQUIRED)
            @JsonProperty("childEntityList")
            private final @NotNull List<ChildEntityVo> childEntityList;

            @Schema(description = "자식 아이템")
            @Data
            public static class ChildEntityVo {
                @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
                @JsonProperty("uid")
                private final @NotNull Long uid;
                @Schema(description = "자식 테이블 이름", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
                @JsonProperty("childName")
                private final @NotNull String childName;
                @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
                @JsonProperty("createDate")
                private final @NotNull String createDate;
                @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
                @JsonProperty("updateDate")
                private final @NotNull String updateDate;
            }
        }
    }


    ////
    @Operation(
            summary = "외래키 관련 테이블 Rows 조회 및 부모 테이블 이름으로 필터링 (QueryDsl)",
            description = "QueryDsl 을 사용하여 외래키 관련 테이블의 모든 Rows 를 반환합니다.<br>" +
                    "추가로, 부모 테이블에 할당된 이름으로 검색 결과를 필터링합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    )
            }
    )
    @GetMapping(
            path = {"/fk-table-parent-name-filter-dsl"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable SelectFkTableRowsByParentNameFilterWithQueryDslOutputVo selectFkTableRowsByParentNameFilterWithQueryDsl(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(name = "parentName", description = "필터링 할 parentName 변수", example = "홍길동")
            @RequestParam(value = "parentName")
            @NotNull String parentName
    ) {
        return service.selectFkTableRowsByParentNameFilterWithQueryDsl(httpServletResponse, parentName);
    }

    @Data
    public static class SelectFkTableRowsByParentNameFilterWithQueryDslOutputVo {
        @Schema(description = "부모 아이템 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("parentEntityVoList")
        private final @NotNull List<ParentEntityVo> parentEntityVoList;

        @Schema(description = "부모 아이템")
        @Data
        public static class ParentEntityVo {
            @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
            @JsonProperty("uid")
            private final @NotNull Long uid;
            @Schema(description = "부모 테이블 이름", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
            @JsonProperty("parentName")
            private final @NotNull String parentName;
            @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("createDate")
            private final @NotNull String createDate;
            @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("updateDate")
            private final @NotNull String updateDate;
            @Schema(description = "부모 테이블에 속하는 자식 테이블들", requiredMode = Schema.RequiredMode.REQUIRED)
            @JsonProperty("childEntityList")
            private final @NotNull List<ChildEntityVo> childEntityList;

            @Schema(description = "자식 아이템")
            @Data
            public static class ChildEntityVo {
                @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
                @JsonProperty("uid")
                private final @NotNull Long uid;
                @Schema(description = "자식 테이블 이름", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
                @JsonProperty("childName")
                private final @NotNull String childName;
                @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
                @JsonProperty("createDate")
                private final @NotNull String createDate;
                @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
                @JsonProperty("updateDate")
                private final @NotNull String updateDate;
            }
        }
    }


    ////
    @Operation(
            summary = "외래키 관련 테이블 부모 테이블 고유번호로 자식 테이블 리스트 검색 (QueryDsl)",
            description = "부모 테이블 고유번호로 자식 테이블 리스트를 검색하여 반환합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    )
            }
    )
    @GetMapping(
            path = {"/fk-table-child-list-dsl"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable SelectFkTableChildListWithQueryDslOutputVo selectFkTableChildListWithQueryDsl(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(name = "parentUid", description = "parent 테이블 고유번호", example = "1")
            @RequestParam(value = "parentUid")
            @NotNull Long parentUid
    ) {
        return service.selectFkTableChildListWithQueryDsl(httpServletResponse, parentUid);
    }

    @Data
    public static class SelectFkTableChildListWithQueryDslOutputVo {
        @Schema(description = "부모 테이블에 속하는 자식 테이블들", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("childEntityList")
        private final @NotNull List<ChildEntityVo> childEntityList;

        @Schema(description = "자식 아이템")
        @Data
        public static class ChildEntityVo {
            @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
            @JsonProperty("uid")
            private final @NotNull Long uid;
            @Schema(description = "자식 테이블 이름", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
            @JsonProperty("childName")
            private final @NotNull String childName;
            @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("createDate")
            private final @NotNull String createDate;
            @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("updateDate")
            private final @NotNull String updateDate;
        }
    }


    ////
    @Operation(
            summary = "외래키 자식 테이블 Row 삭제 테스트",
            description = "외래키 자식 테이블의 Row 하나를 삭제합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            content = {@Content},
                            description = "Response Body 가 없습니다.<br>" +
                                    "Response Headers 를 확인하세요.",
                            headers = {
                                    @Header(
                                            name = "api-result-code",
                                            description = "(Response Code 반환 원인) - Required<br>" +
                                                    "1 : index 에 해당하는 데이터가 데이터베이스에 존재하지 않습니다.",
                                            schema = @Schema(type = "string")
                                    )
                            }
                    )
            }
    )
    @DeleteMapping(
            path = {"/fk-child/{index}"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.ALL_VALUE
    )
    @ResponseBody
    public void deleteFkChildRowSample(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @PathVariable(value = "index")
            @NotNull Long index
    ) {
        service.deleteFkChildRowSample(httpServletResponse, index);
    }


    ////
    @Operation(
            summary = "외래키 부모 테이블 Row 삭제 테스트 (Cascade 기능 확인)",
            description = "외래키 부모 테이블의 Row 하나를 삭제합니다.<br>" +
                    "Cascade 설정을 했으므로 부모 테이블이 삭제되면 해당 부모 테이블을 참조중인 다른 모든 자식 테이블들이 삭제되어야 합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            content = {@Content},
                            description = "Response Body 가 없습니다.<br>" +
                                    "Response Headers 를 확인하세요.",
                            headers = {
                                    @Header(
                                            name = "api-result-code",
                                            description = "(Response Code 반환 원인) - Required<br>" +
                                                    "1 : index 에 해당하는 데이터가 데이터베이스에 존재하지 않습니다.",
                                            schema = @Schema(type = "string")
                                    )
                            }
                    )
            }
    )
    @DeleteMapping(
            path = {"/fk-parent/{index}"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.ALL_VALUE
    )
    @ResponseBody
    public void deleteFkParentRowSample(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @PathVariable(value = "index")
            @NotNull Long index
    ) {
        service.deleteFkParentRowSample(httpServletResponse, index);
    }


    ////
    @Operation(
            summary = "외래키 테이블 트랜젝션 동작 테스트",
            description = "외래키 테이블에 정보 입력 후 Exception 이 발생했을 때 롤백되어 데이터가 저장되지 않는지를 테스트하는 API"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    )
            }
    )
    @PostMapping(
            path = {"/fk-transaction-rollback-sample"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.ALL_VALUE
    )
    @ResponseBody
    public void fkTableTransactionTest(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse
    ) {
        service.fkTableTransactionTest(httpServletResponse);
    }


    ////
    @Operation(
            summary = "외래키 테이블 트랜젝션 비동작 테스트",
            description = "외래키 테이블의 트랜젝션 처리를 하지 않았을 때, DB 정보 입력 후 Exception 이 발생 했을 때 의 테스트 API"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    )
            }
    )
    @PostMapping(
            path = {"/fk-no-transaction-exception-sample"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.ALL_VALUE
    )
    @ResponseBody
    public void fkTableNonTransactionTest(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse
    ) {
        service.fkTableNonTransactionTest(httpServletResponse);
    }


    // ----
    @Operation(
            summary = "ORM Datatype Mapping 테이블 Row 입력 테스트 API",
            description = "ORM Datatype Mapping 테이블에 값이 잘 입력되는지 테스트"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    )
            }
    )
    @PostMapping(
            path = {"/orm-datatype-mapping-test"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable OrmDatatypeMappingTestOutputVo ormDatatypeMappingTest(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @RequestBody
            @NotNull OrmDatatypeMappingTestInputVo inputVo
    ) {
        return service.ormDatatypeMappingTest(
                httpServletResponse,
                inputVo
        );
    }

    @Data
    public static class OrmDatatypeMappingTestInputVo {
        @Schema(description = "TINYINT 타입 컬럼(-128 ~ 127 정수 (1Byte))", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("sampleTinyInt")
        private final @NotNull Short sampleTinyInt;
        @Schema(description = "TINYINT UNSIGNED 타입 컬럼(0 ~ 255 정수 (1Byte))", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("sampleTinyIntUnsigned")
        private final @NotNull Short sampleTinyIntUnsigned;
        @Schema(description = "SMALLINT 타입 컬럼(-32,768 ~ 32,767 정수 (2Byte))", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("sampleSmallInt")
        private final @NotNull Short sampleSmallInt;
        @Schema(description = "SMALLINT UNSIGNED 타입 컬럼(0 ~ 65,535 정수 (2Byte))", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("sampleSmallIntUnsigned")
        private final @NotNull Integer sampleSmallIntUnsigned;
        @Schema(description = "MEDIUMINT 타입 컬럼(-8,388,608 ~ 8,388,607 정수 (3Byte))", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("sampleMediumInt")
        private final @NotNull Integer sampleMediumInt;
        @Schema(description = "MEDIUMINT UNSIGNED 타입 컬럼(0 ~ 16,777,215 정수 (3Byte))", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("sampleMediumIntUnsigned")
        private final @NotNull Integer sampleMediumIntUnsigned;
        @Schema(description = "INT 타입 컬럼(-2,147,483,648 ~ 2,147,483,647 정수 (4Byte))", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("sampleInt")
        private final @NotNull Integer sampleInt;
        @Schema(description = "INT UNSIGNED 타입 컬럼(0 ~ 4,294,967,295 정수 (4Byte))", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("sampleIntUnsigned")
        private final @NotNull Long sampleIntUnsigned;
        @Schema(description = "BIGINT 타입 컬럼(-2^63 ~ 2^63-1 정수 (8Byte))", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("sampleBigInt")
        private final @NotNull Long sampleBigInt;
        @Schema(description = "BIGINT UNSIGNED 타입 컬럼(0 ~ 2^64-1 정수 (8Byte))", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("sampleBigIntUnsigned")
        private final @NotNull BigInteger sampleBigIntUnsigned;
        @Schema(description = "FLOAT 타입 컬럼(-3.4E38 ~ 3.4E38 단정밀도 부동소수점 (4Byte))", requiredMode = Schema.RequiredMode.REQUIRED, example = "1.1")
        @JsonProperty("sampleFloat")
        private final @NotNull Float sampleFloat;
        @Schema(description = "FLOAT UNSIGNED 타입 컬럼(0 ~ 3.402823466E+38 단정밀도 부동소수점 (4Byte))", requiredMode = Schema.RequiredMode.REQUIRED, example = "1.1")
        @JsonProperty("sampleFloatUnsigned")
        private final @NotNull Float sampleFloatUnsigned;
        @Schema(description = "DOUBLE 타입 컬럼(-1.7E308 ~ 1.7E308 배정밀도 부동소수점 (8Byte))", requiredMode = Schema.RequiredMode.REQUIRED, example = "1.1")
        @JsonProperty("sampleDouble")
        private final @NotNull Double sampleDouble;
        @Schema(description = "DOUBLE UNSIGNED 타입 컬럼(0 ~ 1.7976931348623157E+308 배정밀도 부동소수점 (8Byte))", requiredMode = Schema.RequiredMode.REQUIRED, example = "1.1")
        @JsonProperty("sampleDoubleUnsigned")
        private final @NotNull Double sampleDoubleUnsigned;
        @Schema(description = "DECIMAL(65, 10) 타입 컬럼(p(전체 자릿수, 최대 65), s(소수점 아래 자릿수, p 보다 작거나 같아야 함) 설정 가능 고정 소수점 숫자)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1.1")
        @JsonProperty("sampleDecimalP65S10")
        private final @NotNull BigDecimal sampleDecimalP65S10;
        @Schema(description = "DATE 타입 컬럼(1000-01-01 ~ 9999-12-31, yyyy_MM_dd_z, 00시 00분 00초 기준)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_KST")
        @JsonProperty("sampleDate")
        private final @NotNull String sampleDate;
        @Schema(description = "DATETIME 타입 컬럼(1000-01-01 00:00:00 ~ 9999-12-31 23:59:59, yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
        @JsonProperty("sampleDatetime")
        private final @NotNull String sampleDatetime;
        @Schema(description = "TIME 타입 컬럼(-838:59:59 ~ 838:59:59, HH_mm_ss_SSS)", requiredMode = Schema.RequiredMode.REQUIRED, example = "01_01_01_111")
        @JsonProperty("sampleTime")
        private final @NotNull String sampleTime;
        @Schema(description = "TIMESTAMP 타입 컬럼(1970-01-01 00:00:01 ~ 2038-01-19 03:14:07 날짜 데이터 저장시 UTC 기준으로 저장되고, 조회시 시스템 설정에 맞게 반환, yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
        @JsonProperty("sampleTimestamp")
        private final @NotNull String sampleTimestamp;
        @Schema(description = "YEAR 타입 컬럼(1901 ~ 2155 년도)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024")
        @JsonProperty("sampleYear")
        private final @NotNull Integer sampleYear;
        @Schema(description = "CHAR(12) 타입 컬럼(12 바이트 입력 허용)", requiredMode = Schema.RequiredMode.REQUIRED, example = "test")
        @JsonProperty("sampleChar12")
        private final @NotNull String sampleChar12;
        @Schema(description = "VARCHAR(12) 타입 컬럼(12 바이트 입력 허용)", requiredMode = Schema.RequiredMode.REQUIRED, example = "test")
        @JsonProperty("sampleVarchar12")
        private final @NotNull String sampleVarchar12;
        @Schema(description = "TINYTEXT 타입 컬럼(가변 길이 문자열 최대 255 Byte)", requiredMode = Schema.RequiredMode.REQUIRED, example = "test")
        @JsonProperty("sampleTinyText")
        private final @NotNull String sampleTinyText;
        @Schema(description = "TEXT 타입 컬럼(가변 길이 문자열 최대 65,535 Byte)", requiredMode = Schema.RequiredMode.REQUIRED, example = "test")
        @JsonProperty("sampleText")
        private final @NotNull String sampleText;
        @Schema(description = "MEDIUMTEXT 타입 컬럼(가변 길이 문자열 최대 16,777,215 Byte)", requiredMode = Schema.RequiredMode.REQUIRED, example = "test")
        @JsonProperty("sampleMediumText")
        private final @NotNull String sampleMediumText;
        @Schema(description = "LONGTEXT 타입 컬럼(가변 길이 문자열 최대 4,294,967,295 Byte)", requiredMode = Schema.RequiredMode.REQUIRED, example = "test")
        @JsonProperty("sampleLongText")
        private final @NotNull String sampleLongText;
        @Schema(description = "1 bit 값 (Boolean 으로 사용할 수 있습니다. (1 : 참, 0 : 거짓))", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
        @JsonProperty("sampleOneBit")
        private final @NotNull Boolean sampleOneBit;
        @Schema(description = "6 bit 값 (bit 사이즈에 따라 변수 사이즈를 맞춰 매핑)", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
        @JsonProperty("sample6Bit")
        private final @NotNull Short sample6Bit;
        @Schema(description = "JSON 타입", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @JsonProperty("sampleJson")
        private final @Nullable SampleJsonVo sampleJson;
        @Schema(description = "A, B, C 중 하나", requiredMode = Schema.RequiredMode.REQUIRED, example = "A")
        @JsonProperty("sampleEnumAbc")
        private final @NotNull Db1_Template_DataTypeMappingTest.EnumAbc sampleEnumAbc;
        @Schema(description = "A, B, C 중 하나 (SET)", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "[\"A\", \"B\"]")
        @JsonProperty("sampleSetAbc")
        private final @Nullable Set<Db1_Template_DataTypeMappingTest.EnumAbc> sampleSetAbc;
        @Schema(description = "GEOMETRY 타입(Point, Line, Polygon 데이터 중 어느것이라도 하나를 넣을 수 있습니다.), 여기선 Point", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("sampleGeometry")
        private final @NotNull PointVo sampleGeometry;
        @Schema(description = "(X, Y) 공간 좌표", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("samplePoint")
        private final @NotNull PointVo samplePoint;
        @Schema(description = "직선 좌표 시퀀스", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("sampleLinestring")
        private final @NotNull LinestringVo sampleLinestring;
        @Schema(description = "고정 길이 이진 데이터 (최대 65535 바이트), 암호화된 값, UUID, 고정 길이 해시값 등을 저장하는 역할", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("sampleBinary2")
        private final @NotNull Short sampleBinary2;
        @Schema(description = "가변 길이 이진 데이터 (최대 65535 바이트), 동적 크기의 바이너리 데이터, 이미지 등을 저장하는 역할", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("sampleVarbinary2")
        private final @NotNull Short sampleVarbinary2;

        @Schema(description = "Sample Json Value Object")
        @Data
        public static class SampleJsonVo {
            @Schema(description = "json 으로 입력할 String", requiredMode = Schema.RequiredMode.REQUIRED, example = "sampleJsonStr")
            @JsonProperty("sampleJsonStr")
            private final @NotNull String sampleJsonStr;
            @Schema(description = "json 으로 입력할 Int", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1")
            @JsonProperty("sampleJsonInt")
            private final @Nullable Integer sampleJsonInt;
        }

        @Schema(description = "Point Object")
        @Data
        public static class PointVo {
            @Schema(description = "x value", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1.3")
            @JsonProperty("x")
            private final @NotNull Double x;
            @Schema(description = "y value", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "2.9")
            @JsonProperty("y")
            private final @NotNull Double y;
        }

        @Schema(description = "Linestring Object")
        @Data
        public static class LinestringVo {
            @Schema(description = "첫번째 점", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
            @JsonProperty("point1")
            private final @NotNull PointVo point1;
            @Schema(description = "두번째 점", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
            @JsonProperty("point2")
            private final @NotNull PointVo point2;
        }
    }

    @Data
    public static class OrmDatatypeMappingTestOutputVo {
        @Schema(description = "TINYINT 타입 컬럼(-128 ~ 127 정수 (1Byte))", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("sampleTinyInt")
        private final @NotNull Short sampleTinyInt;
        @Schema(description = "TINYINT UNSIGNED 타입 컬럼(0 ~ 255 정수 (1Byte))", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("sampleTinyIntUnsigned")
        private final @NotNull Short sampleTinyIntUnsigned;
        @Schema(description = "SMALLINT 타입 컬럼(-32,768 ~ 32,767 정수 (2Byte))", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("sampleSmallInt")
        private final @NotNull Short sampleSmallInt;
        @Schema(description = "SMALLINT UNSIGNED 타입 컬럼(0 ~ 65,535 정수 (2Byte))", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("sampleSmallIntUnsigned")
        private final @NotNull Integer sampleSmallIntUnsigned;
        @Schema(description = "MEDIUMINT 타입 컬럼(-8,388,608 ~ 8,388,607 정수 (3Byte))", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("sampleMediumInt")
        private final @NotNull Integer sampleMediumInt;
        @Schema(description = "MEDIUMINT UNSIGNED 타입 컬럼(0 ~ 16,777,215 정수 (3Byte))", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("sampleMediumIntUnsigned")
        private final @NotNull Integer sampleMediumIntUnsigned;
        @Schema(description = "INT 타입 컬럼(-2,147,483,648 ~ 2,147,483,647 정수 (4Byte))", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("sampleInt")
        private final @NotNull Integer sampleInt;
        @Schema(description = "INT UNSIGNED 타입 컬럼(0 ~ 4,294,967,295 정수 (4Byte))", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("sampleIntUnsigned")
        private final @NotNull Long sampleIntUnsigned;
        @Schema(description = "BIGINT 타입 컬럼(-2^63 ~ 2^63-1 정수 (8Byte))", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("sampleBigInt")
        private final @NotNull Long sampleBigInt;
        @Schema(description = "BIGINT UNSIGNED 타입 컬럼(0 ~ 2^64-1 정수 (8Byte))", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("sampleBigIntUnsigned")
        private final @NotNull BigInteger sampleBigIntUnsigned;
        @Schema(description = "FLOAT 타입 컬럼(-3.4E38 ~ 3.4E38 단정밀도 부동소수점 (4Byte))", requiredMode = Schema.RequiredMode.REQUIRED, example = "1.1")
        @JsonProperty("sampleFloat")
        private final @NotNull Float sampleFloat;
        @Schema(description = "FLOAT UNSIGNED 타입 컬럼(0 ~ 3.402823466E+38 단정밀도 부동소수점 (4Byte))", requiredMode = Schema.RequiredMode.REQUIRED, example = "1.1")
        @JsonProperty("sampleFloatUnsigned")
        private final @NotNull Float sampleFloatUnsigned;
        @Schema(description = "DOUBLE 타입 컬럼(-1.7E308 ~ 1.7E308 배정밀도 부동소수점 (8Byte))", requiredMode = Schema.RequiredMode.REQUIRED, example = "1.1")
        @JsonProperty("sampleDouble")
        private final @NotNull Double sampleDouble;
        @Schema(description = "DOUBLE UNSIGNED 타입 컬럼(0 ~ 1.7976931348623157E+308 배정밀도 부동소수점 (8Byte))", requiredMode = Schema.RequiredMode.REQUIRED, example = "1.1")
        @JsonProperty("sampleDoubleUnsigned")
        private final @NotNull Double sampleDoubleUnsigned;
        @Schema(description = "DECIMAL(65, 10) 타입 컬럼(p(전체 자릿수, 최대 65), s(소수점 아래 자릿수, p 보다 작거나 같아야 함) 설정 가능 고정 소수점 숫자)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1.1")
        @JsonProperty("sampleDecimalP65S10")
        private final @NotNull BigDecimal sampleDecimalP65S10;
        @Schema(description = "DATE 타입 컬럼(1000-01-01 ~ 9999-12-31, yyyy_MM_dd_z, 00시 00분 00초 기준)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_KST")
        @JsonProperty("sampleDate")
        private final @NotNull String sampleDate;
        @Schema(description = "DATETIME 타입 컬럼(1000-01-01 00:00:00 ~ 9999-12-31 23:59:59, yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
        @JsonProperty("sampleDatetime")
        private final @NotNull String sampleDatetime;
        @Schema(description = "TIME 타입 컬럼(-838:59:59 ~ 838:59:59, HH_mm_ss_SSS)", requiredMode = Schema.RequiredMode.REQUIRED, example = "01_01_01_111")
        @JsonProperty("sampleTime")
        private final @NotNull String sampleTime;
        @Schema(description = "TIMESTAMP 타입 컬럼(1970-01-01 00:00:01 ~ 2038-01-19 03:14:07 날짜 데이터 저장시 UTC 기준으로 저장되고, 조회시 시스템 설정에 맞게 반환, yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
        @JsonProperty("sampleTimestamp")
        private final @NotNull String sampleTimestamp;
        @Schema(description = "YEAR 타입 컬럼(1901 ~ 2155 년도)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024")
        @JsonProperty("sampleYear")
        private final @NotNull Integer sampleYear;
        @Schema(description = "CHAR(12) 타입 컬럼(12 바이트 입력 허용)", requiredMode = Schema.RequiredMode.REQUIRED, example = "test")
        @JsonProperty("sampleChar12")
        private final @NotNull String sampleChar12;
        @Schema(description = "VARCHAR(12) 타입 컬럼(12 바이트 입력 허용)", requiredMode = Schema.RequiredMode.REQUIRED, example = "test")
        @JsonProperty("sampleVarchar12")
        private final @NotNull String sampleVarchar12;
        @Schema(description = "TINYTEXT 타입 컬럼(가변 길이 문자열 최대 255 Byte)", requiredMode = Schema.RequiredMode.REQUIRED, example = "test")
        @JsonProperty("sampleTinyText")
        private final @NotNull String sampleTinyText;
        @Schema(description = "TEXT 타입 컬럼(가변 길이 문자열 최대 65,535 Byte)", requiredMode = Schema.RequiredMode.REQUIRED, example = "test")
        @JsonProperty("sampleText")
        private final @NotNull String sampleText;
        @Schema(description = "MEDIUMTEXT 타입 컬럼(가변 길이 문자열 최대 16,777,215 Byte)", requiredMode = Schema.RequiredMode.REQUIRED, example = "test")
        @JsonProperty("sampleMediumText")
        private final @NotNull String sampleMediumText;
        @Schema(description = "LONGTEXT 타입 컬럼(가변 길이 문자열 최대 4,294,967,295 Byte)", requiredMode = Schema.RequiredMode.REQUIRED, example = "test")
        @JsonProperty("sampleLongText")
        private final @NotNull String sampleLongText;
        @Schema(description = "1 bit 값 (Boolean 으로 사용할 수 있습니다. (1 : 참, 0 : 거짓))", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
        @JsonProperty("sampleOneBit")
        private final @NotNull Boolean sampleOneBit;
        @Schema(description = "6 bit 값 (bit 사이즈에 따라 변수 사이즈를 맞춰 매핑)", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
        @JsonProperty("sample6Bit")
        private final @NotNull Short sample6Bit;
        @Schema(description = "JSON 타입", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @JsonProperty("sampleJson")
        private final @Nullable String sampleJson;
        @Schema(description = "A, B, C 중 하나", requiredMode = Schema.RequiredMode.REQUIRED, example = "A")
        @JsonProperty("sampleEnumAbc")
        private final @NotNull Db1_Template_DataTypeMappingTest.EnumAbc sampleEnumAbc;
        @Schema(description = "A, B, C 중 하나 (SET)", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "[\"A\", \"B\"]")
        @JsonProperty("sampleSetAbc")
        private final @Nullable Set<Db1_Template_DataTypeMappingTest.EnumAbc> sampleSetAbc;
        @Schema(description = "GEOMETRY 타입(Point, Line, Polygon 데이터 중 어느것이라도 하나를 넣을 수 있습니다.), 여기선 Point", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("sampleGeometry")
        private final @NotNull PointVo sampleGeometry;
        @Schema(description = "(X, Y) 공간 좌표", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("samplePoint")
        private final @NotNull PointVo samplePoint;
        @Schema(description = "직선 좌표 시퀀스", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("sampleLinestring")
        private final @NotNull LinestringVo sampleLinestring;
        @Schema(description = "폴리곤", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("samplePolygon")
        private final @NotNull List<PointVo> samplePolygon;
        @Schema(description = "고정 길이 이진 데이터 (최대 65535 바이트), 암호화된 값, UUID, 고정 길이 해시값 등을 저장하는 역할", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("sampleBinary2")
        private final @NotNull Short sampleBinary2;
        @Schema(description = "가변 길이 이진 데이터 (최대 65535 바이트), 동적 크기의 바이너리 데이터, 이미지 등을 저장하는 역할", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("sampleVarbinary2")
        private final @NotNull Short sampleVarbinary2;

        @Schema(description = "Sample Json Value Object")
        @Data
        public static class SampleJsonVo {
            @Schema(description = "json 으로 입력할 String", requiredMode = Schema.RequiredMode.REQUIRED, example = "sampleJsonStr")
            @JsonProperty("sampleJsonStr")
            private final @NotNull String sampleJsonStr;
            @Schema(description = "json 으로 입력할 Int", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1")
            @JsonProperty("sampleJsonInt")
            private final @Nullable Integer sampleJsonInt;
        }

        @Schema(description = "Point Object")
        @Data
        public static class PointVo {
            @Schema(description = "x value", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1.3")
            @JsonProperty("x")
            private final @NotNull Double x;
            @Schema(description = "y value", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "2.9")
            @JsonProperty("y")
            private final @NotNull Double y;
        }

        @Schema(description = "Linestring Object")
        @Data
        public static class LinestringVo {
            @Schema(description = "첫번째 점", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
            @JsonProperty("point1")
            private final @NotNull PointVo point1;
            @Schema(description = "두번째 점", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
            @JsonProperty("point2")
            private final @NotNull PointVo point2;
        }
    }


    // ----
    @Operation(
            summary = "ORM Blob Datatype Mapping 테이블 Row 입력 테스트 API",
            description = "ORM Blob Datatype Mapping 테이블에 값이 잘 입력되는지 테스트"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    )
            }
    )
    @PostMapping(
            path = {"/orm-blob-datatype-mapping-test"},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.ALL_VALUE
    )
    @ResponseBody
    public void ormBlobDatatypeMappingTest(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @ModelAttribute
            @RequestBody
            @NotNull OrmBlobDatatypeMappingTestInputVo inputVo
    ) throws IOException {
        service.ormBlobDatatypeMappingTest(
                httpServletResponse,
                inputVo
        );
    }

    @Data
    public static class OrmBlobDatatypeMappingTestInputVo {
        @Schema(description = "최대 255 바이트 파일", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("sampleTinyBlob")
        private final @NotNull MultipartFile sampleTinyBlob;
        @Schema(description = "최대 65,535바이트 파일", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("sampleBlob")
        private final @NotNull MultipartFile sampleBlob;
        @Schema(description = "최대 16,777,215 바이트 파일", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("sampleMediumBlob")
        private final @NotNull MultipartFile sampleMediumBlob;
        @Schema(description = "최대 4,294,967,295 바이트 파일", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("sampleLongBlob")
        private final @NotNull MultipartFile sampleLongBlob;
    }
}
