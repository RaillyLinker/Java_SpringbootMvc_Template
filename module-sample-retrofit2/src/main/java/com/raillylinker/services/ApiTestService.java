package com.raillylinker.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.raillylinker.controllers.ApiTestController;
import com.raillylinker.util_components.CustomUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

    private final @NotNull Logger classLogger = LoggerFactory.getLogger(ApiTestService.class);

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


    // ----
    // (text/string 반환 샘플)
    public @Nullable String returnTextStringTest(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        httpServletResponse.setStatus(HttpStatus.OK.value());

        return "test Complete!";
    }


    // ----
    // (text/html 반환 샘플)
    public @Nullable ModelAndView returnTextHtmlTest(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        @NotNull ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("return_text_html_test/html_response_example");

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return modelAndView;
    }


    // ----
    // (byte 반환 샘플)
    public @Nullable Resource returnByteDataTest(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new ByteArrayResource(
                new byte[]{
                        'a',
                        'b',
                        'c',
                        'd',
                        'e',
                        'f'
                }
        );
    }


    // ----
    // (비디오 스트리밍 샘플)
    public @Nullable Resource videoStreamingTest(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull ApiTestController.VideoStreamingTestVideoHeight videoHeight
    ) throws IOException {
        // 프로젝트 루트 경로 (프로젝트 settings.gradle 이 있는 경로)
        @NotNull String projectRootAbsolutePathString = new File("").getAbsolutePath();

        // 파일 절대 경로 및 파일명
        @NotNull String serverFileAbsolutePathString =
                projectRootAbsolutePathString + "/module-sample-api/src/main/resources/static/video_streaming_test";

        @NotNull String serverFileNameString = switch (videoHeight) {
            case H240 -> "test_240p.mp4";
            case H360 -> "test_360p.mp4";
            case H480 -> "test_480p.mp4";
            case H720 -> "test_720p.mp4";
        };

        byte[] fileByteArray;
        try (@NotNull FileInputStream fileInputStream = new FileInputStream(serverFileAbsolutePathString + "/" + serverFileNameString)) {
            fileByteArray = fileInputStream.readAllBytes();
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new ByteArrayResource(fileByteArray);
    }


    // ----
    // (오디오 스트리밍 샘플)
    public @Nullable Resource audioStreamingTest(
            @NotNull HttpServletResponse httpServletResponse
    ) throws IOException {
        // 프로젝트 루트 경로 (프로젝트 settings.gradle 이 있는 경로)
        @NotNull String projectRootAbsolutePathString = new File("").getAbsolutePath();

        // 파일 절대 경로 및 파일명
        @NotNull String serverFileAbsolutePathString =
                projectRootAbsolutePathString + "/module-sample-api/src/main/resources/static/audio_streaming_test";
        @NotNull String serverFileNameString = "test.mp3";

        // 반환값에 전해줄 FIS
        byte[] fileByteArray;
        try (@NotNull FileInputStream fileInputStream = new FileInputStream(serverFileAbsolutePathString + "/" + serverFileNameString)) {
            fileByteArray = fileInputStream.readAllBytes();
        }

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new ByteArrayResource(fileByteArray);
    }


    // ----
    // (비동기 처리 결과 반환 샘플)
    public @Nullable DeferredResult<ApiTestController.AsynchronousResponseTestOutputVo> asynchronousResponseTest(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        // 연결 타임아웃 밀리초
        @NotNull Long deferredResultTimeoutMs = 1000L * 60;
        @NotNull DeferredResult<ApiTestController.AsynchronousResponseTestOutputVo> deferredResult =
                new DeferredResult<>(deferredResultTimeoutMs);

        // 비동기 처리
        executorService.execute(() -> {
            @NotNull Long delayMs = 5000L;
            try {
                // 지연시간 대기
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // 결과 반환
            deferredResult.setResult(new ApiTestController.AsynchronousResponseTestOutputVo((delayMs / 1000) + " 초 경과 후 반환했습니다."));
        });

        // 결과 대기 객체를 먼저 반환
        httpServletResponse.setStatus(HttpStatus.OK.value());
        return deferredResult;
    }


    // ----
    // (빈 리스트 받기 테스트)
    public @Nullable ApiTestController.EmptyListRequestTestOutputVo emptyListRequestTest(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull List<String> stringList,
            @NotNull ApiTestController.EmptyListRequestTestInputVo inputVo
    ) {
        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new ApiTestController.EmptyListRequestTestOutputVo(
                stringList,
                inputVo.getRequestBodyStringList()
        );
    }


    // ----
    // (by_product_files 폴더로 파일 업로드)
    public @Nullable ApiTestController.UploadToServerTestOutputVo uploadToServerTest(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull ApiTestController.UploadToServerTestInputVo inputVo
    ) {
        // 파일 저장 기본 디렉토리 경로
        @NotNull Path saveDirectoryPath = Paths.get("./by_product_files/sample_api/test")
                .toAbsolutePath()
                .normalize();

        @NotNull String savedFileName = customUtil.multipartFileLocalSave(
                saveDirectoryPath,
                null,
                inputVo.getMultipartFile()
        );

        httpServletResponse.setStatus(HttpStatus.OK.value());

        return new ApiTestController.UploadToServerTestOutputVo(
                "http://127.0.0.1:12006/api-test/download-from-server/" + savedFileName
        );
    }


    // ----
    // (by_product_files 폴더에서 파일 다운받기)
    public @Nullable ResponseEntity<Resource> fileDownloadTest(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull String fileName
    ) throws IOException {
        // 프로젝트 루트 경로 (settings.gradle이 있는 루트 경로)
        @NotNull String projectRootAbsolutePathString = new File("").getAbsolutePath();

        // 파일 절대 경로
        @NotNull Path serverFilePathObject = Paths.get(projectRootAbsolutePathString, "by_product_files", "sample_api", "test", fileName);

        if (Files.isDirectory(serverFilePathObject) || Files.notExists(serverFilePathObject)) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return null;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.builder("attachment")
                        .filename(fileName, StandardCharsets.UTF_8)
                        .build()
        );
        headers.add(HttpHeaders.CONTENT_TYPE, Files.probeContentType(serverFilePathObject));

        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new ResponseEntity<>(
                new InputStreamResource(Files.newInputStream(serverFilePathObject)),
                headers,
                HttpStatus.OK
        );
    }
}
