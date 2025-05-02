package com.raillylinker.services;

import com.raillylinker.controllers.RedisTestController;
import com.raillylinker.redis_map_components.redis1_main.Redis1_Lock_Test;
import com.raillylinker.redis_map_components.redis1_main.Redis1_Map_Test;
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

/*
    Redis 는 주로 캐싱에 사용됩니다.
    이러한 특징에 기반하여, 본 프로젝트에서는 Redis 를 쉽고 편하게 사용하기 위하여 Key-Map 형식으로 래핑하여 사용하고 있으며,
    인 메모리 데이터 구조 저장소인 Redis 의 성능을 해치지 않기 위하여, Database 와는 달리 트랜젝션 처리를 따로 하지 않습니다.
    (Redis 는 애초에 고성능, 단순성을 위해 설계되었고, 롤백 기능을 지원하지 않으므로 일반적으로는 어플리케이션 레벨에서 처리합니다.)
    고로 Redis 에 값을 입력/삭제/수정하는 로직은, API 의 별도 다른 알고리즘이 모두 실행된 이후, "코드의 끝자락에서 한꺼번에 변경"하도록 처리하세요.
    그럼에도 트랜젝션 기능이 필요하다고 한다면,
    비동기 실행을 고려하여 Semaphore 등으로 락을 건 후, 기존 데이터를 백업한 후, 에러가 일어나면 복원하는 방식을 사용하면 됩니다.
 */
@Service
public class RedisTestService {
    public RedisTestService(
            // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
            @Value("${spring.profiles.active:default}")
            @NotNull String activeProfile,
            @NotNull Redis1_Map_Test redis1Test,
            @NotNull Redis1_Lock_Test redis1LockTest
    ) {
        this.activeProfile = activeProfile;
        this.redis1Test = redis1Test;
        this.redis1LockTest = redis1LockTest;
    }

    // <멤버 변수 공간>
    private final @NotNull String activeProfile;

    private final @NotNull Logger classLogger = LoggerFactory.getLogger(RootService.class);

    private final @NotNull Redis1_Map_Test redis1Test;
    private final @NotNull Redis1_Lock_Test redis1LockTest;


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    // todo


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
    public @Nullable RedisTestController.GetRequestTestOutputVo getRequestTest(
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
        return new RedisTestController.GetRequestTestOutputVo(
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
    public @Nullable RedisTestController.GetRequestTestWithPathParamOutputVo getRequestTestWithPathParam(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull Integer pathParamInt
    ) {
        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new RedisTestController.GetRequestTestWithPathParamOutputVo(
                pathParamInt
        );
    }


    // ----
    // (Post 요청 테스트 (application-json))
    public @Nullable RedisTestController.PostRequestTestWithApplicationJsonTypeRequestBodyOutputVo postRequestTestWithApplicationJsonTypeRequestBody(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull RedisTestController.PostRequestTestWithApplicationJsonTypeRequestBodyInputVo inputVo
    ) {
        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new RedisTestController.PostRequestTestWithApplicationJsonTypeRequestBodyOutputVo(
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
    public @Nullable RedisTestController.PostRequestTestWithApplicationJsonTypeRequestBody2OutputVo postRequestTestWithApplicationJsonTypeRequestBody2(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull RedisTestController.PostRequestTestWithApplicationJsonTypeRequestBody2InputVo inputVo
    ) {
        @NotNull ArrayList<RedisTestController.PostRequestTestWithApplicationJsonTypeRequestBody2OutputVo.ObjectVo> objectList =
                new ArrayList<>();

        for (@NotNull RedisTestController.PostRequestTestWithApplicationJsonTypeRequestBody2InputVo.ObjectVo objectVo : inputVo.getObjectVoList()) {
            @NotNull ArrayList<RedisTestController.PostRequestTestWithApplicationJsonTypeRequestBody2OutputVo.ObjectVo.SubObjectVo> subObjectVoList =
                    new ArrayList<>();
            for (@NotNull RedisTestController.PostRequestTestWithApplicationJsonTypeRequestBody2InputVo.ObjectVo.SubObjectVo subObject : objectVo.getSubObjectVoList()) {
                subObjectVoList.add(
                        new RedisTestController.PostRequestTestWithApplicationJsonTypeRequestBody2OutputVo.ObjectVo.SubObjectVo(
                                subObject.getRequestBodyString(),
                                subObject.getRequestBodyStringList()
                        )
                );
            }

            objectList.add(
                    new RedisTestController.PostRequestTestWithApplicationJsonTypeRequestBody2OutputVo.ObjectVo(
                            objectVo.getRequestBodyString(),
                            objectVo.getRequestBodyStringList(),
                            new RedisTestController.PostRequestTestWithApplicationJsonTypeRequestBody2OutputVo.ObjectVo.SubObjectVo(
                                    objectVo.getSubObjectVo().getRequestBodyString(),
                                    objectVo.getSubObjectVo().getRequestBodyStringList()
                            ),
                            subObjectVoList
                    )
            );
        }

        @NotNull ArrayList<RedisTestController.PostRequestTestWithApplicationJsonTypeRequestBody2OutputVo.ObjectVo.SubObjectVo> subObjectVoList =
                new ArrayList<>();
        for (@NotNull RedisTestController.PostRequestTestWithApplicationJsonTypeRequestBody2InputVo.ObjectVo.SubObjectVo subObject : inputVo.getObjectVo().getSubObjectVoList()) {
            subObjectVoList.add(
                    new RedisTestController.PostRequestTestWithApplicationJsonTypeRequestBody2OutputVo.ObjectVo.SubObjectVo(
                            subObject.getRequestBodyString(),
                            subObject.getRequestBodyStringList()
                    )
            );
        }

        @NotNull RedisTestController.PostRequestTestWithApplicationJsonTypeRequestBody2OutputVo outputVo =
                new RedisTestController.PostRequestTestWithApplicationJsonTypeRequestBody2OutputVo(
                        new RedisTestController.PostRequestTestWithApplicationJsonTypeRequestBody2OutputVo.ObjectVo(
                                inputVo.getObjectVo().getRequestBodyString(),
                                inputVo.getObjectVo().getRequestBodyStringList(),
                                new RedisTestController.PostRequestTestWithApplicationJsonTypeRequestBody2OutputVo.ObjectVo.SubObjectVo(
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
    public @Nullable RedisTestController.PostRequestTestWithFormTypeRequestBodyOutputVo postRequestTestWithFormTypeRequestBody(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull RedisTestController.PostRequestTestWithFormTypeRequestBodyInputVo inputVo
    ) {
        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new RedisTestController.PostRequestTestWithFormTypeRequestBodyOutputVo(
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
            @Nullable RedisTestController.ReturnResultCodeThroughHeadersErrorTypeEnum errorType
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
            @NotNull RedisTestController.VideoStreamingTestVideoHeight videoHeight
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
    // (빈 리스트 받기 테스트)
    public @Nullable RedisTestController.EmptyListRequestTestOutputVo emptyListRequestTest(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull List<String> stringList,
            @NotNull RedisTestController.EmptyListRequestTestInputVo inputVo
    ) {
        httpServletResponse.setStatus(HttpStatus.OK.value());
        return new RedisTestController.EmptyListRequestTestOutputVo(
                stringList,
                inputVo.getRequestBodyStringList()
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
