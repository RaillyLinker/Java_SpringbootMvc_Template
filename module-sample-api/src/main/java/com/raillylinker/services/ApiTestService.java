package com.raillylinker.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.raillylinker.controllers.ApiTestController;
import com.raillylinker.util_components.CustomUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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


    // ----
    // (Post 요청 테스트 (입출력값 없음))
    public void postRequestTestWithNoInputAndOutput(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        httpServletResponse.setStatus(HttpStatus.OK.value());
    }


    // ----
    // (Post 요청 테스트 (x-www-form-urlencoded))
    public @Nullable ApiTestController.PostRequestTestWithFormTypeRequestBodyOutputVo postRequestTestWithFormTypeRequestBody(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull ApiTestController.PostRequestTestWithFormTypeRequestBodyInputVo inputVo
    ) {
        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new ApiTestController.PostRequestTestWithFormTypeRequestBodyOutputVo(
                inputVo.getRequestFormString(),
                inputVo.getRequestFormStringNullable(),
                inputVo.getRequestFormInt(),
                inputVo.getRequestFormIntNullable(),
                inputVo.getRequestFormDouble(),
                inputVo.getRequestFormDoubleNullable(),
                inputVo.getRequestFormBoolean(),
                inputVo.getRequestFormBooleanNullable(),
                inputVo.getRequestFormStringList(),
                inputVo.getRequestFormStringListNullable()
        );
    }


    // ----
    // (Post 요청 테스트 (multipart/form-data))
    public @Nullable ApiTestController.PostRequestTestWithMultipartFormTypeRequestBodyOutputVo postRequestTestWithMultipartFormTypeRequestBody(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull ApiTestController.PostRequestTestWithMultipartFormTypeRequestBodyInputVo inputVo
    ) {
        // 파일 저장 기본 디렉토리 경로
        @NotNull Path saveDirectoryPath = Paths.get("./by_product_files/sample_api/test")
                .toAbsolutePath()
                .normalize();

        customUtil.multipartFileLocalSave(
                saveDirectoryPath,
                null,
                inputVo.getMultipartFile()
        );

        if (inputVo.getMultipartFileNullable() != null) {
            customUtil.multipartFileLocalSave(
                    saveDirectoryPath,
                    null,
                    inputVo.getMultipartFileNullable()
            );
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());

        return new ApiTestController.PostRequestTestWithMultipartFormTypeRequestBodyOutputVo(
                inputVo.getRequestFormString(),
                inputVo.getRequestFormStringNullable(),
                inputVo.getRequestFormInt(),
                inputVo.getRequestFormIntNullable(),
                inputVo.getRequestFormDouble(),
                inputVo.getRequestFormDoubleNullable(),
                inputVo.getRequestFormBoolean(),
                inputVo.getRequestFormBooleanNullable(),
                inputVo.getRequestFormStringList(),
                inputVo.getRequestFormStringListNullable()
        );
    }


    // ----
    // (Post 요청 테스트2 (multipart/form-data))
    public @Nullable ApiTestController.PostRequestTestWithMultipartFormTypeRequestBody2OutputVo postRequestTestWithMultipartFormTypeRequestBody2(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull ApiTestController.PostRequestTestWithMultipartFormTypeRequestBody2InputVo inputVo
    ) throws IOException {
        // 파일 저장 기본 디렉토리 경로
        @NotNull Path saveDirectoryPath = Paths.get("./by_product_files/sample_api/test")
                .toAbsolutePath()
                .normalize();

        // 파일 저장 기본 디렉토리 생성
        Files.createDirectories(saveDirectoryPath);

        for (@NotNull MultipartFile multipartFile : inputVo.getMultipartFileList()) {
            customUtil.multipartFileLocalSave(
                    saveDirectoryPath,
                    null,
                    multipartFile
            );
        }

        if (inputVo.getMultipartFileNullableList() != null) {
            for (@NotNull MultipartFile multipartFileNullable : inputVo.getMultipartFileNullableList()) {
                customUtil.multipartFileLocalSave(
                        saveDirectoryPath,
                        null,
                        multipartFileNullable
                );
            }
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new ApiTestController.PostRequestTestWithMultipartFormTypeRequestBody2OutputVo(
                inputVo.getRequestFormString(),
                inputVo.getRequestFormStringNullable(),
                inputVo.getRequestFormInt(),
                inputVo.getRequestFormIntNullable(),
                inputVo.getRequestFormDouble(),
                inputVo.getRequestFormDoubleNullable(),
                inputVo.getRequestFormBoolean(),
                inputVo.getRequestFormBooleanNullable(),
                inputVo.getRequestFormStringList(),
                inputVo.getRequestFormStringListNullable()
        );
    }


    // ----
    // (Post 요청 테스트 (multipart/form-data - JsonString))
    public @Nullable ApiTestController.PostRequestTestWithMultipartFormTypeRequestBody3OutputVo postRequestTestWithMultipartFormTypeRequestBody3(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull ApiTestController.PostRequestTestWithMultipartFormTypeRequestBody3InputVo inputVo
    ) {
        // JSON String을 객체로 변환
        @NotNull Gson gson = new Gson();
        @NotNull ApiTestController.PostRequestTestWithMultipartFormTypeRequestBody3InputVo.InputJsonObject inputJsonObject =
                gson.fromJson(
                        inputVo.getJsonString(),
                        new TypeToken<ApiTestController.PostRequestTestWithMultipartFormTypeRequestBody3InputVo.InputJsonObject>() {
                        }.getType()
                );

        // 파일 저장 기본 디렉토리 경로
        @NotNull Path saveDirectoryPath = Paths.get("./by_product_files/sample_api/test")
                .toAbsolutePath()
                .normalize();

        customUtil.multipartFileLocalSave(
                saveDirectoryPath,
                null,
                inputVo.getMultipartFile()
        );

        if (inputVo.getMultipartFileNullable() != null) {
            customUtil.multipartFileLocalSave(
                    saveDirectoryPath,
                    null,
                    inputVo.getMultipartFileNullable()
            );
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new ApiTestController.PostRequestTestWithMultipartFormTypeRequestBody3OutputVo(
                inputJsonObject.getRequestFormString(),
                inputJsonObject.getRequestFormStringNullable(),
                inputJsonObject.getRequestFormInt(),
                inputJsonObject.getRequestFormIntNullable(),
                inputJsonObject.getRequestFormDouble(),
                inputJsonObject.getRequestFormDoubleNullable(),
                inputJsonObject.getRequestFormBoolean(),
                inputJsonObject.getRequestFormBooleanNullable(),
                inputJsonObject.getRequestFormStringList(),
                inputJsonObject.getRequestFormStringListNullable()
        );
    }


    // ----
    // (Post 요청 테스트 (multipart/form-data - ObjectList))
    public @Nullable ApiTestController.PostRequestTestWithMultipartFormTypeRequestBody4OutputVo postRequestTestWithMultipartFormTypeRequestBody4(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull ApiTestController.PostRequestTestWithMultipartFormTypeRequestBody4InputVo inputVo
    ) {
        // 파일 저장 기본 디렉토리 경로
        @NotNull Path saveDirectoryPath = Paths.get("./by_product_files/sample_api/test")
                .toAbsolutePath()
                .normalize();

        customUtil.multipartFileLocalSave(
                saveDirectoryPath,
                null,
                inputVo.getMultipartFile()
        );

        if (inputVo.getMultipartFileNullable() != null) {
            customUtil.multipartFileLocalSave(
                    saveDirectoryPath,
                    null,
                    inputVo.getMultipartFileNullable()
            );
        }

        @NotNull List<ApiTestController.PostRequestTestWithMultipartFormTypeRequestBody4OutputVo.InputObject> inputObjectList = new ArrayList<>();
        for (@NotNull ApiTestController.PostRequestTestWithMultipartFormTypeRequestBody4InputVo.InputObject inputObject : inputVo.getInputObjectList()) {
            inputObjectList.add(
                    new ApiTestController.PostRequestTestWithMultipartFormTypeRequestBody4OutputVo.InputObject(
                            inputObject.getRequestFormString(),
                            inputObject.getRequestFormInt()
                    )
            );
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new ApiTestController.PostRequestTestWithMultipartFormTypeRequestBody4OutputVo(inputObjectList);
    }


    // ----
    // (인위적 에러 발생 테스트)
    public void generateErrorTest(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        throw new RuntimeException("Test Error");
    }


    // ----
    // (결과 코드 발생 테스트)
    public void returnResultCodeThroughHeaders(
            @NotNull HttpServletResponse httpServletResponse,
            @Nullable ApiTestController.ReturnResultCodeThroughHeadersErrorTypeEnum errorType
    ) {
        if (errorType == null) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
        } else {
            switch (errorType) {
                case A:
                    httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
                    httpServletResponse.setHeader("api-result-code", "1");
                    break;
                case B:
                    httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
                    httpServletResponse.setHeader("api-result-code", "2");
                    break;
                case C:
                    httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
                    httpServletResponse.setHeader("api-result-code", "3");
                    break;
            }
        }
    }


    // ----
    // (인위적 응답 지연 테스트)
    public void responseDelayTest(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull Long delayTimeSec
    ) {
        @NotNull Long endTime = System.currentTimeMillis() + (delayTimeSec * 1000);

        while (System.currentTimeMillis() < endTime) {
            try {
                Thread.sleep(100);  // 100ms마다 스레드를 잠들게 하여 CPU 사용률을 줄임
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());
    }
}
