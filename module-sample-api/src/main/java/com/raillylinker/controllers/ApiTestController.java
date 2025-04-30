package com.raillylinker.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.raillylinker.services.ApiTestService;
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
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
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
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(name = "queryParamString", description = "String Query 파라미터", example = "testString")
            @RequestParam(value = "queryParamString")
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull String queryParamString,
            @Parameter(name = "queryParamStringNullable", description = "String Query 파라미터 Nullable", example = "testString")
            @RequestParam(value = "queryParamStringNullable", required = false)
            @Nullable String queryParamStringNullable,
            @Parameter(name = "queryParamInt", description = "Int Query 파라미터", example = "1")
            @RequestParam(value = "queryParamInt")
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull Integer queryParamInt,
            @Parameter(name = "queryParamIntNullable", description = "Int Query 파라미터 Nullable", example = "1")
            @RequestParam(value = "queryParamIntNullable", required = false)
            @Nullable Integer queryParamIntNullable,
            @Parameter(name = "queryParamDouble", description = "Double Query 파라미터", example = "1.1")
            @RequestParam(value = "queryParamDouble")
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull Double queryParamDouble,
            @Parameter(name = "queryParamDoubleNullable", description = "Double Query 파라미터 Nullable", example = "1.1")
            @RequestParam(value = "queryParamDoubleNullable", required = false)
            @Nullable Double queryParamDoubleNullable,
            @Parameter(name = "queryParamBoolean", description = "Boolean Query 파라미터", example = "true")
            @RequestParam(value = "queryParamBoolean")
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull Boolean queryParamBoolean,
            @Parameter(name = "queryParamBooleanNullable", description = "Boolean Query 파라미터 Nullable", example = "true")
            @RequestParam(value = "queryParamBooleanNullable", required = false)
            @Nullable Boolean queryParamBooleanNullable,
            @Parameter(name = "queryParamStringList", description = "StringList Query 파라미터", example = "[\"testString1\", \"testString2\"]")
            @RequestParam(value = "queryParamStringList")
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


    // ----
    @Operation(
            summary = "Get 요청 테스트 (Path Parameter)",
            description = "Path Parameter 를 받는 Get 메소드 요청 테스트"
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
            path = {"/get-request/{pathParamInt}"},
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseBody
    public @Nullable GetRequestTestWithPathParamOutputVo getRequestTestWithPathParam(
            @Parameter(hidden = true)
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(name = "pathParamInt", description = "Int Path 파라미터", example = "1")
            @PathVariable(value = "pathParamInt")
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull Integer pathParamInt
    ) {
        return service.getRequestTestWithPathParam(
                httpServletResponse,
                pathParamInt
        );
    }

    @Data
    public static class GetRequestTestWithPathParamOutputVo {
        @Schema(description = "입력한 Int Path 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("pathParamInt")
        private final @NotNull Integer pathParamInt;
    }


    // ----
    @Operation(
            summary = "Post 요청 테스트 (application-json)",
            description = "application-json 형태의 Request Body 를 받는 Post 메소드 요청 테스트"
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
            path = {"/post-request-application-json"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable PostRequestTestWithApplicationJsonTypeRequestBodyOutputVo postRequestTestWithApplicationJsonTypeRequestBody(
            @Parameter(hidden = true)
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull HttpServletResponse httpServletResponse,
            @RequestBody
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull PostRequestTestWithApplicationJsonTypeRequestBodyInputVo inputVo
    ) {
        return service.postRequestTestWithApplicationJsonTypeRequestBody(
                httpServletResponse,
                inputVo
        );
    }

    @Data
    public static class PostRequestTestWithApplicationJsonTypeRequestBodyInputVo {
        @Schema(description = "String Body 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "testString")
        @JsonProperty("requestBodyString")
        private final @NotNull String requestBodyString;
        @Schema(description = "String Nullable Body 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "testString")
        @JsonProperty("requestBodyStringNullable")
        private final @Nullable String requestBodyStringNullable;
        @Schema(description = "Int Body 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("requestBodyInt")
        private final @NotNull Integer requestBodyInt;
        @Schema(description = "Int Nullable Body 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1")
        @JsonProperty("requestBodyIntNullable")
        private final @Nullable Integer requestBodyIntNullable;
        @Schema(description = "Double Body 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "1.1")
        @JsonProperty("requestBodyDouble")
        private final @NotNull Double requestBodyDouble;
        @Schema(description = "Double Nullable Body 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1.1")
        @JsonProperty("requestBodyDoubleNullable")
        private final @Nullable Double requestBodyDoubleNullable;
        @Schema(description = "Boolean Body 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
        @JsonProperty("requestBodyBoolean")
        private final @NotNull Boolean requestBodyBoolean;
        @Schema(description = "Boolean Nullable Body 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "true")
        @JsonProperty("requestBodyBooleanNullable")
        private final @Nullable Boolean requestBodyBooleanNullable;
        @Schema(description = "StringList Body 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"testString1\", \"testString2\"]")
        @JsonProperty("requestBodyStringList")
        private final @NotNull List<String> requestBodyStringList;
        @Schema(description = "StringList Nullable Body 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "[\"testString1\", \"testString2\"]")
        @JsonProperty("requestBodyStringListNullable")
        private final @Nullable List<String> requestBodyStringListNullable;
    }

    @Data
    public static class PostRequestTestWithApplicationJsonTypeRequestBodyOutputVo {
        @Schema(description = "입력한 String Body 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "testString")
        @JsonProperty("requestBodyString")
        private final @NotNull String requestBodyString;
        @Schema(description = "입력한 String Nullable Body 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "testString")
        @JsonProperty("requestBodyStringNullable")
        private final @Nullable String requestBodyStringNullable;
        @Schema(description = "입력한 Int Body 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("requestBodyInt")
        private final @NotNull Integer requestBodyInt;
        @Schema(description = "입력한 Int Nullable Body 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1")
        @JsonProperty("requestBodyIntNullable")
        private final @Nullable Integer requestBodyIntNullable;
        @Schema(description = "입력한 Double Body 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "1.1")
        @JsonProperty("requestBodyDouble")
        private final @NotNull Double requestBodyDouble;
        @Schema(description = "입력한 Double Nullable Body 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1.1")
        @JsonProperty("requestBodyDoubleNullable")
        private final @Nullable Double requestBodyDoubleNullable;
        @Schema(description = "입력한 Boolean Body 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
        @JsonProperty("requestBodyBoolean")
        private final @NotNull Boolean requestBodyBoolean;
        @Schema(description = "입력한 Boolean Nullable Body 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "true")
        @JsonProperty("requestBodyBooleanNullable")
        private final @Nullable Boolean requestBodyBooleanNullable;
        @Schema(description = "입력한 StringList Body 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"testString1\", \"testString2\"]")
        @JsonProperty("requestBodyStringList")
        private final @NotNull List<String> requestBodyStringList;
        @Schema(description = "입력한 StringList Nullable Body 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "[\"testString1\", \"testString2\"]")
        @JsonProperty("requestBodyStringListNullable")
        private final @Nullable List<String> requestBodyStringListNullable;
    }


    // ----
    @Operation(
            summary = "Post 요청 테스트 (application-json, 객체 파라미터 포함)",
            description = "application-json 형태의 Request Body(객체 파라미터 포함) 를 받는 Post 메소드 요청 테스트"
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
            path = {"/post-request-application-json-with-object-param"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable PostRequestTestWithApplicationJsonTypeRequestBody2OutputVo postRequestTestWithApplicationJsonTypeRequestBody2(
            @Parameter(hidden = true)
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull HttpServletResponse httpServletResponse,
            @RequestBody
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull PostRequestTestWithApplicationJsonTypeRequestBody2InputVo inputVo
    ) {
        return service.postRequestTestWithApplicationJsonTypeRequestBody2(
                httpServletResponse,
                inputVo
        );
    }

    @Data
    public static class PostRequestTestWithApplicationJsonTypeRequestBody2InputVo {
        @Schema(description = "객체 타입 파라미터", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("objectVo")
        private final @NotNull ObjectVo objectVo;
        @Schema(description = "객체 타입 리스트 파라미터", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("objectVoList")
        private final @NotNull List<ObjectVo> objectVoList;

        @Data
        public static class ObjectVo {
            @Schema(description = "String Body 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "testString")
            @JsonProperty("requestBodyString")
            private final @NotNull String requestBodyString;
            @Schema(description = "StringList Body 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"testString1\", \"testString2\"]")
            @JsonProperty("requestBodyStringList")
            private final @NotNull List<String> requestBodyStringList;
            @Schema(description = "서브 객체 타입 파라미터", requiredMode = Schema.RequiredMode.REQUIRED)
            @JsonProperty("subObjectVo")
            private final @NotNull SubObjectVo subObjectVo;
            @Schema(description = "서브 객체 타입 리스트 파라미터", requiredMode = Schema.RequiredMode.REQUIRED)
            @JsonProperty("subObjectVoList")
            private final @NotNull List<SubObjectVo> subObjectVoList;

            @Data
            public static class SubObjectVo {
                @Schema(description = "String Body 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "testString")
                @JsonProperty("requestBodyString")
                private final @NotNull String requestBodyString;
                @Schema(description = "StringList Body 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"testString1\", \"testString2\"]")
                @JsonProperty("requestBodyStringList")
                private final @NotNull List<String> requestBodyStringList;
            }
        }
    }

    @Data
    public static class PostRequestTestWithApplicationJsonTypeRequestBody2OutputVo {
        @Schema(description = "객체 타입 파라미터", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("objectVo")
        private final @NotNull ObjectVo objectVo;
        @Schema(description = "객체 타입 리스트 파라미터", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("objectVoList")
        private final @NotNull List<ObjectVo> objectVoList;

        @Data
        public static class ObjectVo {
            @Schema(description = "String Body 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "testString")
            @JsonProperty("requestBodyString")
            private final @NotNull String requestBodyString;
            @Schema(description = "StringList Body 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"testString1\", \"testString2\"]")
            @JsonProperty("requestBodyStringList")
            private final @NotNull List<String> requestBodyStringList;
            @Schema(description = "서브 객체 타입 파라미터", requiredMode = Schema.RequiredMode.REQUIRED)
            @JsonProperty("subObjectVo")
            private final @NotNull ObjectVo.SubObjectVo subObjectVo;
            @Schema(description = "서브 객체 타입 리스트 파라미터", requiredMode = Schema.RequiredMode.REQUIRED)
            @JsonProperty("subObjectVoList")
            private final @NotNull List<ObjectVo.SubObjectVo> subObjectVoList;

            @Data
            public static class SubObjectVo {
                @Schema(description = "String Body 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "testString")
                @JsonProperty("requestBodyString")
                private final @NotNull String requestBodyString;
                @Schema(description = "StringList Body 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"testString1\", \"testString2\"]")
                @JsonProperty("requestBodyStringList")
                private final @NotNull List<String> requestBodyStringList;
            }
        }
    }


    // ----
    @Operation(
            summary = "Post 요청 테스트 (입출력값 없음)",
            description = "입출력값이 없는 Post 메소드 요청 테스트"
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
            path = {"/post-request-application-json-with-no-param"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.ALL_VALUE
    )
    @ResponseBody
    public void postRequestTestWithNoInputAndOutput(
            @Parameter(hidden = true)
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull HttpServletResponse httpServletResponse
    ) {
        service.postRequestTestWithNoInputAndOutput(
                httpServletResponse
        );
    }


    // ----
    @Operation(
            summary = "Post 요청 테스트 (x-www-form-urlencoded)",
            description = "x-www-form-urlencoded 형태의 Request Body 를 받는 Post 메소드 요청 테스트"
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
            path = {"/post-request-x-www-form-urlencoded"},
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable PostRequestTestWithFormTypeRequestBodyOutputVo postRequestTestWithFormTypeRequestBody(
            @Parameter(hidden = true)
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull HttpServletResponse httpServletResponse,
            @ModelAttribute
            @RequestBody
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull PostRequestTestWithFormTypeRequestBodyInputVo inputVo
    ) {
        return service.postRequestTestWithFormTypeRequestBody(
                httpServletResponse,
                inputVo
        );
    }

    @Data
    public static class PostRequestTestWithFormTypeRequestBodyInputVo {
        @Schema(description = "String Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "testString")
        @JsonProperty("requestFormString")
        private final @NotNull String requestFormString;
        @Schema(description = "String Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "testString")
        @JsonProperty("requestFormStringNullable")
        private final @Nullable String requestFormStringNullable;
        @Schema(description = "Int Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("requestFormInt")
        private final @NotNull Integer requestFormInt;
        @Schema(description = "Int Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1")
        @JsonProperty("requestFormIntNullable")
        private final @Nullable Integer requestFormIntNullable;
        @Schema(description = "Double Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "1.1")
        @JsonProperty("requestFormDouble")
        private final @NotNull Double requestFormDouble;
        @Schema(description = "Double Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1.1")
        @JsonProperty("requestFormDoubleNullable")
        private final @Nullable Double requestFormDoubleNullable;
        @Schema(description = "Boolean Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
        @JsonProperty("requestFormBoolean")
        private final @NotNull Boolean requestFormBoolean;
        @Schema(description = "Boolean Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "true")
        @JsonProperty("requestFormBooleanNullable")
        private final @Nullable Boolean requestFormBooleanNullable;
        @Schema(description = "StringList Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"testString1\", \"testString2\"]")
        @JsonProperty("requestFormStringList")
        private final @NotNull List<String> requestFormStringList;
        @Schema(description = "StringList Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "[\"testString1\", \"testString2\"]")
        @JsonProperty("requestFormStringListNullable")
        private final @Nullable List<String> requestFormStringListNullable;
    }

    @Data
    public static class PostRequestTestWithFormTypeRequestBodyOutputVo {
        @Schema(description = "입력한 String Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "testString")
        @JsonProperty("requestFormString")
        private final @NotNull String requestFormString;
        @Schema(description = "입력한 String Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "testString")
        @JsonProperty("requestFormStringNullable")
        private final @Nullable String requestFormStringNullable;
        @Schema(description = "입력한 Int Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("requestFormInt")
        private final @NotNull Integer requestFormInt;
        @Schema(description = "입력한 Int Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1")
        @JsonProperty("requestFormIntNullable")
        private final @Nullable Integer requestFormIntNullable;
        @Schema(description = "입력한 Double Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "1.1")
        @JsonProperty("requestFormDouble")
        private final @NotNull Double requestFormDouble;
        @Schema(description = "입력한 Double Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1.1")
        @JsonProperty("requestFormDoubleNullable")
        private final @Nullable Double requestFormDoubleNullable;
        @Schema(description = "입력한 Boolean Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
        @JsonProperty("requestFormBoolean")
        private final @NotNull Boolean requestFormBoolean;
        @Schema(description = "입력한 Boolean Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "true")
        @JsonProperty("requestFormBooleanNullable")
        private final @Nullable Boolean requestFormBooleanNullable;
        @Schema(description = "입력한 StringList Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"testString1\", \"testString2\"]")
        @JsonProperty("requestFormStringList")
        private final @NotNull List<String> requestFormStringList;
        @Schema(description = "입력한 StringList Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "[\"testString1\", \"testString2\"]")
        @JsonProperty("requestFormStringListNullable")
        private final @Nullable List<String> requestFormStringListNullable;
    }


    // ----
    @Operation(
            summary = "Post 요청 테스트 (multipart/form-data)",
            description = "multipart/form-data 형태의 Request Body 를 받는 Post 메소드 요청 테스트<br>" +
                    "MultipartFile 파라미터가 null 이 아니라면 저장"
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
            path = {"/post-request-multipart-form-data"},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable PostRequestTestWithMultipartFormTypeRequestBodyOutputVo postRequestTestWithMultipartFormTypeRequestBody(
            @Parameter(hidden = true)
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull HttpServletResponse httpServletResponse,
            @ModelAttribute
            @RequestBody
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull PostRequestTestWithMultipartFormTypeRequestBodyInputVo inputVo
    ) {
        return service.postRequestTestWithMultipartFormTypeRequestBody(
                httpServletResponse,
                inputVo
        );
    }

    @Data
    public static class PostRequestTestWithMultipartFormTypeRequestBodyInputVo {
        @Schema(description = "String Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "testString")
        @JsonProperty("requestFormString")
        private final @NotNull String requestFormString;
        @Schema(description = "String Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "testString")
        @JsonProperty("requestFormStringNullable")
        private final @Nullable String requestFormStringNullable;
        @Schema(description = "Int Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("requestFormInt")
        private final @NotNull Integer requestFormInt;
        @Schema(description = "Int Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1")
        @JsonProperty("requestFormIntNullable")
        private final @Nullable Integer requestFormIntNullable;
        @Schema(description = "Double Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "1.1")
        @JsonProperty("requestFormDouble")
        private final @NotNull Double requestFormDouble;
        @Schema(description = "Double Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1.1")
        @JsonProperty("requestFormDoubleNullable")
        private final @Nullable Double requestFormDoubleNullable;
        @Schema(description = "Boolean Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
        @JsonProperty("requestFormBoolean")
        private final @NotNull Boolean requestFormBoolean;
        @Schema(description = "Boolean Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "true")
        @JsonProperty("requestFormBooleanNullable")
        private final @Nullable Boolean requestFormBooleanNullable;
        @Schema(description = "StringList Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"testString1\", \"testString2\"]")
        @JsonProperty("requestFormStringList")
        private final @NotNull List<String> requestFormStringList;
        @Schema(description = "StringList Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "[\"testString1\", \"testString2\"]")
        @JsonProperty("requestFormStringListNullable")
        private final @Nullable List<String> requestFormStringListNullable;
        @Schema(description = "멀티 파트 파일", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("multipartFile")
        private final @NotNull MultipartFile multipartFile;
        @Schema(description = "멀티 파트 파일 Nullable", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @JsonProperty("multipartFileNullable")
        private final @Nullable MultipartFile multipartFileNullable;
    }

    @Data
    public static class PostRequestTestWithMultipartFormTypeRequestBodyOutputVo {
        @Schema(description = "입력한 String Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "testString")
        @JsonProperty("requestFormString")
        private final @NotNull String requestFormString;
        @Schema(description = "입력한 String Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "testString")
        @JsonProperty("requestFormStringNullable")
        private final @Nullable String requestFormStringNullable;
        @Schema(description = "입력한 Int Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("requestFormInt")
        private final @NotNull Integer requestFormInt;
        @Schema(description = "입력한 Int Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1")
        @JsonProperty("requestFormIntNullable")
        private final @Nullable Integer requestFormIntNullable;
        @Schema(description = "입력한 Double Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "1.1")
        @JsonProperty("requestFormDouble")
        private final @NotNull Double requestFormDouble;
        @Schema(description = "입력한 Double Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1.1")
        @JsonProperty("requestFormDoubleNullable")
        private final @Nullable Double requestFormDoubleNullable;
        @Schema(description = "입력한 Boolean Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
        @JsonProperty("requestFormBoolean")
        private final @NotNull Boolean requestFormBoolean;
        @Schema(description = "입력한 Boolean Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "true")
        @JsonProperty("requestFormBooleanNullable")
        private final @Nullable Boolean requestFormBooleanNullable;
        @Schema(description = "입력한 StringList Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"testString1\", \"testString2\"]")
        @JsonProperty("requestFormStringList")
        private final @NotNull List<String> requestFormStringList;
        @Schema(description = "입력한 StringList Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "[\"testString1\", \"testString2\"]")
        @JsonProperty("requestFormStringListNullable")
        private final @Nullable List<String> requestFormStringListNullable;
    }


    // ----
    @Operation(
            summary = "Post 요청 테스트2 (multipart/form-data)",
            description = "multipart/form-data 형태의 Request Body 를 받는 Post 메소드 요청 테스트(Multipart File List)<br>" +
                    "파일 리스트가 null 이 아니라면 저장"
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
            path = {"/post-request-multipart-form-data2"},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable PostRequestTestWithMultipartFormTypeRequestBody2OutputVo postRequestTestWithMultipartFormTypeRequestBody2(
            @Parameter(hidden = true)
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull HttpServletResponse httpServletResponse,
            @ModelAttribute
            @RequestBody
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull PostRequestTestWithMultipartFormTypeRequestBody2InputVo inputVo
    ) throws IOException {
        return service.postRequestTestWithMultipartFormTypeRequestBody2(
                httpServletResponse,
                inputVo
        );
    }

    @Data
    public static class PostRequestTestWithMultipartFormTypeRequestBody2InputVo {
        @Schema(description = "String Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "testString")
        @JsonProperty("requestFormString")
        private final @NotNull String requestFormString;
        @Schema(description = "String Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "testString")
        @JsonProperty("requestFormStringNullable")
        private final @Nullable String requestFormStringNullable;
        @Schema(description = "Int Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("requestFormInt")
        private final @NotNull Integer requestFormInt;
        @Schema(description = "Int Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1")
        @JsonProperty("requestFormIntNullable")
        private final @Nullable Integer requestFormIntNullable;
        @Schema(description = "Double Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "1.1")
        @JsonProperty("requestFormDouble")
        private final @NotNull Double requestFormDouble;
        @Schema(description = "Double Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1.1")
        @JsonProperty("requestFormDoubleNullable")
        private final @Nullable Double requestFormDoubleNullable;
        @Schema(description = "Boolean Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
        @JsonProperty("requestFormBoolean")
        private final @NotNull Boolean requestFormBoolean;
        @Schema(description = "Boolean Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "true")
        @JsonProperty("requestFormBooleanNullable")
        private final @Nullable Boolean requestFormBooleanNullable;
        @Schema(description = "StringList Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"testString1\", \"testString2\"]")
        @JsonProperty("requestFormStringList")
        private final @NotNull List<String> requestFormStringList;
        @Schema(description = "StringList Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "[\"testString1\", \"testString2\"]")
        @JsonProperty("requestFormStringListNullable")
        private final @Nullable List<String> requestFormStringListNullable;
        @Schema(description = "멀티 파트 파일", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("multipartFileList")
        private final @NotNull List<MultipartFile> multipartFileList;
        @Schema(description = "멀티 파트 파일 Nullable", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @JsonProperty("multipartFileNullableList")
        private final @Nullable List<MultipartFile> multipartFileNullableList;
    }

    @Data
    public static class PostRequestTestWithMultipartFormTypeRequestBody2OutputVo {
        @Schema(description = "입력한 String Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "testString")
        @JsonProperty("requestFormString")
        private final @NotNull String requestFormString;
        @Schema(description = "입력한 String Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "testString")
        @JsonProperty("requestFormStringNullable")
        private final @Nullable String requestFormStringNullable;
        @Schema(description = "입력한 Int Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("requestFormInt")
        private final @NotNull Integer requestFormInt;
        @Schema(description = "입력한 Int Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1")
        @JsonProperty("requestFormIntNullable")
        private final @Nullable Integer requestFormIntNullable;
        @Schema(description = "입력한 Double Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "1.1")
        @JsonProperty("requestFormDouble")
        private final @NotNull Double requestFormDouble;
        @Schema(description = "입력한 Double Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1.1")
        @JsonProperty("requestFormDoubleNullable")
        private final @Nullable Double requestFormDoubleNullable;
        @Schema(description = "입력한 Boolean Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
        @JsonProperty("requestFormBoolean")
        private final @NotNull Boolean requestFormBoolean;
        @Schema(description = "입력한 Boolean Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "true")
        @JsonProperty("requestFormBooleanNullable")
        private final @Nullable Boolean requestFormBooleanNullable;
        @Schema(description = "입력한 StringList Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"testString1\", \"testString2\"]")
        @JsonProperty("requestFormStringList")
        private final @NotNull List<String> requestFormStringList;
        @Schema(description = "입력한 StringList Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "[\"testString1\", \"testString2\"]")
        @JsonProperty("requestFormStringListNullable")
        private final @Nullable List<String> requestFormStringListNullable;
    }


    // ----
    @Operation(
            summary = "Post 요청 테스트 (multipart/form-data - JsonString)",
            description = "multipart/form-data 형태의 Request Body 를 받는 Post 메소드 요청 테스트<br>" +
                    "Form Data 의 Input Body 에는 Object 리스트 타입은 사용 불가능입니다.<br>" +
                    "Object 리스트 타입을 사용한다면, Json String 타입으로 객체를 받아서 파싱하여 사용하는 방식을 사용합니다.<br>" +
                    "아래 예시에서는 모두 JsonString 형식으로 만들었지만, ObjectList 타입만 이런식으로 처리하세요."
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
            path = {"/post-request-multipart-form-data-json"},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable PostRequestTestWithMultipartFormTypeRequestBody3OutputVo postRequestTestWithMultipartFormTypeRequestBody3(
            @Parameter(hidden = true)
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull HttpServletResponse httpServletResponse,
            @ModelAttribute
            @RequestBody
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull PostRequestTestWithMultipartFormTypeRequestBody3InputVo inputVo
    ) {
        return service.postRequestTestWithMultipartFormTypeRequestBody3(
                httpServletResponse,
                inputVo
        );
    }

    @Data
    public static class PostRequestTestWithMultipartFormTypeRequestBody3InputVo {
        @Schema(
                description = "json 형식의 문자열<br>" +
                        """
                                            data class InputJsonObject(<br>
                                            @Schema(description = "String Form 파라미터", required = true, example = "testString")<br>
                                            @JsonProperty("requestFormString")<br>
                                            val requestFormString: String,<br>
                                            @Schema(description = "String Nullable Form 파라미터", required = false, example = "testString")<br>
                                            @JsonProperty("requestFormStringNullable")<br>
                                            val requestFormStringNullable: String?,<br>
                                            @Schema(description = "Int Form 파라미터", required = true, example = "1")<br>
                                            @JsonProperty("requestFormInt")<br>
                                            val requestFormInt: Int,<br>
                                            @Schema(description = "Int Nullable Form 파라미터", required = false, example = "1")<br>
                                            @JsonProperty("requestFormIntNullable")<br>
                                            val requestFormIntNullable: Int?,<br>
                                            @Schema(description = "Double Form 파라미터", required = true, example = "1.1")<br>
                                            @JsonProperty("requestFormDouble")<br>
                                            val requestFormDouble: Double,<br>
                                            @Schema(description = "Double Nullable Form 파라미터", required = false, example = "1.1")<br>
                                            @JsonProperty("requestFormDoubleNullable")<br>
                                            val requestFormDoubleNullable: Double?,<br>
                                            @Schema(description = "Boolean Form 파라미터", required = true, example = "true")<br>
                                            @JsonProperty("requestFormBoolean")<br>
                                            val requestFormBoolean: Boolean,<br>
                                            @Schema(description = "Boolean Nullable Form 파라미터", required = false, example = "true")<br>
                                            @JsonProperty("requestFormBooleanNullable")<br>
                                            val requestFormBooleanNullable: Boolean?,<br>
                                            @Schema(description = "StringList Form 파라미터", required = true, example = "["testString1", "testString2"]")<br>
                                            @JsonProperty("requestFormStringList")<br>
                                            val requestFormStringList: List<String>,<br>
                                            @Schema(<br>
                                                description = "StringList Nullable Form 파라미터",<br>
                                                required = false,<br>
                                                example = "["testString1", "testString2"]"<br>
                                            )<br>
                                            @JsonProperty("requestFormStringListNullable")<br>
                                            val requestFormStringListNullable: List<String>?<br>
                                        )
                                """,
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = """
                        {
                          "requestFormString": "testString",
                          "requestFormStringNullable": null,
                          "requestFormInt": 1,
                          "requestFormIntNullable": null,
                          "requestFormDouble": 1.1,
                          "requestFormDoubleNullable": null,
                          "requestFormBoolean": true,
                          "requestFormBooleanNullable": null,
                          "requestFormStringList": [
                            "testString1",
                            "testString2"
                          ],
                          "requestFormStringListNullable": null
                        }
                        """
        )
        @JsonProperty("jsonString")
        private final @NotNull String jsonString;
        @Schema(description = "멀티 파트 파일", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("multipartFile")
        private final @NotNull MultipartFile multipartFile;
        @Schema(description = "멀티 파트 파일 Nullable", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @JsonProperty("multipartFileNullable")
        private final @Nullable MultipartFile multipartFileNullable;

        @Schema(description = "Json String Object")
        @Data
        public static class InputJsonObject {
            @Schema(description = "String Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "testString")
            @JsonProperty("requestFormString")
            private final @NotNull String requestFormString;
            @Schema(description = "String Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "testString")
            @JsonProperty("requestFormStringNullable")
            private final @Nullable String requestFormStringNullable;
            @Schema(description = "Int Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
            @JsonProperty("requestFormInt")
            private final @NotNull Integer requestFormInt;
            @Schema(description = "Int Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1")
            @JsonProperty("requestFormIntNullable")
            private final @Nullable Integer requestFormIntNullable;
            @Schema(description = "Double Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "1.1")
            @JsonProperty("requestFormDouble")
            private final @NotNull Double requestFormDouble;
            @Schema(description = "Double Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1.1")
            @JsonProperty("requestFormDoubleNullable")
            private final @Nullable Double requestFormDoubleNullable;
            @Schema(description = "Boolean Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
            @JsonProperty("requestFormBoolean")
            private final @NotNull Boolean requestFormBoolean;
            @Schema(description = "Boolean Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "true")
            @JsonProperty("requestFormBooleanNullable")
            private final @Nullable Boolean requestFormBooleanNullable;
            @Schema(description = "StringList Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"testString1\", \"testString2\"]")
            @JsonProperty("requestFormStringList")
            private final @NotNull List<String> requestFormStringList;
            @Schema(description = "StringList Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "[\"testString1\", \"testString2\"]")
            @JsonProperty("requestFormStringListNullable")
            private final @Nullable List<String> requestFormStringListNullable;
            @Schema(description = "멀티 파트 파일", requiredMode = Schema.RequiredMode.REQUIRED)
            @JsonProperty("multipartFile")
            private final @NotNull MultipartFile multipartFile;
            @Schema(description = "멀티 파트 파일 Nullable", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
            @JsonProperty("multipartFileNullable")
            private final @Nullable MultipartFile multipartFileNullable;
        }
    }

    @Data
    public static class PostRequestTestWithMultipartFormTypeRequestBody3OutputVo {
        @Schema(description = "입력한 String Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "testString")
        @JsonProperty("requestFormString")
        private final @NotNull String requestFormString;
        @Schema(description = "입력한 String Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "testString")
        @JsonProperty("requestFormStringNullable")
        private final @Nullable String requestFormStringNullable;
        @Schema(description = "입력한 Int Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("requestFormInt")
        private final @NotNull Integer requestFormInt;
        @Schema(description = "입력한 Int Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1")
        @JsonProperty("requestFormIntNullable")
        private final @Nullable Integer requestFormIntNullable;
        @Schema(description = "입력한 Double Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "1.1")
        @JsonProperty("requestFormDouble")
        private final @NotNull Double requestFormDouble;
        @Schema(description = "입력한 Double Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1.1")
        @JsonProperty("requestFormDoubleNullable")
        private final @Nullable Double requestFormDoubleNullable;
        @Schema(description = "입력한 Boolean Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
        @JsonProperty("requestFormBoolean")
        private final @NotNull Boolean requestFormBoolean;
        @Schema(description = "입력한 Boolean Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "true")
        @JsonProperty("requestFormBooleanNullable")
        private final @Nullable Boolean requestFormBooleanNullable;
        @Schema(description = "입력한 StringList Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"testString1\", \"testString2\"]")
        @JsonProperty("requestFormStringList")
        private final @NotNull List<String> requestFormStringList;
        @Schema(description = "입력한 StringList Nullable Form 파라미터", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "[\"testString1\", \"testString2\"]")
        @JsonProperty("requestFormStringListNullable")
        private final @Nullable List<String> requestFormStringListNullable;
    }


    // ----
    @Operation(
            summary = "Post 요청 테스트 (multipart/form-data - ObjectList)",
            description = "multipart/form-data 형태의 Request Body 를 받는 Post 메소드 요청 테스트<br>" +
                    "Form Data 의 Input Body 에는 Object 리스트 타입은 Swagger 테스터에서는 사용 불가능입니다.<br>" +
                    "테스트 시에는 postman 과 같은 테스터에서 inputObjectList[0].requestFormString 이렇게 하여 값을 입력하면 됩니다."
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
            path = {"/post-request-multipart-form-data-object-list"},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable PostRequestTestWithMultipartFormTypeRequestBody4OutputVo postRequestTestWithMultipartFormTypeRequestBody4(
            @Parameter(hidden = true)
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull HttpServletResponse httpServletResponse,
            @ModelAttribute
            @RequestBody
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull PostRequestTestWithMultipartFormTypeRequestBody4InputVo inputVo
    ) {
        return service.postRequestTestWithMultipartFormTypeRequestBody4(
                httpServletResponse,
                inputVo
        );
    }

    @Data
    public static class PostRequestTestWithMultipartFormTypeRequestBody4InputVo {
        @Schema(description = "Object List", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("inputObjectList")
        private final @NotNull List<InputObject> inputObjectList;
        @Schema(description = "멀티 파트 파일", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("multipartFile")
        private final @NotNull MultipartFile multipartFile;
        @Schema(description = "멀티 파트 파일 Nullable", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @JsonProperty("multipartFileNullable")
        private final @Nullable MultipartFile multipartFileNullable;

        @Schema(description = "InputObject")
        @Data
        public static class InputObject {
            @Schema(description = "입력한 String Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "testString")
            @JsonProperty("requestFormString")
            private final @NotNull String requestFormString;
            @Schema(description = "입력한 Int Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
            @JsonProperty("requestFormInt")
            private final @NotNull Integer requestFormInt;
        }
    }

    @Data
    public static class PostRequestTestWithMultipartFormTypeRequestBody4OutputVo {
        @Schema(description = "Object List", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @JsonProperty("inputObjectList")
        private final @Nullable List<InputObject> inputObjectList;

        @Schema(description = "InputObject")
        @Data
        public static class InputObject {
            @Schema(description = "입력한 String Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "testString")
            @JsonProperty("requestFormString")
            private final @NotNull String requestFormString;
            @Schema(description = "입력한 Int Form 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
            @JsonProperty("requestFormInt")
            private final @NotNull Integer requestFormInt;
        }
    }


    // ----
    @Operation(
            summary = "인위적 에러 발생 테스트",
            description = "요청 받으면 인위적인 서버 에러를 발생시킵니다.(Http Response Status 500)"
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
            path = {"/generate-error"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.ALL_VALUE
    )
    @ResponseBody
    public void generateErrorTest(
            @Parameter(hidden = true)
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull HttpServletResponse httpServletResponse
    ) {
        service.generateErrorTest(
                httpServletResponse
        );
    }


    // ----
    @Operation(
            summary = "결과 코드 발생 테스트",
            description = "Response Header 에 api-result-code 를 반환하는 테스트 API"
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
                                                    "1 : errorType 을 A 로 보냈습니다.<br>" +
                                                    "2 : errorType 을 B 로 보냈습니다.<br>" +
                                                    "3 : errorType 을 C 로 보냈습니다.",
                                            schema = @Schema(type = "string")
                                    )
                            }
                    )
            }
    )
    @PostMapping(
            path = {"/api-result-code-test"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.ALL_VALUE
    )
    @ResponseBody
    public void returnResultCodeThroughHeaders(
            @Parameter(hidden = true)
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull HttpServletResponse httpServletResponse,

            @Parameter(name = "errorType", description = "정상적이지 않은 상황을 만들도록 가정된 변수입니다.", example = "A")
            @RequestParam(value = "errorType", required = false)
            @Nullable ReturnResultCodeThroughHeadersErrorTypeEnum errorType
    ) {
        service.returnResultCodeThroughHeaders(
                httpServletResponse,
                errorType
        );
    }

    public enum ReturnResultCodeThroughHeadersErrorTypeEnum {
        A, B, C
    }


    // ----
    @Operation(
            summary = "인위적 응답 지연 테스트",
            description = "임의로 응답 시간을 지연시킵니다."
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
            path = {"/time-delay-test"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.ALL_VALUE
    )
    @ResponseBody
    public void responseDelayTest(
            @Parameter(hidden = true)
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(name = "delayTimeSec", description = "지연 시간(초)", example = "1")
            @RequestParam(value = "delayTimeSec")
            @NotNull Long delayTimeSec
    ) {
        service.responseDelayTest(
                httpServletResponse,
                delayTimeSec
        );
    }


    // ----
    // UTF-8 설정을 적용하려면,
    // produces = ["text/plain;charset=utf-8"]
    // produces = ["text/html;charset=utf-8"]
    @Operation(
            summary = "text/string 반환 샘플",
            description = "text/string 형식의 Response Body 를 반환합니다."
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
            path = {"/return-text-string"},
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.TEXT_PLAIN_VALUE}
    )
    @ResponseBody
    public @Nullable String returnTextStringTest(
            @Parameter(hidden = true)
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull HttpServletResponse httpServletResponse
    ) {
        return service.returnTextStringTest(httpServletResponse);
    }


    // ----
    @Operation(
            summary = "text/html 반환 샘플",
            description = "text/html 형식의 Response Body 를 반환합니다."
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
            path = {"/return-text-html"},
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.TEXT_HTML_VALUE}
    )
    public @Nullable ModelAndView returnTextHtmlTest(
            @Parameter(hidden = true)
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull HttpServletResponse httpServletResponse
    ) {
        return service.returnTextHtmlTest(httpServletResponse);
    }


    // ----
    @Operation(
            summary = "byte 반환 샘플",
            description = " byte array('a', .. , 'f') 에서 아래와 같은 요청으로 원하는 바이트를 요청 가능<br>" +
                    "    >> curl http://localhost:8080/my-service/tk/sample/request-test/byte -i -H \"Range: bytes=2-4\""
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
            path = {"/byte"},
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.TEXT_PLAIN_VALUE}
    )
    @ResponseBody
    public @Nullable Resource returnByteDataTest(
            @Parameter(hidden = true)
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(name = "Range", description = "byte array('a', 'b', 'c', 'd', 'e', 'f') 중 가져올 범위(0 부터 시작되는 인덱스)", example = "Bytes=2-4")
            @RequestHeader(value = "Range")
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull String range
    ) {
        return service.returnByteDataTest(
                httpServletResponse
        );
    }


    // ----
    @Operation(
            summary = "비디오 스트리밍 샘플",
            description = "비디오 스트리밍 샘플<br>" +
                    "테스트는 프로젝트 파일 경로의 external_files/files_for_api_test/html_file_sample 안의 video-streaming.html 파일을 사용하세요."
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
            path = {"/video-streaming"},
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE}
    )
    @ResponseBody
    public @Nullable Resource videoStreamingTest(
            @Parameter(hidden = true)
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull HttpServletResponse httpServletResponse,

            @Parameter(name = "videoHeight", description = "비디오 높이", example = "H240")
            @RequestParam(value = "videoHeight")
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull VideoStreamingTestVideoHeight videoHeight
    ) throws IOException {
        return service.videoStreamingTest(
                httpServletResponse,
                videoHeight
        );
    }

    public enum VideoStreamingTestVideoHeight {
        H240,
        H360,
        H480,
        H720
    }


    // ----
    @Operation(
            summary = "오디오 스트리밍 샘플",
            description = "오디오 스트리밍 샘플<br>" +
                    "테스트는 프로젝트 파일 경로의 external_files/files_for_api_test/html_file_sample 안의 audio-streaming.html 파일을 사용하세요."
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
            path = {"/audio-streaming"},
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE}
    )
    @ResponseBody
    public @Nullable Resource audioStreamingTest(
            @Parameter(hidden = true)
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull HttpServletResponse httpServletResponse
    ) throws IOException {
        return service.audioStreamingTest(
                httpServletResponse
        );
    }


    // ----
    @Operation(
            summary = "비동기 처리 결과 반환 샘플",
            description = "API 호출시 함수 내에서 별도 스레드로 작업을 수행하고,<br>" +
                    "비동기 작업 완료 후 그 처리 결과가 반환됨"
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
            path = {"/async-result"},
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseBody
    public @Nullable DeferredResult<AsynchronousResponseTestOutputVo> asynchronousResponseTest(
            @Parameter(hidden = true)
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull HttpServletResponse httpServletResponse
    ) {
        return service.asynchronousResponseTest(
                httpServletResponse
        );
    }

    @Data
    public static class AsynchronousResponseTestOutputVo {
        @Schema(description = "결과 메세지", requiredMode = Schema.RequiredMode.REQUIRED, example = "n 초 경과 후 반환했습니다.")
        @JsonProperty("resultMessage")
        private final @NotNull String resultMessage;
    }
}
