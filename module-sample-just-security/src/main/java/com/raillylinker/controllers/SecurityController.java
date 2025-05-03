package com.raillylinker.controllers;

import com.raillylinker.services.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Tag(name = "/security APIs", description = "token 인증/인가 테스트 API 컨트롤러")
@Controller
@RequestMapping("/security")
@Validated
public class SecurityController {
    public SecurityController(
            @NotNull SecurityService service
    ) {
        this.service = service;
    }

    // <멤버 변수 공간>
    private final @NotNull SecurityService service;


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @Operation(
            summary = "비 로그인 접속 테스트",
            description = "비 로그인 접속 테스트용 API"
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
            path = {"/for-no-logged-in"},
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.TEXT_PLAIN_VALUE}
    )
    @ResponseBody
    public @Nullable String noLoggedInAccessTest(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse
    ) {
        return service.noLoggedInAccessTest(httpServletResponse);
    }


    // ----
    @Operation(
            summary = "로그인 진입 테스트 <>",
            description = "로그인 되어 있어야 진입 가능"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            content = {@Content},
                            description = "인증되지 않은 접근입니다."
                    )
            }
    )
    @GetMapping(
            path = {"/for-logged-in"},
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.TEXT_PLAIN_VALUE}
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public @Nullable String loggedInAccessTest(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(hidden = true)
            @RequestHeader(value = "Authorization", required = false)
            @Nullable String authorization
    ) throws Exception {
        return service.loggedInAccessTest(httpServletResponse, Objects.requireNonNull(authorization));
    }


    // ----
    @Operation(
            summary = "ADMIN 권한 진입 테스트 <'ADMIN'>",
            description = "ADMIN 권한이 있어야 진입 가능"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            content = {@Content},
                            description = "인증되지 않은 접근입니다."
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            content = {@Content},
                            description = "인가되지 않은 접근입니다."
                    )
            }
    )
    @GetMapping(
            path = {"/for-admin"},
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.TEXT_PLAIN_VALUE}
    )
    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_ADMIN'))")
    @ResponseBody
    public @Nullable String adminAccessTest(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(hidden = true)
            @RequestHeader(value = "Authorization", required = false)
            @Nullable String authorization
    ) throws Exception {
        return service.adminAccessTest(httpServletResponse, Objects.requireNonNull(authorization));
    }


    // ----
    @Operation(
            summary = "Developer 권한 진입 테스트 <'ADMIN' or 'Developer'>",
            description = "Developer 권한이 있어야 진입 가능"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "정상 동작"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            content = {@Content},
                            description = "인증되지 않은 접근입니다."
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            content = {@Content},
                            description = "인가되지 않은 접근입니다."
                    )
            }
    )
    @GetMapping(
            path = {"/for-developer"},
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.TEXT_PLAIN_VALUE}
    )
    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_DEVELOPER') or hasRole('ROLE_ADMIN'))")
    @ResponseBody
    public @Nullable String developerAccessTest(
            @Parameter(hidden = true)
            @NotNull HttpServletResponse httpServletResponse,
            @Parameter(hidden = true)
            @RequestHeader(value = "Authorization", required = false)
            @Nullable String authorization
    ) throws Exception {
        return service.developerAccessTest(httpServletResponse, Objects.requireNonNull(authorization));
    }
}
