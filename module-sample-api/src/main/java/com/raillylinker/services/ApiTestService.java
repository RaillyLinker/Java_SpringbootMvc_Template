package com.raillylinker.services;

import com.raillylinker.controllers.ApiTestController;
import com.raillylinker.util_components.CustomUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ApiTestService {
    public ApiTestService(
            // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
            @Value("${spring.profiles.active:default}")
            @NotNull String activeProfile,
            @NotNull CustomUtil customUtil
    ) {
        this.activeProfile = activeProfile;
        this.customUtil = customUtil;
    }

    // <멤버 변수 공간>
    private final @NotNull String activeProfile;

    private final @NotNull CustomUtil customUtil;

    private final @NotNull Logger classLogger = LoggerFactory.getLogger(RootService.class);

    // (스레드 풀)
    private final @NotNull ExecutorService executorService = Executors.newCachedThreadPool();


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    // (기본 요청 테스트 API)
    public @Nullable String basicRequestTest(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        httpServletResponse.setStatus(HttpStatus.OK.value());

        return activeProfile;
    }


    // ----
    // (요청 Redirect 테스트)
    public @Nullable ModelAndView redirectTest(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        @NotNull ModelAndView mv = new ModelAndView();
        mv.setViewName("redirect:/api-test");

        return mv;
    }


    // ----
    // (요청 Redirect 테스트)
    public @Nullable ModelAndView forwardTest(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        @NotNull ModelAndView mv = new ModelAndView();
        mv.setViewName("forward:/api-test");

        return mv;
    }


    // ----
    // (Get 요청 테스트 (Query Parameter))
    public @Nullable ApiTestController.GetRequestTestOutputVo getRequestTest(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull String queryParamString,
            @Nullable String queryParamStringNullable,
            @NotNull Integer queryParamInt,
            @Nullable Integer queryParamIntNullable,
            @NotNull Double queryParamDouble,
            @Nullable Double queryParamDoubleNullable,
            @NotNull Boolean queryParamBoolean,
            @Nullable Boolean queryParamBooleanNullable,
            @NotNull List<String> queryParamStringList,
            @Nullable List<String> queryParamStringListNullable
    ) {
        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new ApiTestController.GetRequestTestOutputVo(
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
}
