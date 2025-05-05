package com.raillylinker.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.raillylinker.services.Retrofit2TestService;
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
import org.jetbrains.annotations.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "/retrofit2-test APIs", description = "Retrofit2 테스트 API 컨트롤러")
@Controller
@RequestMapping("/retrofit2-test")
@Validated
public class Retrofit2TestController {
    public Retrofit2TestController(
            @NotNull Retrofit2TestService service
    ) {
        this.service = service;
    }

    // <멤버 변수 공간>
    private final @NotNull Retrofit2TestService service;


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @Operation(
            summary = "기본 요청 테스트",
            description = "기본적인 Get 메소드 요청 테스트입니다."
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
                                                    "1 : API 요청시 네트워크 에러가 발생하였습니다.<br>" +
                                                    "2 : API 요청시 서버 에러가 발생하였습니다.",
                                            schema = @Schema(type = "string")
                                    )
                            }
                    )
            }
    )
    @GetMapping(
            path = {"/request-test"},
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
            summary = "Redirect 테스트",
            description = "Redirect 되었을 때의 응답 테스트입니다."
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
                                                    "1 : API 요청시 네트워크 에러가 발생하였습니다.<br>" +
                                                    "2 : API 요청시 서버 에러가 발생하였습니다.",
                                            schema = @Schema(type = "string")
                                    )
                            }
                    )
            }
    )
    @GetMapping(
            path = {"/redirect-to-blank"},
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.TEXT_PLAIN_VALUE}
    )
    @ResponseBody
    public @Nullable String redirectTest(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse
    ) {
        return service.redirectTest(httpServletResponse);
    }


    // ----
    @Operation(
            summary = "Forward 테스트",
            description = "Forward 되었을 때의 응답 테스트입니다."
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
                                                    "1 : API 요청시 네트워크 에러가 발생하였습니다.<br>" +
                                                    "2 : API 요청시 서버 에러가 발생하였습니다.",
                                            schema = @Schema(type = "string")
                                    )
                            }
                    )
            }
    )
    @GetMapping(
            path = {"/forward-to-blank"},
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.TEXT_PLAIN_VALUE}
    )
    @ResponseBody
    public @Nullable String forwardTest(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse
    ) {
        return service.forwardTest(httpServletResponse);
    }


    // ----
    @Operation(
            summary = "Get 요청 테스트 (Query Parameter)",
            description = "Query 파라미터를 받는 Get 요청 테스트"
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
                                                    "1 : API 요청시 네트워크 에러가 발생하였습니다.<br>" +
                                                    "2 : API 요청시 서버 에러가 발생하였습니다.",
                                            schema = @Schema(type = "string")
                                    )
                            }
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
            @NotNull HttpServletResponse httpServletResponse
    ) {
        return service.getRequestTest(
                httpServletResponse
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
                                                    "1 : API 요청시 네트워크 에러가 발생하였습니다.<br>" +
                                                    "2 : API 요청시 서버 에러가 발생하였습니다.",
                                            schema = @Schema(type = "string")
                                    )
                            }
                    )
            }
    )
    @GetMapping(
            path = {"/get-request-path-param"},
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseBody
    public @Nullable GetRequestTestWithPathParamOutputVo getRequestTestWithPathParam(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse
    ) {
        return service.getRequestTestWithPathParam(
                httpServletResponse
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
            summary = "Post 요청 테스트 (Request Body, application/json)",
            description = "application/json 형식의 Request Body 를 받는 Post 요청 테스트"
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
                                                    "1 : API 요청시 네트워크 에러가 발생하였습니다.<br>" +
                                                    "2 : API 요청시 서버 에러가 발생하였습니다.",
                                            schema = @Schema(type = "string")
                                    )
                            }
                    )
            }
    )
    @PostMapping(
            path = {"/post-request-application-json"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable PostRequestTestWithApplicationJsonTypeRequestBodyOutputVo postRequestTestWithApplicationJsonTypeRequestBody(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse
    ) {
        return service.postRequestTestWithApplicationJsonTypeRequestBody(
                httpServletResponse
        );
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
            summary = "Post 요청 테스트 (Request Body, x-www-form-urlencoded)",
            description = "x-www-form-urlencoded 형식의 Request Body 를 받는 Post 요청 테스트"
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
                                                    "1 : API 요청시 네트워크 에러가 발생하였습니다.<br>" +
                                                    "2 : API 요청시 서버 에러가 발생하였습니다.",
                                            schema = @Schema(type = "string")
                                    )
                            }
                    )
            }
    )
    @PostMapping(
            path = {"/post-request-x-www-form-urlencoded"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable PostRequestTestWithFormTypeRequestBodyOutputVo postRequestTestWithFormTypeRequestBody(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse
    ) {
        return service.postRequestTestWithFormTypeRequestBody(
                httpServletResponse
        );
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
            summary = "Post 요청 테스트 (Request Body, multipart/form-data)",
            description = "multipart/form-data 형식의 Request Body 를 받는 Post 요청 테스트"
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
                                                    "1 : API 요청시 네트워크 에러가 발생하였습니다.<br>" +
                                                    "2 : API 요청시 서버 에러가 발생하였습니다.",
                                            schema = @Schema(type = "string")
                                    )
                            }
                    )
            }
    )
    @PostMapping(
            path = {"/post-request-multipart-form-data"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable PostRequestTestWithMultipartFormTypeRequestBodyOutputVo postRequestTestWithMultipartFormTypeRequestBody(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse
    ) {
        return service.postRequestTestWithMultipartFormTypeRequestBody(
                httpServletResponse
        );
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
            summary = "Post 요청 테스트 (Request Body, multipart/form-data, MultipartFile List)",
            description = "multipart/form-data 형식의 Request Body 를 받는 Post 요청 테스트<br>" +
                    "MultipartFile 파라미터를 List 로 받습니다."
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
                                                    "1 : API 요청시 네트워크 에러가 발생하였습니다.<br>" +
                                                    "2 : API 요청시 서버 에러가 발생하였습니다.",
                                            schema = @Schema(type = "string")
                                    )
                            }
                    )
            }
    )
    @PostMapping(
            path = {"/post-request-multipart-form-data2"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable PostRequestTestWithMultipartFormTypeRequestBody2OutputVo postRequestTestWithMultipartFormTypeRequestBody2(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse
    ) {
        return service.postRequestTestWithMultipartFormTypeRequestBody2(
                httpServletResponse
        );
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
            summary = "Post 요청 테스트 (Request Body, multipart/form-data, with jsonString)",
            description = "multipart/form-data 형식의 Request Body 를 받는 Post 요청 테스트<br>" +
                    "파일 외의 파라미터를 JsonString 형식으로 받습니다."
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
                                                    "1 : API 요청시 네트워크 에러가 발생하였습니다.<br>" +
                                                    "2 : API 요청시 서버 에러가 발생하였습니다.",
                                            schema = @Schema(type = "string")
                                    )
                            }
                    )
            }
    )
    @PostMapping(
            path = {"/post-request-multipart-form-data-json"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public @Nullable PostRequestTestWithMultipartFormTypeRequestBody3OutputVo postRequestTestWithMultipartFormTypeRequestBody3(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse
    ) {
        return service.postRequestTestWithMultipartFormTypeRequestBody3(
                httpServletResponse
        );
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
            summary = "에러 발생 테스트",
            description = "요청시 에러가 발생했을 때의 테스트입니다."
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
                                                    "1 : API 요청시 네트워크 에러가 발생하였습니다.<br>" +
                                                    "2 : API 요청시 서버 에러가 발생하였습니다.",
                                            schema = @Schema(type = "string")
                                    )
                            }
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
            summary = "api-result-code 반환 테스트",
            description = "api-result-code 가 Response Header 로 반환되는 테스트입니다."
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
                                                    "1 : 네트워크 에러<br>" +
                                                    "2 : 서버 에러<br>" +
                                                    "3 : errorType 을 A 로 보냈습니다.<br>" +
                                                    "4 : errorType 을 B 로 보냈습니다.<br>" +
                                                    "5 : errorType 을 C 로 보냈습니다.",
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
            @NotNull HttpServletResponse httpServletResponse
    ) {
        service.returnResultCodeThroughHeaders(
                httpServletResponse
        );
    }


    // ----
    @Operation(
            summary = "응답 지연 발생 테스트",
            description = "요청을 보내어 인위적으로 응답이 지연 되었을 때를 테스트합니다."
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
                                                    "1 : API 요청시 네트워크 에러가 발생하였습니다.<br>" +
                                                    "2 : API 요청시 서버 에러가 발생하였습니다.",
                                            schema = @Schema(type = "string")
                                    )
                            }
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
    @Operation(
            summary = "text/string 형식 Response 받아오기",
            description = "text/string 형식 Response 를 받아옵니다."
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
                                                    "1 : API 요청시 네트워크 에러가 발생하였습니다.<br>" +
                                                    "2 : API 요청시 서버 에러가 발생하였습니다.",
                                            schema = @Schema(type = "string")
                                    )
                            }
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
            summary = "text/html 형식 Response 받아오기",
            description = "text/html 형식 Response 를 받아옵니다."
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
                                                    "1 : API 요청시 네트워크 에러가 발생하였습니다.<br>" +
                                                    "2 : API 요청시 서버 에러가 발생하였습니다.",
                                            schema = @Schema(type = "string")
                                    )
                            }
                    )
            }
    )
    @GetMapping(
            path = {"/return-text-html"},
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.TEXT_HTML_VALUE}
    )
    @ResponseBody
    public @Nullable String returnTextHtmlTest(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse
    ) {
        return service.returnTextHtmlTest(httpServletResponse);
    }


    // ----
    @Operation(
            summary = "DeferredResult Get 요청 테스트",
            description = "결과 반환 지연 Get 메소드 요청 테스트"
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
                                                    "1 : API 요청시 네트워크 에러가 발생하였습니다.<br>" +
                                                    "2 : API 요청시 서버 에러가 발생하였습니다.",
                                            schema = @Schema(type = "string")
                                    )
                            }
                    )
            }
    )
    @GetMapping(
            path = {"/async-result"},
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseBody
    public @Nullable AsynchronousResponseTestOutputVo asynchronousResponseTest(
            @Parameter(hidden = true)
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


    // todo
//    // ----
//    @Operation(
//            summary = "SSE 구독 테스트",
//            description = "SSE 구독 요청 테스트\n\n" +
//                    "SSE 를 구독하여 백그라운드에서 실행합니다.\n\n"
//    )
//    @ApiResponses(
//            value = {
//                    @ApiResponse(responseCode = "200", description = "정상 동작"),
//                    @ApiResponse(
//                            responseCode = "204",
//                            content = @Content(),
//                            description = "Response Body 가 없습니다.\n\nResponse Headers 를 확인하세요.",
//                            headers = {
//                                    @Header(
//                                            name = "api-result-code",
//                                            description = "(Response Code 반환 원인) - Required\n\n" +
//                                                    "1 : API 요청시 네트워크 에러가 발생하였습니다.\n\n" +
//                                                    "2 : API 요청시 서버 에러가 발생하였습니다.\n\n",
//                                            schema = @Schema(type = "string")
//                                    )
//                            }
//                    )
//            }
//    )
//    @GetMapping(
//            path = "/sse-subscribe",
//            consumes = MediaType.ALL_VALUE,
//            produces = MediaType.ALL_VALUE
//    )
//    @ResponseBody
//    public void sseSubscribeTest(
//            @Parameter(hidden = true)
//            HttpServletResponse httpServletResponse
//    ) {
//        service.sseSubscribeTest(httpServletResponse);
//    }


    // ----
    @Operation(
            summary = "WebSocket 연결 테스트",
            description = "WebSocket 연결 요청 테스트<br>" +
                    "WebSocket 을 연결 하여 백그라운드에서 실행합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "정상 동작"),
                    @ApiResponse(
                            responseCode = "204",
                            content = @Content(),
                            description = "Response Body 가 없습니다.<br>" +
                                    "Response Headers 를 확인하세요.",
                            headers = {
                                    @Header(
                                            name = "api-result-code",
                                            description = "(Response Code 반환 원인) - Required<br>" +
                                                    "1 : API 요청시 네트워크 에러가 발생하였습니다.<br>" +
                                                    "2 : API 요청시 서버 에러가 발생하였습니다.",
                                            schema = @Schema(type = "string")
                                    )
                            }
                    )
            }
    )
    @GetMapping(
            path = {"/websocket-connect"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.ALL_VALUE
    )
    @ResponseBody
    public void websocketConnectTest(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse
    ) {
        service.websocketConnectTest(httpServletResponse);
    }
}
