package com.raillylinker.services;

import com.google.gson.Gson;
import com.raillylinker.controllers.Retrofit2TestController;
import com.raillylinker.retrofit2_classes.RepositoryNetworkRetrofit2;
import com.raillylinker.retrofit2_classes.request_apis.LocalHostRequestApi;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import okhttp3.*;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class Retrofit2TestService {
    public Retrofit2TestService(
            // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
            @Value("${spring.profiles.active:default}")
            @NotNull String activeProfile
    ) {
        this.activeProfile = activeProfile;
    }

    // <멤버 변수 공간>
    private final @NotNull String activeProfile;

    private final @NotNull Logger classLogger = LoggerFactory.getLogger(Retrofit2TestService.class);

    // Retrofit2 요청 객체
    private final @NotNull RepositoryNetworkRetrofit2 networkRetrofit2 = RepositoryNetworkRetrofit2.instance;


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    // (기본 요청 테스트)
    public @Nullable String basicRequestTest(@NotNull HttpServletResponse httpServletResponse) {
        try {
            // 네트워크 요청
            @NotNull Response<String> responseObj = networkRetrofit2.localHostRequestApi.getMyServiceTkSampleRequestTest().execute();

            if (responseObj.code() == 200) {
                // 정상 동작
                httpServletResponse.setStatus(HttpStatus.OK.value());
                return responseObj.body();
            } else {
                // 반환될 일 없는 상태 = 서버측 에러
                httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
                httpServletResponse.setHeader("api-result-code", "2");
                return null;
            }
        } catch (SocketTimeoutException e) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return null;
        } catch (IOException e) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return null;
        }
    }


    // ----
    // (Redirect 테스트)
    public @Nullable String redirectTest(@NotNull HttpServletResponse httpServletResponse) {
        try {
            // 네트워크 요청
            @NotNull Response<String> responseObj =
                    networkRetrofit2.localHostRequestApi.getMyServiceTkSampleRequestTestRedirectToBlank().execute();

            if (responseObj.code() == 200) {
                // 정상 동작
                httpServletResponse.setStatus(HttpStatus.OK.value());
                return responseObj.body();
            } else {
                // 반환될 일 없는 상태 = 서버측 에러
                httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
                httpServletResponse.setHeader("api-result-code", "2");
                return null;
            }
        } catch (SocketTimeoutException e) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return null;
        } catch (IOException e) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return null;
        }
    }


    // ----
    // (Forward 테스트)
    public @Nullable String forwardTest(@NotNull HttpServletResponse httpServletResponse) {
        try {
            // 네트워크 요청
            @NotNull Response<String> responseObj =
                    networkRetrofit2.localHostRequestApi.getMyServiceTkSampleRequestTestForwardToBlank().execute();

            if (responseObj.code() == 200) {
                // 정상 동작
                httpServletResponse.setStatus(HttpStatus.OK.value());
                return responseObj.body();
            } else {
                // 반환될 일 없는 상태 = 서버측 에러
                httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
                httpServletResponse.setHeader("api-result-code", "2");
                return null;
            }
        } catch (SocketTimeoutException e) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return null;
        } catch (IOException e) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return null;
        }
    }


    // ----
    // (Get 요청 테스트 (Query Parameter))
    public @Nullable Retrofit2TestController.GetRequestTestOutputVo getRequestTest(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        try {
            // 네트워크 요청
            @NotNull Response<LocalHostRequestApi.GetMyServiceTkSampleRequestTestGetRequestOutputVO> responseObj =
                    networkRetrofit2.localHostRequestApi
                            .getMyServiceTkSampleRequestTestGetRequest(
                                    "paramFromServer",
                                    null,
                                    1,
                                    null,
                                    1.1,
                                    null,
                                    true,
                                    null,
                                    List.of("paramFromServer"),
                                    null
                            ).execute();

            if (responseObj.code() == 200) {
                // 정상 동작
                httpServletResponse.setStatus(HttpStatus.OK.value());
                @NotNull LocalHostRequestApi.GetMyServiceTkSampleRequestTestGetRequestOutputVO responseBody = Objects.requireNonNull(responseObj.body());
                return new Retrofit2TestController.GetRequestTestOutputVo(
                        responseBody.queryParamString(),
                        responseBody.queryParamStringNullable(),
                        responseBody.queryParamInt(),
                        responseBody.queryParamIntNullable(),
                        responseBody.queryParamDouble(),
                        responseBody.queryParamDoubleNullable(),
                        responseBody.queryParamBoolean(),
                        responseBody.queryParamBooleanNullable(),
                        responseBody.queryParamStringList(),
                        responseBody.queryParamStringListNullable()
                );
            } else {
                // 반환될 일 없는 상태 = 서버측 에러
                httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
                httpServletResponse.setHeader("api-result-code", "2");
                return null;
            }
        } catch (SocketTimeoutException e) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return null;
        } catch (IOException e) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return null;
        }
    }


    // ----
    // (Get 요청 테스트 (Path Parameter))
    public @Nullable Retrofit2TestController.GetRequestTestWithPathParamOutputVo getRequestTestWithPathParam(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        try {
            // 네트워크 요청
            @NotNull Response<LocalHostRequestApi.GetMyServiceTkSampleRequestTestGetRequestPathParamIntOutputVO> responseObj =
                    networkRetrofit2.localHostRequestApi
                            .getMyServiceTkSampleRequestTestGetRequestPathParamInt(1234)
                            .execute();

            if (responseObj.code() == 200) {
                // 정상 동작
                httpServletResponse.setStatus(HttpStatus.OK.value());
                @NotNull LocalHostRequestApi.GetMyServiceTkSampleRequestTestGetRequestPathParamIntOutputVO responseBody =
                        Objects.requireNonNull(responseObj.body());
                return new Retrofit2TestController.GetRequestTestWithPathParamOutputVo(
                        responseBody.pathParamInt()
                );
            } else {
                // 반환될 일 없는 상태 = 서버측 에러
                httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
                httpServletResponse.setHeader("api-result-code", "2");
                return null;
            }
        } catch (SocketTimeoutException e) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return null;
        } catch (IOException e) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return null;
        }
    }


    // ----
    // (Post 요청 테스트 (Request Body, application/json))
    public @Nullable Retrofit2TestController.PostRequestTestWithApplicationJsonTypeRequestBodyOutputVo postRequestTestWithApplicationJsonTypeRequestBody(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        try {
            // 네트워크 요청
            @NotNull Response<LocalHostRequestApi.PostMyServiceTkSampleRequestTestPostRequestApplicationJsonOutputVO> responseObj =
                    networkRetrofit2.localHostRequestApi
                            .postMyServiceTkSampleRequestTestPostRequestApplicationJson(
                                    new LocalHostRequestApi.PostMyServiceTkSampleRequestTestPostRequestApplicationJsonInputVO(
                                            "paramFromServer",
                                            null,
                                            1,
                                            null,
                                            1.1,
                                            null,
                                            true,
                                            null,
                                            List.of("paramFromServer"),
                                            null
                                    )
                            )
                            .execute();

            if (responseObj.code() == 200) {
                // 정상 동작
                httpServletResponse.setStatus(HttpStatus.OK.value());
                @NotNull LocalHostRequestApi.PostMyServiceTkSampleRequestTestPostRequestApplicationJsonOutputVO responseBody =
                        Objects.requireNonNull(responseObj.body());
                return new Retrofit2TestController.PostRequestTestWithApplicationJsonTypeRequestBodyOutputVo(
                        responseBody.requestBodyString(),
                        responseBody.requestBodyStringNullable(),
                        responseBody.requestBodyInt(),
                        responseBody.requestBodyIntNullable(),
                        responseBody.requestBodyDouble(),
                        responseBody.requestBodyDoubleNullable(),
                        responseBody.requestBodyBoolean(),
                        responseBody.requestBodyBooleanNullable(),
                        responseBody.requestBodyStringList(),
                        responseBody.requestBodyStringListNullable()
                );
            } else {
                // 반환될 일 없는 상태 = 서버측 에러
                httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
                httpServletResponse.setHeader("api-result-code", "2");
                return null;
            }
        } catch (SocketTimeoutException e) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return null;
        } catch (IOException e) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return null;
        }
    }


    // ----
    // (Post 요청 테스트 (Request Body, x-www-form-urlencoded))
    public @Nullable Retrofit2TestController.PostRequestTestWithFormTypeRequestBodyOutputVo postRequestTestWithFormTypeRequestBody(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        try {
            // 네트워크 요청
            @NotNull Response<LocalHostRequestApi.PostMyServiceTkSampleRequestTestPostRequestXWwwFormUrlencodedOutputVO> responseObj =
                    networkRetrofit2.localHostRequestApi
                            .postMyServiceTkSampleRequestTestPostRequestXWwwFormUrlencoded(
                                    "paramFromServer",
                                    null,
                                    1,
                                    null,
                                    1.1,
                                    null,
                                    true,
                                    null,
                                    List.of("paramFromServer"),
                                    null
                            )
                            .execute();

            if (responseObj.code() == 200) {
                // 정상 동작
                httpServletResponse.setStatus(HttpStatus.OK.value());
                @NotNull LocalHostRequestApi.PostMyServiceTkSampleRequestTestPostRequestXWwwFormUrlencodedOutputVO responseBody = Objects.requireNonNull(responseObj.body());
                return new Retrofit2TestController.PostRequestTestWithFormTypeRequestBodyOutputVo(
                        responseBody.requestFormString(),
                        responseBody.requestFormStringNullable(),
                        responseBody.requestFormInt(),
                        responseBody.requestFormIntNullable(),
                        responseBody.requestFormDouble(),
                        responseBody.requestFormDoubleNullable(),
                        responseBody.requestFormBoolean(),
                        responseBody.requestFormBooleanNullable(),
                        responseBody.requestFormStringList(),
                        responseBody.requestFormStringListNullable()
                );
            } else {
                // 반환될 일 없는 상태 = 서버측 에러
                httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
                httpServletResponse.setHeader("api-result-code", "2");
                return null;
            }
        } catch (SocketTimeoutException e) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return null;
        } catch (IOException e) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return null;
        }
    }


    // ----
    // (Post 요청 테스트 (Request Body, multipart/form-data))
    public @Nullable Retrofit2TestController.PostRequestTestWithMultipartFormTypeRequestBodyOutputVo postRequestTestWithMultipartFormTypeRequestBody(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        try {
            // 네트워크 요청
            @NotNull MultipartBody.Part requestFormString = MultipartBody.Part.createFormData("requestFormString", "paramFromServer");
            @NotNull MultipartBody.Part requestFormInt = MultipartBody.Part.createFormData("requestFormInt", Integer.toString(1));
            @NotNull MultipartBody.Part requestFormDouble = MultipartBody.Part.createFormData("requestFormDouble", Double.toString(1.1));
            @NotNull MultipartBody.Part requestFormBoolean = MultipartBody.Part.createFormData("requestFormBoolean", Boolean.toString(true));

            @NotNull List<MultipartBody.Part> requestFormStringList = new ArrayList<>();
            for (@NotNull String requestFormString1 : List.of("paramFromServer")) {
                requestFormStringList.add(MultipartBody.Part.createFormData("requestFormStringList", requestFormString1));
            }

            // 전송 하려는 File
            @NotNull File serverFile =
                    Paths.get(new File("").getAbsolutePath() + "/module-sample-retrofit2/src/main/resources/static/post_request_test_with_multipart_form_type_request_body/test.txt").toFile();
            @NotNull MultipartBody.Part multipartFileFormData =
                    MultipartBody.Part.createFormData(
                            "multipartFile",
                            serverFile.getName(),
                            RequestBody.create(serverFile, MediaType.parse(serverFile.toURI().toURL().openConnection().getContentType()))
                    );

            // 네트워크 요청 전송
            @NotNull Response<LocalHostRequestApi.PostMyServiceTkSampleRequestTestPostRequestMultipartFormDataOutputVO> responseObj =
                    networkRetrofit2.localHostRequestApi
                            .postMyServiceTkSampleRequestTestPostRequestMultipartFormData(
                                    requestFormString,
                                    null,
                                    requestFormInt,
                                    null,
                                    requestFormDouble,
                                    null,
                                    requestFormBoolean,
                                    null,
                                    requestFormStringList,
                                    null,
                                    multipartFileFormData,
                                    null
                            )
                            .execute();

            if (responseObj.code() == 200) {
                // 정상 동작
                httpServletResponse.setStatus(HttpStatus.OK.value());
                @NotNull LocalHostRequestApi.PostMyServiceTkSampleRequestTestPostRequestMultipartFormDataOutputVO responseBody = Objects.requireNonNull(responseObj.body());
                return new Retrofit2TestController.PostRequestTestWithMultipartFormTypeRequestBodyOutputVo(
                        responseBody.requestFormString(),
                        responseBody.requestFormStringNullable(),
                        responseBody.requestFormInt(),
                        responseBody.requestFormIntNullable(),
                        responseBody.requestFormDouble(),
                        responseBody.requestFormDoubleNullable(),
                        responseBody.requestFormBoolean(),
                        responseBody.requestFormBooleanNullable(),
                        responseBody.requestFormStringList(),
                        responseBody.requestFormStringListNullable()
                );
            } else {
                // 반환될 일 없는 상태 = 서버측 에러
                httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
                httpServletResponse.setHeader("api-result-code", "2");
                return null;
            }
        } catch (SocketTimeoutException e) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return null;
        } catch (IOException e) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return null;
        }
    }


    // ----
    // (Post 요청 테스트 (Request Body, multipart/form-data, MultipartFile List))
    public @Nullable Retrofit2TestController.PostRequestTestWithMultipartFormTypeRequestBody2OutputVo postRequestTestWithMultipartFormTypeRequestBody2(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        try {
            // 네트워크 요청
            @NotNull MultipartBody.Part requestFormString = MultipartBody.Part.createFormData("requestFormString", "paramFromServer");
            @NotNull MultipartBody.Part requestFormInt = MultipartBody.Part.createFormData("requestFormInt", Integer.toString(1));
            @NotNull MultipartBody.Part requestFormDouble = MultipartBody.Part.createFormData("requestFormDouble", Double.toString(1.1));
            @NotNull MultipartBody.Part requestFormBoolean = MultipartBody.Part.createFormData("requestFormBoolean", Boolean.toString(true));

            @NotNull List<MultipartBody.Part> requestFormStringList = new ArrayList<>();
            for (@NotNull String requestFormString1 : List.of("paramFromServer")) {
                requestFormStringList.add(MultipartBody.Part.createFormData("requestFormStringList", requestFormString1));
            }

            // 전송 하려는 File
            @NotNull File serverFile1 =
                    Paths.get(new File("").getAbsolutePath() + "/module-sample-retrofit2/src/main/resources/static/post_request_test_with_multipart_form_type_request_body2/test1.txt").toFile();
            @NotNull File serverFile2 =
                    Paths.get(new File("").getAbsolutePath() + "/module-sample-retrofit2/src/main/resources/static/post_request_test_with_multipart_form_type_request_body2/test2.txt").toFile();

            @NotNull List<MultipartBody.Part> multipartFileListFormData = Arrays.asList(
                    MultipartBody.Part.createFormData(
                            "multipartFileList",
                            serverFile1.getName(),
                            RequestBody.create(serverFile1, MediaType.parse(serverFile1.toURI().toURL().openConnection().getContentType()))
                    ),
                    MultipartBody.Part.createFormData(
                            "multipartFileList",
                            serverFile2.getName(),
                            RequestBody.create(serverFile2, MediaType.parse(serverFile2.toURI().toURL().openConnection().getContentType()))
                    )
            );

            // 네트워크 요청
            @NotNull Response<LocalHostRequestApi.PostMyServiceTkSampleRequestTestPostRequestMultipartFormData2VO> responseObj = networkRetrofit2.localHostRequestApi
                    .postMyServiceTkSampleRequestTestPostRequestMultipartFormData2(
                            requestFormString,
                            null,
                            requestFormInt,
                            null,
                            requestFormDouble,
                            null,
                            requestFormBoolean,
                            null,
                            requestFormStringList,
                            null,
                            multipartFileListFormData,
                            null
                    ).execute();

            if (responseObj.code() == 200) {
                // 정상 동작
                httpServletResponse.setStatus(HttpStatus.OK.value());
                @NotNull LocalHostRequestApi.PostMyServiceTkSampleRequestTestPostRequestMultipartFormData2VO responseBody = Objects.requireNonNull(responseObj.body());
                return new Retrofit2TestController.PostRequestTestWithMultipartFormTypeRequestBody2OutputVo(
                        responseBody.requestFormString(),
                        responseBody.requestFormStringNullable(),
                        responseBody.requestFormInt(),
                        responseBody.requestFormIntNullable(),
                        responseBody.requestFormDouble(),
                        responseBody.requestFormDoubleNullable(),
                        responseBody.requestFormBoolean(),
                        responseBody.requestFormBooleanNullable(),
                        responseBody.requestFormStringList(),
                        responseBody.requestFormStringListNullable()
                );
            } else {
                // 반환될 일 없는 상태 = 서버측 에러
                httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
                httpServletResponse.setHeader("api-result-code", "2");
                return null;
            }
        } catch (SocketTimeoutException e) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return null;
        } catch (IOException e) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return null;
        }
    }


    // ----
    // (Post 요청 테스트 (Request Body, multipart/form-data, with jsonString))
    public @Nullable Retrofit2TestController.PostRequestTestWithMultipartFormTypeRequestBody3OutputVo postRequestTestWithMultipartFormTypeRequestBody3(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        try {
            // 네트워크 요청
            @NotNull MultipartBody.Part jsonStringFormData = MultipartBody.Part.createFormData(
                    "jsonString", new Gson().toJson(
                            new LocalHostRequestApi.PostMyServiceTkSampleRequestTestPostRequestMultipartFormDataJsonJsonStringVo(
                                    "paramFromServer",
                                    null,
                                    1,
                                    null,
                                    1.1,
                                    null,
                                    true,
                                    null,
                                    List.of("paramFromServer"),
                                    null
                            )
                    )
            );

            // 전송 하려는 File
            @NotNull File serverFile = Paths.get(new File("").getAbsolutePath() + "/module-sample-retrofit2/src/main/resources/static/post_request_test_with_multipart_form_type_request_body3/test.txt").toFile();
            @NotNull MultipartBody.Part multipartFileFormData = MultipartBody.Part.createFormData(
                    "multipartFile",
                    serverFile.getName(),
                    RequestBody.create(serverFile, MediaType.parse(serverFile.toURI().toURL().openConnection().getContentType()))
            );

            // 네트워크 요청
            @NotNull Response<LocalHostRequestApi.PostMyServiceTkSampleRequestTestPostRequestMultipartFormDataJsonOutputVO> responseObj =
                    networkRetrofit2.localHostRequestApi
                            .postMyServiceTkSampleRequestTestPostRequestMultipartFormDataJson(
                                    jsonStringFormData,
                                    multipartFileFormData,
                                    null
                            ).execute();

            if (responseObj.code() == 200) {
                // 정상 동작
                httpServletResponse.setStatus(HttpStatus.OK.value());
                @NotNull LocalHostRequestApi.PostMyServiceTkSampleRequestTestPostRequestMultipartFormDataJsonOutputVO responseBody = Objects.requireNonNull(responseObj.body());
                return new Retrofit2TestController.PostRequestTestWithMultipartFormTypeRequestBody3OutputVo(
                        responseBody.requestFormString(),
                        responseBody.requestFormStringNullable(),
                        responseBody.requestFormInt(),
                        responseBody.requestFormIntNullable(),
                        responseBody.requestFormDouble(),
                        responseBody.requestFormDoubleNullable(),
                        responseBody.requestFormBoolean(),
                        responseBody.requestFormBooleanNullable(),
                        responseBody.requestFormStringList(),
                        responseBody.requestFormStringListNullable()
                );
            } else {
                // 반환될 일 없는 상태 = 서버측 에러
                httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
                httpServletResponse.setHeader("api-result-code", "2");
                return null;
            }
        } catch (SocketTimeoutException e) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return null;
        } catch (IOException e) {
            httpServletResponse.setHeader("api-result-code", "1");
            return null;
        }
    }


    // ----
    // (에러 발생 테스트)
    public void generateErrorTest(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        try {
            // 네트워크 요청
            @NotNull Response<Void> responseObj = networkRetrofit2.localHostRequestApi
                    .postMyServiceTkSampleRequestTestGenerateError().execute();

            if (responseObj.code() == 200) {
                // 정상 동작
                httpServletResponse.setStatus(HttpStatus.OK.value());
            } else {
                // 반환될 일 없는 상태 = 서버측 에러
                httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
                httpServletResponse.setHeader("api-result-code", "2");
            }
        } catch (IOException e) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
        }
    }


    // ----
    // (api-result-code 반환 테스트)
    public void returnResultCodeThroughHeaders(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        try {
            // 네트워크 요청
            @NotNull Response<Void> responseObj = networkRetrofit2.localHostRequestApi
                    .postMyServiceTkSampleRequestTestApiResultCodeTest(
                            LocalHostRequestApi.PostMyServiceTkSampleRequestTestApiResultCodeTestErrorTypeEnum.A
                    ).execute();

            if (responseObj.code() == 200) {
                // 정상 동작
                httpServletResponse.setStatus(HttpStatus.OK.value());
            } else if (responseObj.code() == 204) {
                // api-result-code 확인 필요
                @NotNull Headers responseHeaders = responseObj.headers();
                @Nullable String apiResultCode = responseHeaders.get("api-result-code");

                // api-result-code 분기
                switch (Objects.requireNonNull(apiResultCode)) {
                    case "1":
                        httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
                        httpServletResponse.setHeader("api-result-code", "3");
                        break;

                    case "2":
                        httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
                        httpServletResponse.setHeader("api-result-code", "4");
                        break;

                    case "3":
                        httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
                        httpServletResponse.setHeader("api-result-code", "5");
                        break;

                    default:
                        // 알수없는 api-result-code
                        httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
                        httpServletResponse.setHeader("api-result-code", "2");
                        break;
                }
            } else {
                // 반환될 일 없는 상태 = 서버측 에러
                httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
                httpServletResponse.setHeader("api-result-code", "2");
            }
        } catch (IOException e) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
        }
    }


    // ----
    // (응답 지연 발생 테스트)
    public void responseDelayTest(
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull Long delayTimeSec
    ) {
        try {
            // 네트워크 요청
            @NotNull Response<Void> responseObj = networkRetrofit2.localHostRequestApi
                    .postMyServiceTkSampleRequestTestGenerateTimeOutError(delayTimeSec)
                    .execute();

            if (responseObj.code() == 200) {
                // 정상 동작
                httpServletResponse.setStatus(HttpStatus.OK.value());
            } else {
                // 반환될 일 없는 상태 = 서버측 에러
                httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
                httpServletResponse.setHeader("api-result-code", "2");
            }
        } catch (IOException e) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
        }
    }


    // ----
    // (text/string 형식 Response 받아오기)
    public @Nullable String returnTextStringTest(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        try {
            // 네트워크 요청
            @NotNull Response<String> responseObj = networkRetrofit2.localHostRequestApi
                    .getMyServiceTkSampleRequestTestReturnTextString().execute();

            if (responseObj.code() == 200) {
                // 정상 동작
                httpServletResponse.setStatus(HttpStatus.OK.value());
                return responseObj.body();
            } else {
                // 반환될 일 없는 상태 = 서버측 에러
                httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
                httpServletResponse.setHeader("api-result-code", "2");
                return null;
            }
        } catch (IOException e) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return null;
        }
    }


    // ----
    // (text/html 형식 Response 받아오기)
    public @Nullable String returnTextHtmlTest(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        try {
            // 네트워크 요청
            @NotNull Response<String> responseObj = networkRetrofit2.localHostRequestApi
                    .getMyServiceTkSampleRequestTestReturnTextHtml().execute();

            if (responseObj.code() == 200) {
                // 정상 동작
                httpServletResponse.setStatus(HttpStatus.OK.value());
                return responseObj.body();
            } else {
                // 반환될 일 없는 상태 = 서버측 에러
                httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
                httpServletResponse.setHeader("api-result-code", "2");
                return null;
            }
        } catch (IOException e) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return null;
        }
    }


    // ----
    // (DeferredResult Get 요청 테스트)
    public @Nullable Retrofit2TestController.AsynchronousResponseTestOutputVo asynchronousResponseTest(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        try {
            // 네트워크 요청
            @NotNull Response<LocalHostRequestApi.GetMyServiceTkSampleRequestTestAsyncResultOutputVO> responseObj = networkRetrofit2.localHostRequestApi
                    .getMyServiceTkSampleRequestTestAsyncResult().execute();

            if (responseObj.code() == 200) {
                // 정상 동작
                httpServletResponse.setStatus(HttpStatus.OK.value());
                @NotNull LocalHostRequestApi.GetMyServiceTkSampleRequestTestAsyncResultOutputVO responseBody = Objects.requireNonNull(responseObj.body());
                return new Retrofit2TestController.AsynchronousResponseTestOutputVo(responseBody.resultMessage());
            } else {
                // 반환될 일 없는 상태 = 서버측 에러
                httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
                httpServletResponse.setHeader("api-result-code", "2");
                return null;
            }
        } catch (IOException e) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            httpServletResponse.setHeader("api-result-code", "1");
            return null;
        }
    }


    // todo
    // ----
    // (SSE 구독 테스트)
