package com.raillylinker.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.raillylinker.services.MongoDbTestService;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
    !!!
    테스트를 하고 싶다면, 도커를 설치하고,
    cmd 를 열어,
    프로젝트 폴더 내의 external_files/dockers/mongodb_docker 로 이동 후,
    명령어.txt 에 적힌 명령어를 입력하여 Mongodb 를 실행시킬 수 있습니다.
    !!!
 */
@Tag(name = "/mongodb-test APIs", description = "MongoDB 테스트 API 컨트롤러")
@Controller
@RequestMapping("/mongodb-test")
@Validated
public class MongoDbTestController {
    public MongoDbTestController(
            @NotNull MongoDbTestService service
    ) {
        this.service = service;
    }

    // <멤버 변수 공간>
    private final @NotNull MongoDbTestService service;


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @Operation(
            summary = "DB Row 입력 테스트 API",
            description = "테스트 테이블에 Row 를 입력합니다."
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
            path = {"/row"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable InsertDataSampleOutputVo insertDataSample(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @RequestBody
            @NotNull InsertDataSampleInputVo inputVo) {
        return service.insertDataSample(httpServletResponse, inputVo);
    }

    @Data
    public static class InsertDataSampleInputVo {
        @Schema(description = "글 본문", requiredMode = Schema.RequiredMode.REQUIRED, example = "테스트 텍스트입니다.")
        @JsonProperty("content")
        private final @NotNull String content;
        @Schema(description = "원하는 날짜(yyyy_MM_dd_'T'_HH_mm_ss_SSS_z)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
        @JsonProperty("dateString")
        private final @NotNull String dateString;
        @Schema(description = "테스트용 nullable 데이터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "test")
        @JsonProperty("nullableValue")
        private final @Nullable String nullableValue;
    }

    @Data
    public static class InsertDataSampleOutputVo {
        @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
        @JsonProperty("uid")
        private final @NotNull String uid;
        @Schema(description = "글 본문", requiredMode = Schema.RequiredMode.REQUIRED, example = "테스트 텍스트입니다.")
        @JsonProperty("content")
        private final @NotNull String content;
        @Schema(description = "자동 생성 숫자", requiredMode = Schema.RequiredMode.REQUIRED, example = "21345")
        @JsonProperty("randomNum")
        private final @NotNull Integer randomNum;
        @Schema(description = "테스트용 일시 데이터", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
        @JsonProperty("testDatetime")
        private final @NotNull String testDatetime;
        @Schema(description = "테스트용 nullable 데이터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "test")
        @JsonProperty("nullableValue")
        private final @Nullable String nullableValue;
        @Schema(description = "글 작성일", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
        @JsonProperty("createDate")
        private final @NotNull String createDate;
        @Schema(description = "글 수정일", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
        @JsonProperty("updateDate")
        private final @NotNull String updateDate;
        @Schema(description = "글 삭제일", requiredMode = Schema.RequiredMode.REQUIRED, example = "/")
        @JsonProperty("deleteDate")
        private final @NotNull String deleteDate;
    }


    // ----
    @Operation(
            summary = "DB Rows 삭제 테스트 API",
            description = "테스트 테이블의 모든 Row 를 모두 삭제합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    )
            }
    )
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
            @RequestParam("deleteLogically")
            @NotNull Boolean deleteLogically
    ) {
        service.deleteRowsSample(httpServletResponse, deleteLogically);
    }


    // ----
    @Operation(
            summary = "DB Row 삭제 테스트",
            description = "테스트 테이블의 Row 하나를 삭제합니다."
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
            path = {"/row/{id}"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.ALL_VALUE
    )
    @ResponseBody
    public void deleteRowSample(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(name = "id", description = "글 아이디", example = "1")
            @PathVariable("id")
            @NotNull String id,
            @Parameter(name = "deleteLogically", description = "논리적 삭제 여부", example = "true")
            @RequestParam("deleteLogically")
            @NotNull Boolean deleteLogically
    ) {
        service.deleteRowSample(httpServletResponse, id, deleteLogically);
    }


    // ----
    @Operation(
            summary = "DB Rows 조회 테스트",
            description = "테스트 테이블의 모든 Rows 를 반환합니다."
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

        @Data
        public static class TestEntityVo {
            @Schema(description = "글 고유번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
            @JsonProperty("uid")
            private final @NotNull String uid;
            @Schema(description = "글 본문", requiredMode = Schema.RequiredMode.REQUIRED, example = "테스트 텍스트입니다.")
            @JsonProperty("content")
            private final @NotNull String content;
            @Schema(description = "자동 생성 숫자", requiredMode = Schema.RequiredMode.REQUIRED, example = "21345")
            @JsonProperty("randomNum")
            private final @NotNull Integer randomNum;
            @Schema(description = "테스트용 일시 데이터", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("testDatetime")
            private final @NotNull String testDatetime;
            @Schema(description = "테스트용 nullable 데이터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "test")
            @JsonProperty("nullableValue")
            private final @Nullable String nullableValue;
            @Schema(description = "글 작성일", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("createDate")
            private final @NotNull String createDate;
            @Schema(description = "글 수정일", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024_05_02_T_15_14_49_552_KST")
            @JsonProperty("updateDate")
            private final @NotNull String updateDate;
            @Schema(description = "글 삭제일", requiredMode = Schema.RequiredMode.REQUIRED, example = "/")
            @JsonProperty("deleteDate")
            private final @NotNull String deleteDate;
        }
    }
}
