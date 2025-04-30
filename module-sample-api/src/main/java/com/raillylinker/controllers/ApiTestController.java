package com.raillylinker.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.raillylinker.services.ApiTestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Tag(name = "/api-test APIs", description = "API 요청 / 응답에 대한 테스트 컨트롤러")
@Controller
@RequestMapping("/api-test")
@Validated
public class ApiTestController {
    public ApiTestController(
            @NotNull ApiTestService service
    ) {
        this.service = service;
    }

    // <멤버 변수 공간>
    private final @NotNull ApiTestService service;


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @Operation(
            summary = "기본 요청 테스트 API",
            description = "이 API 를 요청하면 현재 실행중인 프로필 이름을 반환합니다."
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
            path = {""},
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.TEXT_PLAIN_VALUE}
    )
    @ResponseBody
    public @Nullable String basicRequestTest(
            @Parameter(hidden = true)
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull HttpServletResponse httpServletResponse
    ) {
        return service.basicRequestTest(httpServletResponse);
    }


    // ----
    @Operation(
            summary = "요청 Redirect 테스트 API",
            description = "이 API 를 요청하면 /my-service/tk/sample/request-test 로 Redirect 됩니다."
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
            path = {"/redirect-to-blank"},
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.ALL_VALUE}
    )
    @ResponseBody
    public @Nullable ModelAndView redirectTest(
            @Parameter(hidden = true)
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull HttpServletResponse httpServletResponse
    ) {
        return service.redirectTest(httpServletResponse);
    }


    // ----
    @Operation(
            summary = "요청 Forward 테스트 API",
            description = "이 API 를 요청하면 /my-service/tk/sample/request-test 로 Forward 됩니다."
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
            path = {"/forward-to-blank"},
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.ALL_VALUE}
    )
    @ResponseBody
    public @Nullable ModelAndView forwardTest(
            @Parameter(hidden = true)
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull HttpServletResponse httpServletResponse
    ) {
        return service.forwardTest(httpServletResponse);
    }


    // ----
    @Operation(
            summary = "Get 요청 테스트 (Query Parameter)",
            description = "Query Parameter 를 받는 Get 메소드 요청 테스트"
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
            path = {"/get-request"},
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseBody
    public @Nullable GetRequestTestOutputVo getRequestTest(
            @Parameter(hidden = true)
            HttpServletResponse httpServletResponse,
            @Parameter(name = "queryParamString", description = "String Query 파라미터", example = "testString")
            @RequestParam("queryParamString")
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull String queryParamString,
            @Parameter(name = "queryParamStringNullable", description = "String Query 파라미터 Nullable", example = "testString")
            @RequestParam(value = "queryParamStringNullable", required = false)
            @Nullable String queryParamStringNullable,
            @Parameter(name = "queryParamInt", description = "Int Query 파라미터", example = "1")
            @RequestParam("queryParamInt")
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull Integer queryParamInt,
            @Parameter(name = "queryParamIntNullable", description = "Int Query 파라미터 Nullable", example = "1")
            @RequestParam(value = "queryParamIntNullable", required = false)
            @Nullable Integer queryParamIntNullable,
            @Parameter(name = "queryParamDouble", description = "Double Query 파라미터", example = "1.1")
            @RequestParam("queryParamDouble")
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull Double queryParamDouble,
            @Parameter(name = "queryParamDoubleNullable", description = "Double Query 파라미터 Nullable", example = "1.1")
            @RequestParam(value = "queryParamDoubleNullable", required = false)
            @Nullable Double queryParamDoubleNullable,
            @Parameter(name = "queryParamBoolean", description = "Boolean Query 파라미터", example = "true")
            @RequestParam("queryParamBoolean")
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull Boolean queryParamBoolean,
            @Parameter(name = "queryParamBooleanNullable", description = "Boolean Query 파라미터 Nullable", example = "true")
            @RequestParam(value = "queryParamBooleanNullable", required = false)
            @Nullable Boolean queryParamBooleanNullable,
            @Parameter(name = "queryParamStringList", description = "StringList Query 파라미터", example = "[\"testString1\", \"testString2\"]")
            @RequestParam("queryParamStringList")
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull List<String> queryParamStringList,
            @Parameter(name = "queryParamStringListNullable", description = "StringList Query 파라미터 Nullable", example = "[\"testString1\", \"testString2\"]")
            @RequestParam(value = "queryParamStringListNullable", required = false)
            @Nullable List<String> queryParamStringListNullable
    ) {
        return service.getRequestTest(
                httpServletResponse,
                queryParamString,
                queryParamStringNullable,
                queryParamInt,
                queryParamIntNullable,
                queryParamDouble,
                queryParamDoubleNullable,
                queryParamBoolean,
                queryParamBooleanNullable,
                queryParamStringList,
                queryParamStringListNullable
        );
    }

    @Data
    public static class GetRequestTestOutputVo {
        @Schema(description = "입력한 String Query 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "testString")
        @JsonProperty("queryParamString")
        private final @NotNull String queryParamString;
        @Schema(description = "입력한 String Nullable Query 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "testString")
        @JsonProperty("queryParamStringNullable")
        private final @Nullable String queryParamStringNullable;
        @Schema(description = "입력한 Int Query 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("queryParamInt")
        private final @NotNull Integer queryParamInt;
        @Schema(description = "입력한 Int Nullable Query 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1")
        @JsonProperty("queryParamIntNullable")
        private final @Nullable Integer queryParamIntNullable;
        @Schema(description = "입력한 Double Query 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "1.1")
        @JsonProperty("queryParamDouble")
        private final @NotNull Double queryParamDouble;
        @Schema(description = "입력한 Double Nullable Query 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1.1")
        @JsonProperty("queryParamDoubleNullable")
        private final @Nullable Double queryParamDoubleNullable;
        @Schema(description = "입력한 Boolean Query 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
        @JsonProperty("queryParamBoolean")
        private final @NotNull Boolean queryParamBoolean;
        @Schema(description = "입력한 Boolean Nullable Query 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "true")
        @JsonProperty("queryParamBooleanNullable")
        private final @Nullable Boolean queryParamBooleanNullable;
        @Schema(description = "입력한 StringList Query 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"testString1\", \"testString2\"]")
        @JsonProperty("queryParamStringList")
        private final @NotNull List<String> queryParamStringList;
        @Schema(description = "입력한 StringList Nullable Query 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "[\"testString1\", \"testString2\"]")
        @JsonProperty("queryParamStringListNullable")
        private final @Nullable List<String> queryParamStringListNullable;
    }
}