//    public void sseSubscribeTest(HttpServletResponse httpServletResponse) {
//        // SSE Subscribe Url 연결 객체 생성
//        SseClient sseClient = new SseClient("http://127.0.0.1:8080/my-service/tk/sample/request-test/sse-test/subscribe");
//
//        final int maxCount = 5;
//        final AtomicInteger count = new AtomicInteger(0);
//
//        // SSE 구독 연결
//        sseClient.connect(5000L, new SseClient.ListenerCallback() {
//            @Override
//            public void onConnectRequestFirstTime(
//                    @Valid @jakarta.validation.constraints.NotNull @org.jetbrains.annotations.NotNull
//                    SseClient sse,
//                    @Valid @jakarta.validation.constraints.NotNull @org.jetbrains.annotations.NotNull
//                    Request originalRequest
//            ) {
//                classLogger.info("++++ api17 : onConnectRequestFirstTime");
//            }
//
//            @Override
//            public void onConnect(
//                    @Valid @jakarta.validation.constraints.NotNull @org.jetbrains.annotations.NotNull
//                    SseClient sse,
//                    @Valid @jakarta.validation.constraints.NotNull @org.jetbrains.annotations.NotNull
//                    okhttp3.Response response
//            ) {
//                classLogger.info("++++ api17 : onOpen");
//            }
//
//            @Override
//            public void onMessageReceive(
//                    @Valid @jakarta.validation.constraints.NotNull @org.jetbrains.annotations.NotNull
//                    SseClient sse,
//                    @Nullable @org.jetbrains.annotations.Nullable
//                    String eventId,
//                    @Valid @jakarta.validation.constraints.NotNull @org.jetbrains.annotations.NotNull
//                    String event,
//                    @Valid @jakarta.validation.constraints.NotNull @org.jetbrains.annotations.NotNull
//                    String message
//            ) {
//                classLogger.info("++++ api17 : onMessage : " + event + " " + message);
//
//                // maxCount 만큼 반복했다면 연결 끊기
//                if (maxCount == count.incrementAndGet()) {
//                    sseClient.disconnect();
//                }
//            }
//
//            @Override
//            public void onCommentReceive(
//                    @Valid @jakarta.validation.constraints.NotNull @org.jetbrains.annotations.NotNull
//                    SseClient sse,
//                    @Valid @jakarta.validation.constraints.NotNull @org.jetbrains.annotations.NotNull
//                    String comment
//            ) {
//                classLogger.info("++++ api17 : onComment : " + comment);
//            }
//
//            @Override
//            public boolean onPreRetry(
//                    @Valid @jakarta.validation.constraints.NotNull @org.jetbrains.annotations.NotNull
//                    SseClient sse,
//                    @Valid @jakarta.validation.constraints.NotNull @org.jetbrains.annotations.NotNull
//                    Request originalRequest,
//                    @Valid @jakarta.validation.constraints.NotNull @org.jetbrains.annotations.NotNull
//                    Throwable throwable,
//                    @Nullable @org.jetbrains.annotations.Nullable
//                    okhttp3.Response response
//            ) {
//
//                classLogger.info("++++ api17 : onPreRetry");
//                return true;
//            }
//
//            @Override
//            public void onDisconnected(
//                    @Valid @jakarta.validation.constraints.NotNull @org.jetbrains.annotations.NotNull
//                    SseClient sse
//            ) {
//                classLogger.info("++++ api17 : onClosed");
//            }
//        });
//
//        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
//    }


    // ----
    // (WebSocket 연결 테스트)
    public void websocketConnectTest(
            @NotNull HttpServletResponse httpServletResponse
    ) {
        @NotNull OkHttpClient client = new OkHttpClient();

        final @NotNull Integer maxCount = 5;
        final @NotNull AtomicInteger count = new AtomicInteger(0);

        @NotNull WebSocket webSocket = client.newWebSocket(
                new Request.Builder()
                        .url("ws://localhost:12004/ws/test/websocket")
                        .build(),
                new WebSocketListener() {
                    @Override
                    public void onOpen(
                            @NotNull WebSocket webSocket,
                            @NotNull okhttp3.Response response
                    ) {
                        classLogger.info("++++ api18 : onOpen");
                        webSocket.send(
                                new Gson().toJson(
                                        new MessagePayloadVo(
                                                "++++ api18 Client",
                                                "i am open!"
                                        )
                                )
                        );
                    }

                    @Override
                    public void onMessage(
                            @NotNull WebSocket webSocket,
                            @NotNull String text
                    ) {
                        // maxCount 만큼 반복했다면 연결 끊기
                        if (maxCount == count.incrementAndGet()) {
                            webSocket.close(1000, null);
                        }

                        classLogger.info("++++ api18 : onMessage : " + text);

                        // 메세지를 받으면 바로 메세지를 보내기
                        webSocket.send(
                                new Gson().toJson(
                                        new MessagePayloadVo(
                                                "++++ api18 Client",
                                                "reply!"
                                        )
                                )
                        );
                    }

                    @Override
                    public void onMessage(
                            @NotNull WebSocket webSocket,
                            @NotNull ByteString bytes
                    ) {
                        classLogger.info("++++ api18 : onMessage : " + bytes);
                    }

                    @Override
                    public void onClosing(
                            @NotNull WebSocket webSocket,
                            int code,
                            @NotNull String reason) {
                        classLogger.info("++++ api18 : onClosing");
                    }

                    @Override
                    public void onFailure(
                            @NotNull WebSocket webSocket,
                            @NotNull Throwable t,
                            @Nullable okhttp3.Response response
                    ) {
                        classLogger.info("++++ api18 : onFailure");
                        t.printStackTrace();
                    }
                });

        client.dispatcher().executorService().shutdown();

        classLogger.info(webSocket.toString());

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
    }

    @Data
    public static class MessagePayloadVo {
        private final @NotNull String sender; // 송신자 (실제로는 JWT 로 보안 처리를 할 것)
        private final @NotNull String message;
    }
}
