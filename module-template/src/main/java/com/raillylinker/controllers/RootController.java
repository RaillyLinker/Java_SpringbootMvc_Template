package com.raillylinker.controllers;

import com.raillylinker.services.RootService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.jetbrains.annotations.*;
import org.springframework.validation.annotation.Validated;

@Tag(name = "root APIs", description = "Root 경로에 대한 API 컨트롤러")
@Controller
@Validated
public class RootController {
    public RootController(
            @NotNull RootService service
    ) {
        this.service = service;
    }

    // <멤버 변수 공간>
    private final @NotNull RootService service;


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @Operation(
            summary = "루트 경로",
            description = "루트 경로 정보를 반환합니다."
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
            path = {"/", ""},
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.TEXT_HTML_VALUE}
    )
    public @Nullable ModelAndView getRootInfo(
            @Parameter(hidden = true)
            @jakarta.validation.Valid @jakarta.validation.constraints.NotNull
            @NotNull HttpServletResponse httpServletResponse
    ) {
        return service.getRootInfo(httpServletResponse);
    }
}
