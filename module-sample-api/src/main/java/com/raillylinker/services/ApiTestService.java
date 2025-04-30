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

import java.util.ArrayList;
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


    // ----
    // (Get 요청 테스트 (Path Parameter))
    public @Nullable ApiTestController.GetRequestTestWithPathParamOutputVo getRequestTestWithPathParam(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull Integer pathParamInt
    ) {
        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new ApiTestController.GetRequestTestWithPathParamOutputVo(
                pathParamInt
        );
    }


    // ----
    // (Post 요청 테스트 (application-json))
    public @Nullable ApiTestController.PostRequestTestWithApplicationJsonTypeRequestBodyOutputVo postRequestTestWithApplicationJsonTypeRequestBody(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull ApiTestController.PostRequestTestWithApplicationJsonTypeRequestBodyInputVo inputVo
    ) {
        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new ApiTestController.PostRequestTestWithApplicationJsonTypeRequestBodyOutputVo(
                inputVo.getRequestBodyString(),
                inputVo.getRequestBodyStringNullable(),
                inputVo.getRequestBodyInt(),
                inputVo.getRequestBodyIntNullable(),
                inputVo.getRequestBodyDouble(),
                inputVo.getRequestBodyDoubleNullable(),
                inputVo.getRequestBodyBoolean(),
                inputVo.getRequestBodyBooleanNullable(),
                inputVo.getRequestBodyStringList(),
                inputVo.getRequestBodyStringListNullable()
        );
    }


    // ----
    // (Post 요청 테스트 (application-json, 객체 파라미터 포함))
    public @Nullable ApiTestController.PostRequestTestWithApplicationJsonTypeRequestBody2OutputVo postRequestTestWithApplicationJsonTypeRequestBody2(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull ApiTestController.PostRequestTestWithApplicationJsonTypeRequestBody2InputVo inputVo
    ) {
        @NotNull ArrayList<ApiTestController.PostRequestTestWithApplicationJsonTypeRequestBody2OutputVo.ObjectVo> objectList =
                new ArrayList<>();

        for (@NotNull ApiTestController.PostRequestTestWithApplicationJsonTypeRequestBody2InputVo.ObjectVo objectVo : inputVo.getObjectVoList()) {
            @NotNull ArrayList<ApiTestController.PostRequestTestWithApplicationJsonTypeRequestBody2OutputVo.ObjectVo.SubObjectVo> subObjectVoList =
                    new ArrayList<>();
            for (@NotNull ApiTestController.PostRequestTestWithApplicationJsonTypeRequestBody2InputVo.ObjectVo.SubObjectVo subObject : objectVo.getSubObjectVoList()) {
                subObjectVoList.add(
                        new ApiTestController.PostRequestTestWithApplicationJsonTypeRequestBody2OutputVo.ObjectVo.SubObjectVo(
                                subObject.getRequestBodyString(),
                                subObject.getRequestBodyStringList()
                        )
                );
            }

            objectList.add(
                    new ApiTestController.PostRequestTestWithApplicationJsonTypeRequestBody2OutputVo.ObjectVo(
                            objectVo.getRequestBodyString(),
                            objectVo.getRequestBodyStringList(),
                            new ApiTestController.PostRequestTestWithApplicationJsonTypeRequestBody2OutputVo.ObjectVo.SubObjectVo(
                                    objectVo.getSubObjectVo().getRequestBodyString(),
                                    objectVo.getSubObjectVo().getRequestBodyStringList()
                            ),
                            subObjectVoList
                    )
            );
        }

        @NotNull ArrayList<ApiTestController.PostRequestTestWithApplicationJsonTypeRequestBody2OutputVo.ObjectVo.SubObjectVo> subObjectVoList =
                new ArrayList<>();
        for (@NotNull ApiTestController.PostRequestTestWithApplicationJsonTypeRequestBody2InputVo.ObjectVo.SubObjectVo subObject : inputVo.getObjectVo().getSubObjectVoList()) {
            subObjectVoList.add(
                    new ApiTestController.PostRequestTestWithApplicationJsonTypeRequestBody2OutputVo.ObjectVo.SubObjectVo(
                            subObject.getRequestBodyString(),
                            subObject.getRequestBodyStringList()
                    )
            );
        }

        @NotNull ApiTestController.PostRequestTestWithApplicationJsonTypeRequestBody2OutputVo outputVo =
                new ApiTestController.PostRequestTestWithApplicationJsonTypeRequestBody2OutputVo(
                        new ApiTestController.PostRequestTestWithApplicationJsonTypeRequestBody2OutputVo.ObjectVo(
                                inputVo.getObjectVo().getRequestBodyString(),
                                inputVo.getObjectVo().getRequestBodyStringList(),
                                new ApiTestController.PostRequestTestWithApplicationJsonTypeRequestBody2OutputVo.ObjectVo.SubObjectVo(
                                        inputVo.getObjectVo().getSubObjectVo().getRequestBodyString(),
                                        inputVo.getObjectVo().getSubObjectVo().getRequestBodyStringList()
                                ),
                                subObjectVoList
                        ),
                        objectList
                );

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return outputVo;
    }
}
