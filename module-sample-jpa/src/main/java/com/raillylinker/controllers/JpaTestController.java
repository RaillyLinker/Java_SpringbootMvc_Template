package com.raillylinker.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
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
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.jetbrains.annotations.*;

import java.util.List;

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


//    ////
//    @Operation(
//            summary = "DB Rows 조회 테스트 (페이징)",
//            description = "테스트 테이블의 Rows 를 페이징하여 반환합니다."
//    )
//    @ApiResponses(
//            value = {
//                    @ApiResponse(
//                            responseCode = "200",
//                            description = "정상 동작"
//                    )
//            }
//    )
//    @GetMapping(
//            path = {"/rows/paging"},
//            consumes = MediaType.ALL_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    @ResponseBody
//    public @Nullable SelectRowsPageSampleOutputVo selectRowsPageSample(
//            @Parameter(hidden = true)
//            @NotNull HttpServletResponse httpServletResponse,
//            @Parameter(name = "page", description = "원하는 페이지(1 부터 시작)", example = "1")
//            @RequestParam(value = "page")
//            @NotNull Integer page,
//            @Parameter(name = "pageElementsCount", description = "페이지 아이템 개수", example = "10")
//            @RequestParam(value = "pageElementsCount")
//            @NotNull Integer pageElementsCount
//    ) {
//        return service.selectRowsPageSample(httpServletResponse, page, pageElementsCount);
//    }
//
//    @Data
//    public static class SelectRowsPageSampleOutputVo {
//        @Schema(description = "아이템 전체 개수", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
//        @JsonProperty("totalElements")
//        private final @NotNull Long totalElements;
//        @Schema(description = "아이템 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
//        @JsonProperty("testEntityVoList")
//        private final @NotNull List<TestEntityVo> testEntityVoList;
//
//        @Schema(description = "아이템")
//        @Data
//        public static class TestEntityVo {
//            @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
//            @JsonProperty("uid")
//            private final @NotNull Long uid;
//            @Schema(description = "글 본문", requiredMode = Schema.RequiredMode.REQUIRED, example = "테스트 텍스트입니다.")
//            @JsonProperty("content")
//            private final @NotNull String content;
//            @Schema(description = "자동 생성 숫자", requiredMode = Schema.RequiredMode.REQUIRED, example = "23456")
//            @JsonProperty("randomNum")
//            private final @NotNull Integer randomNum;
//            @Schema(description = "테스트용 일시 데이터(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
//            @JsonProperty("testDatetime")
//            private final @NotNull String testDatetime;
//            @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
//            @JsonProperty("createDate")
//            private final @NotNull String createDate;
//            @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
//            @JsonProperty("updateDate")
//            private final @NotNull String updateDate;
//        }
//    }
//
//
//    ////
//    @Operation(
//            summary = "DB Rows 조회 테스트 (네이티브 쿼리 페이징)",
//            description = "테스트 테이블의 Rows 를 네이티브 쿼리로 페이징하여 반환합니다.<br>" +
//                    "num 을 기준으로 근사치 정렬도 수행합니다."
//    )
//    @ApiResponses(
//            value = {
//                    @ApiResponse(
//                            responseCode = "200",
//                            description = "정상 동작"
//                    )
//            }
//    )
//    @GetMapping(
//            path = {"/rows/native-paging"},
//            consumes = MediaType.ALL_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    @ResponseBody
//    public @Nullable SelectRowsNativeQueryPageSampleOutputVo selectRowsNativeQueryPageSample(
//            @Parameter(hidden = true)
//            @NotNull HttpServletResponse httpServletResponse,
//            @Parameter(name = "page", description = "원하는 페이지(1 부터 시작)", example = "1")
//            @RequestParam(value = "page")
//            @NotNull Integer page,
//            @Parameter(name = "pageElementsCount", description = "페이지 아이템 개수", example = "10")
//            @RequestParam(value = "pageElementsCount")
//            @NotNull Integer pageElementsCount,
//            @Parameter(name = "num", description = "근사값의 기준", example = "1")
//            @RequestParam(value = "num")
//            @NotNull Integer num
//    ) {
//        return service.selectRowsNativeQueryPageSample(httpServletResponse, page, pageElementsCount, num);
//    }
//
//    @Data
//    public static class SelectRowsNativeQueryPageSampleOutputVo {
//        @Schema(description = "아이템 전체 개수", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
//        @JsonProperty("totalElements")
//        private final @NotNull Long totalElements;
//        @Schema(description = "아이템 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
//        @JsonProperty("testEntityVoList")
//        private final @NotNull List<TestEntityVo> testEntityVoList;
//
//        @Schema(description = "아이템")
//        @Data
//        public static class TestEntityVo {
//            @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
//            @JsonProperty("uid")
//            private final @NotNull Long uid;
//            @Schema(description = "글 본문", requiredMode = Schema.RequiredMode.REQUIRED, example = "테스트 텍스트입니다.")
//            @JsonProperty("content")
//            private final @NotNull String content;
//            @Schema(description = "자동 생성 숫자", requiredMode = Schema.RequiredMode.REQUIRED, example = "21345")
//            @JsonProperty("randomNum")
//            private final @NotNull Integer randomNum;
//            @Schema(description = "테스트용 일시 데이터(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
//            @JsonProperty("testDatetime")
//            private final @NotNull String testDatetime;
//            @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
//            @JsonProperty("createDate")
//            private final @NotNull String createDate;
//            @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
//            @JsonProperty("updateDate")
//            private final @NotNull String updateDate;
//            @Schema(description = "기준과의 절대거리", requiredMode = Schema.RequiredMode.REQUIRED, example = "34")
//            @JsonProperty("distance")
//            private final @NotNull Integer distance;
//        }
//    }
//
//
//    ////
//    @Operation(
//            summary = "DB Row 수정 테스트",
//            description = "테스트 테이블의 Row 하나를 수정합니다."
//    )
//    @ApiResponses(
//            value = {
//                    @ApiResponse(
//                            responseCode = "200",
//                            description = "정상 동작"
//                    ),
//                    @ApiResponse(
//                            responseCode = "204",
//                            content = {@Content},
//                            description = "Response Body 가 없습니다.<br>" +
//                                    "Response Headers 를 확인하세요.",
//                            headers = {
//                                    @Header(
//                                            name = "api-result-code",
//                                            description = "(Response Code 반환 원인) - Required<br>" +
//                                                    "1 : testTableUid 에 해당하는 정보가 데이터베이스에 존재하지 않습니다.",
//                                            schema = @Schema(type = "string")
//                                    )
//                            }
//                    )
//            }
//    )
//    @PatchMapping(
//            path = {"/row/{testTableUid}"},
//            consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    @ResponseBody
//    public @Nullable UpdateRowSampleOutputVo updateRowSample(
//            @Parameter(hidden = true)
//            @NotNull HttpServletResponse httpServletResponse,
//            @Parameter(name = "testTableUid", description = "test 테이블의 uid", example = "1")
//            @PathVariable(value = "testTableUid")
//            @NotNull Long testTableUid,
//            @RequestBody
//            @NotNull UpdateRowSampleInputVo inputVo
//    ) {
//        return service.updateRowSample(httpServletResponse, testTableUid, inputVo);
//    }
//
//    @Data
//    public static class UpdateRowSampleInputVo {
//        @Schema(description = "글 본문", requiredMode = Schema.RequiredMode.REQUIRED, example = "테스트 텍스트 수정글입니다.")
//        @JsonProperty("content")
//        private final @NotNull String content;
//        @Schema(description = "원하는 날짜(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
//        @JsonProperty("dateString")
//        private final @NotNull String dateString;
//    }
//
//    @Data
//    public static class UpdateRowSampleOutputVo {
//        @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
//        @JsonProperty("uid")
//        private final @NotNull Long uid;
//        @Schema(description = "글 본문", requiredMode = Schema.RequiredMode.REQUIRED, example = "테스트 텍스트입니다.")
//        @JsonProperty("content")
//        private final @NotNull String content;
//        @Schema(description = "자동 생성 숫자", requiredMode = Schema.RequiredMode.REQUIRED, example = "21345")
//        @JsonProperty("randomNum")
//        private final @NotNull Integer randomNum;
//        @Schema(description = "테스트용 일시 데이터(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
//        @JsonProperty("testDatetime")
//        private final @NotNull String testDatetime;
//        @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
//        @JsonProperty("createDate")
//        private final @NotNull String createDate;
//        @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
//        @JsonProperty("updateDate")
//        private final @NotNull String updateDate;
//    }
//
//
//    ////
//    @Operation(
//            summary = "DB Row 수정 테스트 (네이티브 쿼리)",
//            description = "테스트 테이블의 Row 하나를 네이티브 쿼리로 수정합니다."
//    )
//    @ApiResponses(
//            value = {
//                    @ApiResponse(
//                            responseCode = "200",
//                            description = "정상 동작"
//                    ),
//                    @ApiResponse(
//                            responseCode = "204",
//                            content = {@Content},
//                            description = "Response Body 가 없습니다.<br>" +
//                                    "Response Headers 를 확인하세요.",
//                            headers = {
//                                    @Header(
//                                            name = "api-result-code",
//                                            description = "(Response Code 반환 원인) - Required<br>" +
//                                                    "1 : testTableUid 에 해당하는 정보가 데이터베이스에 존재하지 않습니다.",
//                                            schema = @Schema(type = "string")
//                                    )
//                            }
//                    )
//            }
//    )
//    @PatchMapping(
//            path = {"/row/{testTableUid}/native-query"},
//            consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    @ResponseBody
//    public void updateRowNativeQuerySample(
//            @Parameter(hidden = true)
//            @NotNull HttpServletResponse httpServletResponse,
//            @Parameter(name = "testTableUid", description = "test 테이블의 uid", example = "1")
//            @PathVariable(value = "testTableUid")
//            @NotNull Long testTableUid,
//            @RequestBody
//            @NotNull UpdateRowNativeQuerySampleInputVo inputVo
//    ) {
//        service.updateRowNativeQuerySample(httpServletResponse, testTableUid, inputVo);
//    }
//
//    @Data
//    public static class UpdateRowNativeQuerySampleInputVo {
//        @Schema(description = "글 본문", requiredMode = Schema.RequiredMode.REQUIRED, example = "테스트 텍스트 수정글입니다.")
//        @JsonProperty("content")
//        private final @NotNull String content;
//        @Schema(description = "원하는 날짜(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
//        @JsonProperty("dateString")
//        private final @NotNull String dateString;
//    }
//
//
//    ////
//    @Operation(
//            summary = "DB 정보 검색 테스트",
//            description = "글 본문 내용중 searchKeyword 가 포함된 rows 를 검색하여 반환합니다."
//    )
//    @ApiResponses(
//            value = {
//                    @ApiResponse(
//                            responseCode = "200",
//                            description = "정상 동작"
//                    )
//            }
//    )
//    @GetMapping(
//            path = {"/search-content"},
//            consumes = MediaType.ALL_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    @ResponseBody
//    public @Nullable SelectRowWhereSearchingKeywordSampleOutputVo selectRowWhereSearchingKeywordSample(
//            @Parameter(hidden = true)
//            @NotNull HttpServletResponse httpServletResponse,
//            @Parameter(name = "page", description = "원하는 페이지(1 부터 시작)", example = "1")
//            @RequestParam(value = "page")
//            @NotNull Integer page,
//            @Parameter(name = "pageElementsCount", description = "페이지 아이템 개수", example = "10")
//            @RequestParam(value = "pageElementsCount")
//            @NotNull Integer pageElementsCount,
//            @Parameter(name = "searchKeyword", description = "검색어", example = "테스트")
//            @RequestParam(value = "searchKeyword")
//            @NotNull String searchKeyword
//    ) {
//        return service.selectRowWhereSearchingKeywordSample(httpServletResponse, page, pageElementsCount, searchKeyword);
//    }
//
//    @Data
//    public static class SelectRowWhereSearchingKeywordSampleOutputVo {
//        @Schema(description = "아이템 전체 개수", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
//        @JsonProperty("totalElements")
//        private final @NotNull Long totalElements;
//        @Schema(description = "아이템 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
//        @JsonProperty("testEntityVoList")
//        private final @NotNull List<TestEntityVo> testEntityVoList;
//
//        @Schema(description = "아이템")
//        @Data
//        public static class TestEntityVo {
//            @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
//            @JsonProperty("uid")
//            private final @NotNull Long uid;
//            @Schema(description = "글 본문", requiredMode = Schema.RequiredMode.REQUIRED, example = "테스트 텍스트입니다.")
//            @JsonProperty("content")
//            private final @NotNull String content;
//            @Schema(description = "자동 생성 숫자", requiredMode = Schema.RequiredMode.REQUIRED, example = "21345")
//            @JsonProperty("randomNum")
//            private final @NotNull Integer randomNum;
//            @Schema(description = "테스트용 일시 데이터(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
//            @JsonProperty("testDatetime")
//            private final @NotNull String testDatetime;
//            @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
//            @JsonProperty("createDate")
//            private final @NotNull String createDate;
//            @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
//            @JsonProperty("updateDate")
//            private final @NotNull String updateDate;
//        }
//    }
//
//
//    ////
//    @Operation(
//            summary = "트랜젝션 동작 테스트",
//            description = "정보 입력 후 Exception 이 발생했을 때 롤백되어 데이터가 저장되지 않는지를 테스트하는 API"
//    )
//    @ApiResponses(
//            value = {
//                    @ApiResponse(
//                            responseCode = "200",
//                            description = "정상 동작"
//                    )
//            }
//    )
//    @PostMapping(
//            path = {"/transaction-rollback-sample"},
//            consumes = MediaType.ALL_VALUE,
//            produces = MediaType.ALL_VALUE
//    )
//    @ResponseBody
//    public void transactionTest(
//            @Parameter(hidden = true)
//            @NotNull HttpServletResponse httpServletResponse
//    ) {
//        service.transactionTest(httpServletResponse);
//    }
//
//
//    ////
//    @Operation(
//            summary = "트랜젝션 비동작 테스트",
//            description = "트랜젝션 처리를 하지 않았을 때, DB 정보 입력 후 Exception 이 발생 했을 때 의 테스트 API"
//    )
//    @ApiResponses(
//            value = {
//                    @ApiResponse(
//                            responseCode = "200",
//                            description = "정상 동작"
//                    )
//            }
//    )
//    @PostMapping(
//            path = {"/no-transaction-exception-sample"},
//            consumes = MediaType.ALL_VALUE,
//            produces = MediaType.ALL_VALUE
//    )
//    @ResponseBody
//    public void nonTransactionTest(
//            @Parameter(hidden = true)
//            @NotNull HttpServletResponse httpServletResponse
//    ) {
//        service.nonTransactionTest(httpServletResponse);
//    }
//
//
//    ////
//    @Operation(
//            summary = "트랜젝션 비동작 테스트(try-catch)",
//            description = "에러 발생문이 try-catch 문 안에 있을 때, DB 정보 입력 후 Exception 이 발생 해도 트랜젝션이 동작하지 않는지에 대한 테스트 API"
//    )
//    @ApiResponses(
//            value = {
//                    @ApiResponse(
//                            responseCode = "200",
//                            description = "정상 동작"
//                    )
//            }
//    )
//    @PostMapping(
//            path = {"/try-catch-no-transaction-exception-sample"},
//            consumes = MediaType.ALL_VALUE,
//            produces = MediaType.ALL_VALUE
//    )
//    @ResponseBody
//    public void tryCatchNonTransactionTest(
//            @Parameter(hidden = true)
//            @NotNull HttpServletResponse httpServletResponse
//    ) {
//        service.tryCatchNonTransactionTest(httpServletResponse);
//    }
//
//
//    ////
//    @Operation(
//            summary = "DB Rows 조회 테스트 (중복 없는 네이티브 쿼리 페이징)",
//            description = "테스트 테이블의 Rows 를 네이티브 쿼리로 중복없이 페이징하여 반환합니다.<br>" +
//                    "num 을 기준으로 근사치 정렬도 수행합니다."
//    )
//    @ApiResponses(
//            value = {
//                    @ApiResponse(
//                            responseCode = "200",
//                            description = "정상 동작"
//                    )
//            }
//    )
//    @GetMapping(
//            path = {"/rows/native-paging-no-duplication"},
//            consumes = MediaType.ALL_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    @ResponseBody
//    public @Nullable SelectRowsNoDuplicatePagingSampleOutputVo selectRowsNoDuplicatePagingSample(
//            @Parameter(hidden = true)
//            @NotNull HttpServletResponse httpServletResponse,
//            @Parameter(name = "lastItemUid", description = "이전 페이지에서 받은 마지막 아이템의 Uid (첫 요청이면 null)", example = "1")
//            @RequestParam(value = "lastItemUid", required = false)
//            @Nullable Long lastItemUid,
//            @Parameter(name = "pageElementsCount", description = "페이지 아이템 개수", example = "10")
//            @RequestParam(value = "pageElementsCount")
//            @NotNull Integer pageElementsCount
//    ) {
//        return service.selectRowsNoDuplicatePagingSample(httpServletResponse, lastItemUid, pageElementsCount);
//    }
//
//    @Data
//    public static class SelectRowsNoDuplicatePagingSampleOutputVo {
//        @Schema(description = "아이템 전체 개수", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
//        @JsonProperty("totalElements")
//        private final @NotNull Long totalElements;
//        @Schema(description = "아이템 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
//        @JsonProperty("testEntityVoList")
//        private final @NotNull List<TestEntityVo> testEntityVoList;
//
//        @Schema(description = "아이템")
//        @Data
//        public static class TestEntityVo {
//            @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
//            @JsonProperty("uid")
//            private final @NotNull Long uid;
//            @Schema(description = "글 본문", requiredMode = Schema.RequiredMode.REQUIRED, example = "테스트 텍스트입니다.")
//            @JsonProperty("content")
//            private final @NotNull String content;
//            @Schema(description = "자동 생성 숫자", requiredMode = Schema.RequiredMode.REQUIRED, example = "21345")
//            @JsonProperty("randomNum")
//            private final @NotNull Integer randomNum;
//            @Schema(description = "테스트용 일시 데이터(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
//            @JsonProperty("testDatetime")
//            private final @NotNull String testDatetime;
//            @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
//            @JsonProperty("createDate")
//            private final @NotNull String createDate;
//            @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
//            @JsonProperty("updateDate")
//            private final @NotNull String updateDate;
//        }
//    }
//
//
//    ////
//    @Operation(
//            summary = "DB Rows 조회 테스트 (카운팅)",
//            description = "테스트 테이블의 Rows 를 카운팅하여 반환합니다."
//    )
//    @ApiResponses(
//            value = {
//                    @ApiResponse(
//                            responseCode = "200",
//                            description = "정상 동작"
//                    )
//            }
//    )
//    @GetMapping(
//            path = {"/rows/counting"},
//            consumes = MediaType.ALL_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    @ResponseBody
//    public @Nullable SelectRowsCountSampleOutputVo selectRowsCountSample(
//            @Parameter(hidden = true)
//            @NotNull HttpServletResponse httpServletResponse
//    ) {
//        return service.selectRowsCountSample(httpServletResponse);
//    }
//
//    @Data
//    public static class SelectRowsCountSampleOutputVo {
//        @Schema(description = "아이템 전체 개수", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
//        @JsonProperty("totalElements")
//        private final @NotNull Long totalElements;
//    }
//
//
//    ////
//    @Operation(
//            summary = "DB Rows 조회 테스트 (네이티브 카운팅)",
//            description = "테스트 테이블의 Rows 를 네이티브 쿼리로 카운팅하여 반환합니다."
//    )
//    @ApiResponses(
//            value = {
//                    @ApiResponse(
//                            responseCode = "200",
//                            description = "정상 동작"
//                    )
//            }
//    )
//    @GetMapping(
//            path = {"/rows/native-counting"},
//            consumes = MediaType.ALL_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    @ResponseBody
//    public @Nullable SelectRowsCountByNativeQuerySampleOutputVo selectRowsCountByNativeQuerySample(
//            @Parameter(hidden = true)
//            @NotNull HttpServletResponse httpServletResponse
//    ) {
//        return service.selectRowsCountByNativeQuerySample(httpServletResponse);
//    }
//
//    @Data
//    public static class SelectRowsCountByNativeQuerySampleOutputVo {
//        @Schema(description = "아이템 전체 개수", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
//        @JsonProperty("totalElements")
//        private final @NotNull Long totalElements;
//    }
//
//
//    ////
//    @Operation(
//            summary = "DB Row 조회 테스트 (네이티브)",
//            description = "테스트 테이블의 Row 하나를 네이티브 쿼리로 반환합니다."
//    )
//    @ApiResponses(
//            value = {
//                    @ApiResponse(
//                            responseCode = "200",
//                            description = "정상 동작"
//                    ),
//                    @ApiResponse(
//                            responseCode = "204",
//                            content = {@Content},
//                            description = "Response Body 가 없습니다.<br>" +
//                                    "Response Headers 를 확인하세요.",
//                            headers = {
//                                    @Header(
//                                            name = "api-result-code",
//                                            description = "(Response Code 반환 원인) - Required<br>" +
//                                                    "1 : testTableUid 에 해당하는 정보가 데이터베이스에 존재하지 않습니다.",
//                                            schema = @Schema(type = "string")
//                                    )
//                            }
//                    )
//            }
//    )
//    @GetMapping(
//            path = {"/row/native/{testTableUid}"},
//            consumes = MediaType.ALL_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    @ResponseBody
//    public @Nullable SelectRowByNativeQuerySampleOutputVo selectRowByNativeQuerySample(
//            @Parameter(hidden = true)
//            @NotNull HttpServletResponse httpServletResponse,
//            @Parameter(name = "testTableUid", description = "test 테이블의 uid", example = "1")
//            @PathVariable(value = "testTableUid")
//            @NotNull Long testTableUid
//    ) {
//        return service.selectRowByNativeQuerySample(httpServletResponse, testTableUid);
//    }
//
//    @Data
//    public static class SelectRowByNativeQuerySampleOutputVo {
//        @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
//        @JsonProperty("uid")
//        private final @NotNull Long uid;
//        @Schema(description = "글 본문", requiredMode = Schema.RequiredMode.REQUIRED, example = "테스트 텍스트입니다.")
//        @JsonProperty("content")
//        private final @NotNull String content;
//        @Schema(description = "자동 생성 숫자", requiredMode = Schema.RequiredMode.REQUIRED, example = "21345")
//        @JsonProperty("randomNum")
//        private final @NotNull Integer randomNum;
//        @Schema(description = "테스트용 일시 데이터(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
//        @JsonProperty("testDatetime")
//        private final @NotNull String testDatetime;
//        @Schema(description = "글 작성일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
//        @JsonProperty("createDate")
//        private final @NotNull String createDate;
//        @Schema(description = "글 수정일(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
//        @JsonProperty("updateDate")
//        private final @NotNull String updateDate;
//    }
}
