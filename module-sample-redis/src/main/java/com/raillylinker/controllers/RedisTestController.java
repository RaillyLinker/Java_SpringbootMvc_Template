package com.raillylinker.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.raillylinker.services.RedisTestService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

// Key - Value 형식으로 저장되는 Redis Wrapper 를 사용하여 Database 의 Row 를 모사할 수 있으며,
// 이를 통해 데이터베이스 결과에 대한 캐싱을 구현할 수 있습니다.
/*
    !!!
    테스트를 하고 싶다면, 도커를 설치하고,
    cmd 를 열어,
    프로젝트 폴더 내의 external_files/docker/redis_docker 로 이동 후,
    명령어.txt 에 적힌 명령어를 입력하여 Redis 를 실행시킬 수 있습니다.
    !!!
 */
@Tag(name = "/redis-test APIs", description = "Redis 테스트 API 컨트롤러")
@Controller
@RequestMapping("/redis-test")
@Validated
public class RedisTestController {
    public RedisTestController(
            @NotNull RedisTestService service
    ) {
        this.service = service;
    }

    // <멤버 변수 공간>
    private final @NotNull RedisTestService service;


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    // todo


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
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(name = "queryParamString", description = "String Query 파라미터", example = "testString")
            @RequestParam(value = "queryParamString")
            @NotNull String queryParamString,
            @Parameter(name = "queryParamStringNullable", description = "String Query 파라미터 Nullable", example = "testString")
            @RequestParam(value = "queryParamStringNullable", required = false)
            @Nullable String queryParamStringNullable,
            @Parameter(name = "queryParamInt", description = "Int Query 파라미터", example = "1")
            @RequestParam(value = "queryParamInt")
            @NotNull Integer queryParamInt,
            @Parameter(name = "queryParamIntNullable", description = "Int Query 파라미터 Nullable", example = "1")
            @RequestParam(value = "queryParamIntNullable", required = false)
            @Nullable Integer queryParamIntNullable,
            @Parameter(name = "queryParamDouble", description = "Double Query 파라미터", example = "1.1")
            @RequestParam(value = "queryParamDouble")
            @NotNull Double queryParamDouble,
            @Parameter(name = "queryParamDoubleNullable", description = "Double Query 파라미터 Nullable", example = "1.1")
            @RequestParam(value = "queryParamDoubleNullable", required = false)
            @Nullable Double queryParamDoubleNullable,
            @Parameter(name = "queryParamBoolean", description = "Boolean Query 파라미터", example = "true")
            @RequestParam(value = "queryParamBoolean")
            @NotNull Boolean queryParamBoolean,
            @Parameter(name = "queryParamBooleanNullable", description = "Boolean Query 파라미터 Nullable", example = "true")
            @RequestParam(value = "queryParamBooleanNullable", required = false)
            @Nullable Boolean queryParamBooleanNullable,
            @Parameter(name = "queryParamStringList", description = "StringList Query 파라미터", example = "[\"testString1\", \"testString2\"]")
            @RequestParam(value = "queryParamStringList")
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
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(name = "pathParamInt", description = "Int Path 파라미터", example = "1")
            @PathVariable(value = "pathParamInt")
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
            @NotNull HttpServletResponse httpServletResponse,
            @RequestBody
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
            @NotNull HttpServletResponse httpServletResponse,
            @RequestBody
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
            @NotNull HttpServletResponse httpServletResponse,
            @ModelAttribute
            @RequestBody
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
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(name = "Range", description = "byte array('a', 'b', 'c', 'd', 'e', 'f') 중 가져올 범위(0 부터 시작되는 인덱스)", example = "Bytes=2-4")
            @RequestHeader(value = "Range")
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
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(name = "videoHeight", description = "비디오 높이", example = "H240")
            @RequestParam(value = "videoHeight")
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
            @NotNull HttpServletResponse httpServletResponse
    ) throws IOException {
        return service.audioStreamingTest(
                httpServletResponse
        );
    }


    // ----
    /*
         결론적으로 아래 파라미터는, Query Param 의 경우는 리스트 입력이 ?stringList=string&stringList=string 이런식이므로,
         리스트 파라미터가 Not NULL 이라면 빈 리스트를 보낼 수 없으며,
         Body Param 의 경우는 JSON 으로 "requestBodyStringList" : [] 이렇게 보내면 빈 리스트를 보낼 수 있습니다.
     */
    @Operation(
            summary = "빈 리스트 받기 테스트",
            description = "Query 파라미터에 NotNull List 와 Body 파라미터의 NotNull List 에 빈 리스트를 넣었을 때의 현상을 관측하기 위한 테스트"
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
            path = {"/empty-list-param-test"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable EmptyListRequestTestOutputVo emptyListRequestTest(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(name = "stringList", description = "String List Query 파라미터", example = "[\"testString1\", \"testString2\"]")
            @RequestParam(value = "stringList")
            @NotNull List<String> stringList,
            @RequestBody
            @NotNull EmptyListRequestTestInputVo inputVo
    ) {
        return service.emptyListRequestTest(
                httpServletResponse,
                stringList,
                inputVo
        );
    }

    @Data
    public static class EmptyListRequestTestInputVo {
        @Schema(description = "StringList Body 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"testString1\", \"testString2\"]")
        @JsonProperty("requestBodyStringList")
        private final @NotNull List<String> requestBodyStringList;
    }

    @Data
    public static class EmptyListRequestTestOutputVo {
        @Schema(description = "StringList Query 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"testString1\", \"testString2\"]")
        @JsonProperty("requestQueryStringList")
        private final @NotNull List<String> requestQueryStringList;
        @Schema(description = "입력한 StringList Body 파라미터", requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"testString1\", \"testString2\"]")
        @JsonProperty("requestBodyStringList")
        private final @NotNull List<String> requestBodyStringList;
    }


    // ----
    @Operation(
            summary = "by_product_files 폴더에서 파일 다운받기",
            description = "업로드 API 를 사용하여 by_product_files 로 업로드한 파일을 다운로드"
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
                                                    "1 : fileName 에 해당하는 파일이 존재하지 않습니다.",
                                            schema = @Schema(type = "string")
                                    )
                            }
                    )
            }
    )
    @GetMapping(
            path = {"/download-from-server/{fileName}"},
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE}
    )
    @ResponseBody
    public @Nullable ResponseEntity<Resource> fileDownloadTest(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(name = "fileName", description = "by_product_files/test 폴더 안의 파일명", example = "sample.txt")
            @PathVariable(value = "fileName")
            @NotNull String fileName
    ) throws IOException {
        return service.fileDownloadTest(
                httpServletResponse,
                fileName
        );
    }
}
