package com.raillylinker.controllers;

import com.raillylinker.services.ApiTestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
            path = "",
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
            path = "/redirect-to-blank",
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
            path = "/forward-to-blank",
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
}
